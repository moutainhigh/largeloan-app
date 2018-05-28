package com.xianjinxia.cashman.service.impl;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.ImmutableList;
import com.google.gson.Gson;
import com.xianjingxia.paymentclient.paycenter.enums.BusTypeEnum;
import com.xianjinxia.cashman.constants.Constant;
import com.xianjinxia.cashman.constants.ErrorCodeConstants;
import com.xianjinxia.cashman.constants.QueueConstants;
import com.xianjinxia.cashman.domain.*;
import com.xianjinxia.cashman.dto.*;
import com.xianjinxia.cashman.enums.*;
import com.xianjinxia.cashman.domain.CollectRequest;
import com.xianjinxia.cashman.domain.LoanOrder;
import com.xianjinxia.cashman.domain.PaymentRequest;
import com.xianjinxia.cashman.domain.Products;
import com.xianjinxia.cashman.domain.RepaymentPlan;
import com.xianjinxia.cashman.domain.RepaymentRecord;
import com.xianjinxia.cashman.dto.AlipayRepamentPlanDto;
import com.xianjinxia.cashman.dto.AlipayRepaymentCallbackDto;
import com.xianjinxia.cashman.dto.CountRepaymentPlanDto;
import com.xianjinxia.cashman.dto.IndexRepaymentPlanDto;
import com.xianjinxia.cashman.dto.LoanOrderRepayedDto;
import com.xianjinxia.cashman.dto.QuotaGiveBackDto;
import com.xianjinxia.cashman.dto.RepaymentNoticeDto;
import com.xianjinxia.cashman.dto.SmsDto;
import com.xianjinxia.cashman.dto.SyncLoanOrderDto;
import com.xianjinxia.cashman.enums.IdempotentEventTypeEnum;
import com.xianjinxia.cashman.enums.ManualIncomOverdueStatusEnum;
import com.xianjinxia.cashman.enums.MerchantNoEnum;
import com.xianjinxia.cashman.enums.NotifyCollectionTypeEnum;
import com.xianjinxia.cashman.enums.PayCenterRepaymentStatusEnum;
import com.xianjinxia.cashman.enums.PaymentBizTypeEnum;
import com.xianjinxia.cashman.enums.PaymentCenterBizTypeEnum;
import com.xianjinxia.cashman.enums.PaymentOrderTypeEnum;
import com.xianjinxia.cashman.enums.PaymentRequestStatusEnum;
import com.xianjinxia.cashman.enums.ProductCategoryEnum;
import com.xianjinxia.cashman.enums.RepaymentPlanOperationFlagEnum;
import com.xianjinxia.cashman.enums.RepaymentPlanStatusEnum;
import com.xianjinxia.cashman.enums.TrdLoanOrderStatusEnum;
import com.xianjinxia.cashman.exceptions.CashmanExceptionBuilder;
import com.xianjinxia.cashman.exceptions.ServiceException;
import com.xianjinxia.cashman.idempotent.IdempotentService;
import com.xianjinxia.cashman.mapper.*;
import com.xianjinxia.cashman.remote.PayCenterRemoteService;
import com.xianjinxia.cashman.request.AlipayRepayIncomeReq;
import com.xianjinxia.cashman.request.CollectWithholdReq;
import com.xianjinxia.cashman.request.CollectionDeductReq;
import com.xianjinxia.cashman.request.PayCenterCallbackReq;
import com.xianjinxia.cashman.request.PaymentCenterCallbackRequestCode;
import com.xianjinxia.cashman.request.RepaymentReq;
import com.xianjinxia.cashman.request.VerifyPaymentReq;
import com.xianjinxia.cashman.service.ILoanOrderService;
import com.xianjinxia.cashman.service.IMqMessageService;
import com.xianjinxia.cashman.service.IRepaymentService;
import com.xianjinxia.cashman.service.ISmsService;
import com.xianjinxia.cashman.service.repay.IPaymentRequestService;
import com.xianjinxia.cashman.service.repay.IRepaymentPlanService;
import com.xianjinxia.cashman.service.repay.IRepaymentRecordService;
import com.xianjinxia.cashman.service.repay.RepaymentPlanSpliter;
import com.xianjinxia.cashman.service.repay.checker.RepaymentChecker;
import com.xianjinxia.cashman.service.repay.collection.CollectionNotifyDto;
import com.xianjinxia.cashman.service.repay.collection.CollectionNotifyDtoBuilder;
import com.xianjinxia.cashman.service.repay.collection.CollectionNotifyService;
import com.xianjinxia.cashman.service.repay.confirm.RepaymentConfirmContext;
import com.xianjinxia.cashman.service.repay.confirm.RepaymentConfirmProcessParam;
import com.xianjinxia.cashman.service.repay.deduct.DeductStrategyContext;
import com.xianjinxia.cashman.service.repay.paycenter.IPayCenterService;
import com.xianjinxia.cashman.utils.DateUtil;
import com.xianjinxia.cashman.utils.MoneyUtil;
import com.xjx.mqclient.pojo.MqMessage;
import com.xjx.mqclient.service.MqClient;
import org.apache.commons.lang.BooleanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.*;

//import com.xianjinxia.cashman.enums.LoanOrderStatusEnum;

@Service
public class RepaymentServiceImpl implements IRepaymentService {

    private static final Logger logger = LoggerFactory.getLogger(RepaymentServiceImpl.class);

    @Autowired
    private IRepaymentPlanService repaymentPlanService;

    @Autowired
    private RepaymentConfirmContext repaymentConfirmContext;

    @Autowired
    private IPaymentRequestService paymentRequestService;

    @Autowired
    private IRepaymentRecordService repaymentRecordService;

    @Autowired
    private RepaymentChecker repaymentChecker;

    @Autowired
    private IPayCenterService payCenterService;

    @Autowired
    private PaymentRequestConfigMapper paymentRequestConfigMapper;

    @Autowired
    private ProductsMapper productsMapper;

    @Autowired
    private LoanOrderMapper loanOrderMapper;

    @Autowired
    private IMqMessageService mqMessageService;

    @Autowired
    private PayCenterRemoteService payCenterRemoteService;

    @Autowired
    private ILoanOrderService loanOrderService;
    @Autowired
    private RepaymentPlanMapper repaymentPlanMapper;
    @Autowired
    private ISmsService smsService;

    @Autowired
    private DeductStrategyContext deductStrategyContext;

    @Autowired
    private CollectRequestMapper collectRequestMapper;

    @Autowired
    private RepaymentPlanSpliter repaymentPlanSpliter;

    @Autowired
    protected CollectionNotifyDtoBuilder collectionNotifyDtoBuilder;

    @Autowired
    protected CollectionNotifyService collectionNotifyService;

    @Autowired
    protected ScheduleTaskOverdueMapper scheduleTaskOverdueMapper;

    @Autowired
    private MqClient mqClient;

    @Autowired
    private IdempotentService idempotentService;


    @Override
    public RepaymentPlan getRepaymentOrderById(String id) {
        return repaymentPlanService.getRepaymentPlanById(Long.parseLong(id));
    }

    @Override
    @Transactional
    public List<RepaymentPlan> getRepaymentPlanListByTrdLoanOrderId(Long trdLoanOrderId) {
        // 在查询还款计划列表的时候刷新数据
        LoanOrder loanOrder = loanOrderMapper.selectByTrdOrderId(trdLoanOrderId);
        return repaymentPlanService.getRepaymentOrderListByLoanOrderId(trdLoanOrderId);
    }


    @Override
    @Transactional
    public void repayCommit(RepaymentReq repaymentReq) {
        Long userId = repaymentReq.getUserId();
        Integer repaymentAmount = repaymentReq.getAmount();
        PaymentBizTypeEnum paymentBizTypeEnum = repaymentReq.getPaymentBizTypeEnum();


        // 前置条件：有且只有1笔正在支付的请求
        PaymentRequest existPaymentRequest = paymentRequestService.getPaymentRequestByUserIdAndStatus(userId, PaymentRequestStatusEnum.NEW);
        if (!ObjectUtils.isEmpty(existPaymentRequest)) {
            throw new ServiceException("您当前有一笔正在支付的订单，请稍后尝试还款");
        }

        List<RepaymentPlan> allRepaymentPlanList = repaymentPlanService.getRepaymentOrderListByLoanOrderId(repaymentReq.getTrdLoanOrderId(), false);
        Integer totalWaitRepayAmount = this.getTotalWaitRepayAmount(allRepaymentPlanList);
        Integer refundAmount = repaymentAmount - totalWaitRepayAmount;
        if (repaymentAmount > totalWaitRepayAmount) {
            throw new ServiceException("还款金额不能超过待还款订单总金额");
        }
        List<RepaymentPlan> matchedRepaymentPlanList = repaymentPlanSpliter.splitByRepayAmount(repaymentAmount, allRepaymentPlanList);
        Products product = productsMapper.selectById(matchedRepaymentPlanList.get(0).getProductId());
        LoanOrder loanOrder = loanOrderMapper.selectByTrdOrderId(repaymentReq.getTrdLoanOrderId());
        boolean isHistoryOverdue = this.isHistoryOverdue(allRepaymentPlanList);
        boolean isRepayAll = this.isRepayAll(allRepaymentPlanList, matchedRepaymentPlanList);
        boolean isLoanRepayOneDay = this.isLoanRepayOneDay(loanOrder);

        // 3.创建支付请求, 创建还款记录，冻结还款计划金额
        PaymentRequest paymentRequest = paymentRequestService.createPaymentRequest(userId, repaymentAmount, PaymentRequestStatusEnum.NEW, paymentBizTypeEnum);

        List<PaymentInstallmentDto> paymentInstallmentDtos = new ArrayList<>();
        for (int i = 0; i < matchedRepaymentPlanList.size(); i++) {
            RepaymentPlan repaymentPlan = matchedRepaymentPlanList.get(i);
            // 3.2 创建还款记录
            RepaymentRecord repaymentRecord = repaymentRecordService.buildRepaymentRecord(paymentRequest.getId(), repaymentPlan, paymentBizTypeEnum);
            repaymentRecordService.addRepaymentRecord(repaymentRecord);
            // 3.2冻结还款计划中的金额
            repaymentPlanService.increaseWaitingAmount(repaymentPlan.getId(), repaymentRecord.getAmount(), repaymentRecord.getRepayPrincipalAmt(), repaymentRecord.getRepayInterestAmt(), repaymentRecord.getRepayOverdueAmt(), repaymentPlan.getVersion());

            PaymentInstallmentDto paymentInstallmentDto = new PaymentInstallmentDto();
            // 这里的费用按照匹配到的repayment plan进行封装，
            paymentInstallmentDto.setPeriod(repaymentPlan.getPeriod());
            paymentInstallmentDto.setInstallmentAmount(repaymentRecord.getAmount());
            paymentInstallmentDto.setInstallmentPrincipal(repaymentRecord.getRepayPrincipalAmt());
            paymentInstallmentDto.setInstallInterest(repaymentRecord.getRepayInterestAmt());
            paymentInstallmentDtos.add(paymentInstallmentDto);
        }


        payCenterService.payWithMQ(
                userId.toString(),
                paymentRequest.getId(),
                paymentRequest.getAmount(),
                PaymentCenterBizTypeEnum.BIG_AMT_USER_REPAY,
                JSON.toJSONString(paymentRequest),
                isHistoryOverdue,
                isRepayAll,
                isLoanRepayOneDay,
                false,
                paymentInstallmentDtos,
                loanOrder,
                repaymentReq.getUserInfo()
        );
    }

    private Integer getTotalWaitRepayAmount(List<RepaymentPlan> repaymentPlans) {
        Integer totalWaitRepayAmouny = 0;

        for (Iterator<RepaymentPlan> iterator = repaymentPlans.iterator(); iterator.hasNext(); ) {
            RepaymentPlan repaymentPlan = iterator.next();
            totalWaitRepayAmouny += repaymentPlan.getRepaymentTotalAmount();
        }
        return totalWaitRepayAmouny;
    }

    @Override
    @Transactional
    public void repayIncomeByAlipay(AlipayRepayIncomeReq alipayRepayIncomeReq) {

        Long userId = alipayRepayIncomeReq.getUserId();
        Integer repaymentAmount = alipayRepayIncomeReq.getAmount();
        Long trdLoanOrderId = alipayRepayIncomeReq.getTrdLoanOrderId();
        Date repaymentDate = DateUtil.yyyyMMddHHmmssToDate(alipayRepayIncomeReq.getRepaymentTime());

        // 请求数据幂等
        idempotentService.idempotentCheck(IdempotentEventTypeEnum.ALIPAY_REPAY_INCOME, alipayRepayIncomeReq);
        PaymentRequest paymentRequestCheck = paymentRequestService.getPaymentRequestByRespOrderId(alipayRepayIncomeReq.getThirdOrderNo());
        if (paymentRequestCheck != null) {
            logger.info("流水号{}重复入账", alipayRepayIncomeReq.getThirdOrderNo());
            return;
        }

        // 人工入账的时候不允许有在支付的请求
        this.checkPaymentRequestExist(userId);

        // 1.根据ID数组 查询还款计划订单, 查询出所有需要的参数
        List<RepaymentPlan> allRepaymentPlanList = repaymentPlanService.getRepaymentOrderListByLoanOrderId(trdLoanOrderId, false);
        Integer totalWaitRepayAmount = this.getTotalWaitRepayAmount(allRepaymentPlanList);
        Integer refundAmount = repaymentAmount - totalWaitRepayAmount;
        boolean isRepayAll = this.isRepayAll(allRepaymentPlanList, repaymentAmount);
        List<RepaymentPlan> matchedRepaymentPlanList = repaymentPlanSpliter.splitByRepayAmount(repaymentAmount, allRepaymentPlanList);
        LoanOrder loanOrder = loanOrderMapper.selectByTrdOrderId(trdLoanOrderId);
        Products product = productsMapper.selectById(loanOrder.getProductId());

        // 3.创建支付请求, 创建还款记录, 都是成功装填的订单
        PaymentRequest paymentRequest = paymentRequestService.createPaymentRequestByCmsIncome(
                userId,
                repaymentAmount,
                PaymentRequestStatusEnum.SUCCESS,
                PaymentBizTypeEnum.APLIPAY,
                alipayRepayIncomeReq.getThirdOrderNo(),
                alipayRepayIncomeReq.getIncomeAccount(),
                repaymentDate
        );

        List<MqMessage> messageList = new ArrayList<>();
        for (int i = 0; i < matchedRepaymentPlanList.size(); i++) {
            RepaymentPlan updateRepaymentPlan = matchedRepaymentPlanList.get(i);
            // 4.创建还款记录
            RepaymentRecord repaymentRecord = repaymentRecordService.buildRepaymentRecordByIncomeCms(paymentRequest.getId(), updateRepaymentPlan, PaymentBizTypeEnum.APLIPAY);
//            if (refundAmount > 0 && i == matchedRepaymentPlanList.size()-1){
//                repaymentRecord.setRefundAmt(refundAmount);
//            }
            repaymentRecordService.addRepaymentRecord(repaymentRecord);

            // 5.还款计划入账, 判断是否结清
            RepaymentPlan existRepaymentPlan = repaymentPlanService.getRepaymentPlanById(updateRepaymentPlan.getId(), true);
            boolean isRepaymented = repaymentRecord.getAmount().intValue() == existRepaymentPlan.getRepaymentTotalAmount().intValue();
            //更新还款计划
            AlipayRepamentPlanDto alipayRepamentPlanDto = new AlipayRepamentPlanDto();
            alipayRepamentPlanDto.setId(updateRepaymentPlan.getId());
            alipayRepamentPlanDto.setAmount(repaymentRecord.getAmount());
            alipayRepamentPlanDto.setPrincipalAmount(repaymentRecord.getRepayPrincipalAmt());
            alipayRepamentPlanDto.setInterestAmount(repaymentRecord.getRepayInterestAmt());
            alipayRepamentPlanDto.setOverdueAmount(repaymentRecord.getRepayOverdueAmt());
            alipayRepamentPlanDto.setRepaymentTime(DateUtil.yyyyMMddHHmmss(repaymentDate));
            alipayRepamentPlanDto.setStatus(isRepaymented ? RepaymentPlanStatusEnum.Repaymented.getCode() : null);
            alipayRepamentPlanDto.setVersion(updateRepaymentPlan.getVersion());
            repaymentPlanService.updateRepaymentPlanAmount(alipayRepamentPlanDto);

            // 当前还款计划全额入账则关闭催收通知和overdue job
            if (updateRepaymentPlan.getIsOverdue()) {
                if (isRepayAll) {
                    //从逾期的任务列表中删除
                    scheduleTaskOverdueMapper.updateIsRepaymentedByOrderId(updateRepaymentPlan.getId(), true);
                }
                //通知催收
                CollectionNotifyDto collectionNotifyDto = collectionNotifyDtoBuilder.build(updateRepaymentPlan.getId(), true);
                MqMessage collectionNotifyCancelMessage = new MqMessage();
                collectionNotifyCancelMessage.setMessage(JSON.toJSONString(collectionNotifyDto));
                collectionNotifyCancelMessage.setQueueName(QueueConstants.COLLECTION_REPAYMENT_PLAN_OVERDUE_REPAY);
                messageList.add(collectionNotifyCancelMessage);
                logger.info("推送逾期订单还款到催收系统:" + collectionNotifyDto);
            }
        }

        if (refundAmount > 0){
            this.addRefundRecord(refundAmount, paymentRequest.getId(), matchedRepaymentPlanList.get(matchedRepaymentPlanList.size() - 1));
        }

        // 3.所有还款计划全部结清的情况下：处理外部的服务调用：用户额度恢复，修改trade-app订单状态判断
        if (isRepayAll) {

            //用户额度恢复
            MqMessage recoveryUserAmountMessage = this.getRecoveryUserAmountMessage(paymentRequest.getId().toString(), paymentRequest.getUserId(), loanOrder.getOrderAmount());
            messageList.add(recoveryUserAmountMessage);

            //同步trade-app订单状态
            loanOrderService.updateLoanOrderStatusToSettledByTradeAppId(loanOrder.getTrdLoanOrderId());
            MqMessage syncLoanOrderMessage = new MqMessage();
            SyncLoanOrderDto syncLoanOrderDto = new SyncLoanOrderDto();
            syncLoanOrderDto.setLoanOrderId(loanOrder.getTrdLoanOrderId());
            syncLoanOrderDto.setProductCategory(loanOrder.getProductCategory());
            syncLoanOrderDto.setStatus(TrdLoanOrderStatusEnum.SETTLED.getCode());
            syncLoanOrderMessage.setMessage(new Gson().toJson(syncLoanOrderDto));
            syncLoanOrderMessage.setQueueName(QueueConstants.CASHMANAPP_SYNC_TRD_ORDER_STAUTS_TO_TRADE);
            messageList.add(syncLoanOrderMessage);
        }

        //回调通知CMS
        AlipayRepaymentCallbackDto alipayRepaymentCallbackDto = new AlipayRepaymentCallbackDto();
        alipayRepaymentCallbackDto.setAlipayOrderNo(alipayRepayIncomeReq.getThirdOrderNo());
        alipayRepaymentCallbackDto.setLoanOrderNo(String.valueOf(alipayRepayIncomeReq.getTrdLoanOrderId()));
        alipayRepaymentCallbackDto.setType(1);
        alipayRepaymentCallbackDto.setStatus("1");
        alipayRepaymentCallbackDto.setRemark("支付宝还款入账成功");
        alipayRepaymentCallbackDto.setRepayAccountTime(DateUtil.yyyyMMddHHmmss(new Date()));
        MqMessage alipayRepaymentIncomeCallbackMessage = new MqMessage();
        alipayRepaymentIncomeCallbackMessage.setMessage(JSON.toJSONString(alipayRepaymentCallbackDto));
        alipayRepaymentIncomeCallbackMessage.setQueueName(QueueConstants.CMS_ALIPAY_REPAYMENT_CALLBACK_QUEUE);
        messageList.add(alipayRepaymentIncomeCallbackMessage);
        logger.info("支付宝还款入账成功回调通知CMS:" + alipayRepaymentIncomeCallbackMessage);

        mqMessageService.sendMessageList(messageList);

    }

    private void checkPaymentRequestExist(Long userId) {

        // 前置条件：有且只有1笔正在支付的请求
        PaymentRequest existPaymentRequest = paymentRequestService.getPaymentRequestByUserIdAndStatus(userId, PaymentRequestStatusEnum.NEW);
        if (!ObjectUtils.isEmpty(existPaymentRequest)) {
            throw new ServiceException("您当前有一笔正在定时代扣的订单，请稍后尝试还款");
//
//            String queryPaymentRequestStatus = payCenterRemoteService.queryPaymentRequestStatus(existPaymentRequest.toString(), PaymentCenterBizTypeEnum.BIG_AMT_USER_REPAY.getCode(), Constant.APPLICATION_PAYMENT_SOURCE);
//            //如果支付中心收到了请求，则不允许再进行还款的操作
//            if (!queryPaymentRequestStatus.equals(PayCenterRepaymentStatusEnum.REQUEST_UNFIND.getCode())) {
//                throw new ServiceException("您当前有一笔正在支付的订单，请稍后尝试还款");
//            }
        }
    }
    private boolean isHistoryOverdue(List<RepaymentPlan> allRepaymentPlanList) {
        //1.先判断历史是否有逾期的还款计划
        boolean isHistoryOverdue = false;
        for (Iterator<RepaymentPlan> iterator = allRepaymentPlanList.iterator(); iterator.hasNext(); ) {
            RepaymentPlan repaymentPlan = iterator.next();
            if (BooleanUtils.isFalse(repaymentPlan.getIsOverdue())) {
                continue;
            }
            isHistoryOverdue = true;
        }

        return isHistoryOverdue;
    }

    private boolean isRepayAll(List<RepaymentPlan> allRepaymentPlanList, List<RepaymentPlan> currentRepaymentPlanList) {
        int all = 0;
        for (Iterator<RepaymentPlan> iterator = allRepaymentPlanList.iterator(); iterator.hasNext(); ) {
            RepaymentPlan repaymentPlan = iterator.next();
            all += repaymentPlan.getRepaymentTotalAmount();
        }

        int currentRepay = 0;
        for (Iterator<RepaymentPlan> iterator = currentRepaymentPlanList.iterator(); iterator.hasNext(); ) {
            RepaymentPlan repaymentPlan = iterator.next();
            currentRepay += repaymentPlan.getRepaymentTotalAmount();
        }


        if (all == currentRepay) {
            return true;
        }

        return false;
    }

    private boolean isRepayAll(List<RepaymentPlan> allRepaymentPlanList, Integer repayAmount) {
        int all = 0;
        for (Iterator<RepaymentPlan> iterator = allRepaymentPlanList.iterator(); iterator.hasNext(); ) {
            RepaymentPlan repaymentPlan = iterator.next();
            all += repaymentPlan.getRepaymentTotalAmount();
        }

        if (!(repayAmount < all)) {
            return true;
        }

        return false;
    }

    private boolean isLoanRepayOneDay(LoanOrder loanOrder) {
        Date createdTime = loanOrder.getCreatedTime();
        Date loanCreateDate = DateUtil.dateFilter(createdTime);
        Date now = DateUtil.dateFilter(new Date());

        int i = DateUtil.daysBetween(loanCreateDate, now);
        if (i == 0) {
            return true;
        }

        return false;
    }


    @Override
    @Transactional
    public void repayCallback(PayCenterCallbackReq payCenterCallbackReq) {
        // 1. PaymentCallbackProcessParamDto是一个回调处理的汇总对象，里面包含了回调方法需要的各个流程的参数
        RepaymentConfirmProcessParam repaymentConfirmProcessParam = this.getPaymentCallbackProcessParam(payCenterCallbackReq);

        // 1.1 检查支付请求是否被处理过，如果已处理过，不做任何处理, 记录log日志
        PaymentRequest paymentRequest = repaymentConfirmProcessParam.getPaymentRequest();
        if (paymentRequest.getStatus() != PaymentRequestStatusEnum.NEW.getCode()) {
            logger.info("支付请求[{}]已经被处理过[{}]", paymentRequest.getId(), paymentRequest.getRespTime());
            return;
        }

        // 2.处理cashman-app内部的还款/代扣逻辑
        this.repaymentConfirmContext.process(repaymentConfirmProcessParam);

        // 只有支付请求成功的情况下才会走下面的流程: 恢复用户的可用额度, 判断并修改借款订单的状态
        if (!PaymentCenterCallbackRequestCode.SUCCESS.equals(payCenterCallbackReq.getCode())) {
            return;
        }

        // TODO 逾期状态的还款/借款订单，需要修改loan_order的状态

        // 3.处理外部的服务调用：用户额度恢复，修改trade-app订单状态判断
        LoanOrder loanOrder = repaymentConfirmProcessParam.getLoanOrder();

        // 3.1 调用用户系统恢复额度(注：恢复的额度是已还款的本金)
        List<MqMessage> messageList = new ArrayList<>();
        //MqMessage recoveryUserAmountMessage = this.getRecoveryUserAmountMessage(paymentRequest.getId().toString(), paymentRequest.getUserId(), repaymentConfirmProcessParam.getTotalPrincipalAmount());
        //messageList.add(recoveryUserAmountMessage);


        // 3.2 判断是否此借款单已完全还清，如果还清，则修改cashman-app、trade-app修改订单的状态为"已还清"
        boolean isLoanOrderRepayAll = this.isLoanOrderRepayAll(loanOrder.getTrdLoanOrderId());
        if (isLoanOrderRepayAll) {
            loanOrderService.updateLoanOrderStatusToSettledByTradeAppId(loanOrder.getTrdLoanOrderId());

            MqMessage syncLoanOrderMessage = new MqMessage();
            SyncLoanOrderDto syncLoanOrderDto = new SyncLoanOrderDto();
            syncLoanOrderDto.setLoanOrderId(loanOrder.getTrdLoanOrderId());
            syncLoanOrderDto.setProductCategory(loanOrder.getProductCategory());
            syncLoanOrderDto.setStatus(TrdLoanOrderStatusEnum.SETTLED.getCode());
            syncLoanOrderMessage.setMessage(new Gson().toJson(syncLoanOrderDto));
            syncLoanOrderMessage.setQueueName(QueueConstants.CASHMANAPP_SYNC_TRD_ORDER_STAUTS_TO_TRADE);
            messageList.add(syncLoanOrderMessage);

            //用户还清时恢复用户额度
            logger.info("用户全部还清,恢复用户额度为:{}，paymentRequestId为:{}",loanOrder.getOrderAmount(),paymentRequest.getId());
            MqMessage recoveryUserAmountMessage = this.getRecoveryUserAmountMessage(paymentRequest.getId().toString(), paymentRequest.getUserId(), loanOrder.getOrderAmount());
            messageList.add(recoveryUserAmountMessage);
        }

        // mq 多条信息只能以list形式发送
        mqMessageService.sendMessageList(messageList);
        //单边账处理不发送短信通知用户
        if(!paymentRequest.getPaymentType().equals(PaymentBizTypeEnum.BEFOREHAND_PAY.getCode())) {
            //发送短信通知还款结果
            try {
                sendNotice(isLoanOrderRepayAll, payCenterCallbackReq.getPayAmount(), loanOrder);
            } catch (Exception e) {
                logger.error("发送短信失败：", e);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void repayWithhold(CollectWithholdReq withholdReq) {
        /** 1.0  获取最新借款单【可能更新罚息、利息、本金、待还款总金额】此处更新的罚息事务还是单事务 */
        RepaymentPlan repaymentPlan = Optional.ofNullable(repaymentPlanService.getRepaymentPlanById(withholdReq.getRepaymentId(), true)).orElse(null);
        logger.info("催收代扣请求借款单编号:{}查询的最新借款单明细为:{}", withholdReq.getRepaymentId(), repaymentPlan);

        /** 2.0  业务验证 */
        validRepayWithhold(withholdReq, repaymentPlan);
        logger.info("催收代扣验证完成!!!");

        /* 3.0 对代扣金额进行瓜分 */
        Map<String, Integer> splitWithHoldAmount = repaymentPlanSpliter.splitWithHoldAmount(withholdReq.getMoney().intValue(), repaymentPlan);
        /* 代扣本金   */int repayPrincipalAmt = splitWithHoldAmount.get(RepaymentPlanSpliter.RECORD_REPAY_PRINCIPAL_AMT);
        /* 代扣利息   */int repayInterestAmt = splitWithHoldAmount.get(RepaymentPlanSpliter.RECORD_REPAY_INTEREST_AMT);
        /* 代扣滞纳金 */int repayOverdueAmt = splitWithHoldAmount.get(RepaymentPlanSpliter.RECORD_REPAY_OVERDUE_AMT);
        /* 代扣总金额 */int repayTotalAmt = repayPrincipalAmt + repayInterestAmt + repayOverdueAmt;
        logger.info("代扣金额瓜分如下: 代扣总金额:{}, 代扣本金:{}, 代扣利息:{}, 代扣滞纳金:{}", repayTotalAmt, repayPrincipalAmt, repayInterestAmt, repayOverdueAmt);

        /* 4.0 催收代扣请求落库*/
        CollectRequest request = withholdReq.buildWithhold();
        collectRequestMapper.insert(request);
        logger.info("催收代扣请求落库成功!!!请求id:{}", request.getId());

        /* 5.0  支付请求落库 */
        PaymentRequest paymentRequest = paymentRequestService.createPaymentRequest(repaymentPlan.getUserId(), repayTotalAmt, PaymentRequestStatusEnum.NEW, PaymentBizTypeEnum.COLLECT_WITHHOLD);
        logger.info("催收代扣支付请求落库完成!!!支付请求id:{}", paymentRequest.getId());

        /* 6.0  还款记录落库 */
        RepaymentRecord repaymentRecord = new RepaymentRecord(repaymentPlan.getUserId(),repaymentPlan.getId(),paymentRequest.getId(), PaymentRequestStatusEnum.NEW.getCode(), repayTotalAmt, PaymentBizTypeEnum.COLLECT_WITHHOLD.getText(), PaymentBizTypeEnum.COLLECT_WITHHOLD.getCode(),repaymentPlan.getLoanOrderId().toString(), repayPrincipalAmt, repayInterestAmt, repayOverdueAmt);
        repaymentRecordService.addRepaymentRecord(repaymentRecord);
        logger.info("催收代扣还款记录落库完成!!!还款记录id:{}", repaymentRecord.getId());

        /* 7.0  修改还款计划的在途金额(增加), 续期的不需要增加 */
        repaymentPlanService.increaseWaitingAmount(repaymentPlan.getId(), repayTotalAmt, repaymentRecord.getRepayPrincipalAmt(), repaymentRecord.getRepayInterestAmt(), repaymentRecord.getRepayOverdueAmt(), repaymentPlan.getVersion());
        repaymentPlan = Optional.ofNullable(repaymentPlanService.getRepaymentPlanById(withholdReq.getRepaymentId(), false)).orElse(null);
        logger.info("催收代扣在途金额修改成功!!!");

        /* 8.0  封装MQ消息并提交支付中心 */
        LoanOrder loanOrder = loanOrderMapper.selectByTrdOrderId(repaymentPlan.getLoanOrderId());
        logger.info("催收代扣根据借款单id:{}查询到trd_loan_order_id:{},biz_seq_no:{}", repaymentPlan.getLoanOrderId(), loanOrder.getTrdLoanOrderId(), loanOrder.getBizSeqNo());



        PaymentInstallmentDto paymentInstallmentDto = new PaymentInstallmentDto();
        // 这里的费用按照匹配到的repayment plan进行封装，
        paymentInstallmentDto.setPeriod(repaymentPlan.getPeriod());
        paymentInstallmentDto.setInstallmentAmount(repaymentRecord.getAmount());
        paymentInstallmentDto.setInstallmentPrincipal(repaymentRecord.getRepayPrincipalAmt());
        paymentInstallmentDto.setInstallInterest(repaymentRecord.getRepayInterestAmt());

        payCenterService.payWithMQ(repaymentPlan.getUserId().toString(), paymentRequest.getId(), repayTotalAmt, PaymentCenterBizTypeEnum.BIG_AMT_OVERDUE_WITHHOLD, JSON.toJSONString(request), repaymentPlan.getIsOverdue(), false, false, false, ImmutableList.of(paymentInstallmentDto), loanOrder, null);
        logger.info("催收代扣提交代扣还款订单到支付中心成功!!!还款计划id:{}, 代扣金额{}", repaymentPlan.getId(), repayTotalAmt);
    }

    private void validRepayWithhold(CollectWithholdReq withholdReq, RepaymentPlan repaymentPlan) {
        /** 1.0  验证催收代扣金额是否大于0 */
        if (withholdReq.getMoney().intValue() <= 0) {
            logger.error("催收代扣还款计划编号:【{}】代扣金额:【{}】不能小于等于0", withholdReq.getRepaymentId(), withholdReq.getMoney());
            throw CashmanExceptionBuilder.build(ErrorCodeConstants.COLLECT_WITHHOLD_AMOUNT_ERROR);
        }

        /** 1.1  验证还款计划是否存在或该还款计划正在被定时任务执行 */
        if (Objects.isNull(repaymentPlan) ||
                !repaymentPlan.getOperationFlag().equals(RepaymentPlanOperationFlagEnum.OVERDUE.getCode()) || repaymentPlan.getStatus() != RepaymentPlanStatusEnum.Waiting.getCode()) {
            logger.error("催收代扣还款计划不存在,或操作标识不等于【{}】或还款状态不等于【{}】", RepaymentPlanOperationFlagEnum.OVERDUE.getCode(), RepaymentPlanStatusEnum.Waiting.getCode());
            throw CashmanExceptionBuilder.build(ErrorCodeConstants.COLLECT_WITHHOLD_REPAYMENT_PLAN_STATUS_HOLDED);
        }

        /** 1.2  验证还款计划是否逾期 */
        if (!repaymentPlan.getIsOverdue()) {
            throw CashmanExceptionBuilder.build(ErrorCodeConstants.COLLECT_WITHHOLD_NOT_SUPPORT_REPAYMENT_PLAN_STATUS);
        }

        /** 1.3  验证还款计划是否存在未入账 */
        if (repaymentPlan.getRepaymentWaitingAmount().intValue() > 0) {
            throw CashmanExceptionBuilder.build(ErrorCodeConstants.COLLECT_WITHHOLD_NOT_SUPPORT_REPAYMENT_PLAN_WAITING_AMT);
        }

        /** 1.4  验证待还金额是否等于0或者代扣金额不等于待还总金额 */
//        if(repaymentPlan.getRepaymentTotalAmount() == 0 || withholdReq.getMoney().intValue()!=repaymentPlan.getRepaymentTotalAmount()){
//            throw CashmanExceptionBuilder.build(ErrorCodeConstants.COLLECT_WITHHOLD_REPAYMENT_PLAN_TOTAL_AMOUNT_ERROR);
//        }
        if (repaymentPlan.getRepaymentTotalAmount() == 0 || withholdReq.getMoney().intValue() > repaymentPlan.getRepaymentTotalAmount()) {
            throw CashmanExceptionBuilder.build(ErrorCodeConstants.COLLECT_WITHHOLD_REPAYMENT_PLAN_TOTAL_AMOUNT_ERROR);
        }
    }

    @Override
    @Transactional
    public void repayDeduct(CollectionDeductReq collectionDeductReq) {
        deductStrategyContext.deduct(collectionDeductReq);
    }

    @Override
    @Transactional
    public void repayCheckWithPayCenter(Long paymentRequestId) {
        String queryPaymentRequestStatus = payCenterRemoteService.queryPaymentRequestStatus(paymentRequestId.toString(), PaymentCenterBizTypeEnum.BIG_AMT_USER_REPAY.getCode(), Constant.APPLICATION_PAYMENT_SOURCE);
        //查询支付中心的支付订单状态是否能找到，找到的情况下会被明确的处理，如果未找到，则说明订单未提交
        //未提交的情况下需要对在途金额进行回滚
        if (queryPaymentRequestStatus.equals(PayCenterRepaymentStatusEnum.REQUEST_UNFIND.getCode())) {
            paymentRequestService.updatePaymentRequestStatusToFailure(paymentRequestId, "-1", "超时未支付", "", "");
            List<RepaymentRecord> repaymentRecords = repaymentRecordService.getRepaymentRecordsByPaymentRequestId(paymentRequestId);
            repaymentRecordService.updateRepaymentRecordToCancel(paymentRequestId, repaymentRecords);
            for (RepaymentRecord repaymentRecord : repaymentRecords) {
                RepaymentPlan repaymentPlan = repaymentPlanService.getRepaymentPlanByIdWithoutCheck(repaymentRecord.getRepaymentPlanId());
                repaymentPlanService.substractWaitingAmount(repaymentRecord.getRepaymentPlanId(), repaymentRecord.getAmount(), repaymentRecord.getRepayPrincipalAmt(), repaymentRecord.getRepayInterestAmt(), repaymentRecord.getRepayOverdueAmt(), repaymentPlan.getVersion());
            }
        }
    }

    @Override
    public void repayVerify(VerifyPaymentReq verifyPaymentReq) {
        PaymentRequest paymentRequest = paymentRequestService.getPaymentRequest(verifyPaymentReq.getBizId());

        if (ObjectUtils.isEmpty(paymentRequest)) {
            throw CashmanExceptionBuilder.build(ErrorCodeConstants.REPAY_VERIFY_ORDER_NOT_EXIST);
        }

        if (paymentRequest.getAmount().intValue() != verifyPaymentReq.getWithholdingAmount().intValue()) {
            throw CashmanExceptionBuilder.build(ErrorCodeConstants.REPAY_VERIFY_AMOUNT_ERROR);
        }

        if (paymentRequest.getUserId().intValue() != verifyPaymentReq.getUserId().intValue()) {
            throw CashmanExceptionBuilder.build(ErrorCodeConstants.REPAY_VERIFY_USER_NOT_MATCH);
        }

        if (BusTypeEnum.USER_REPAYMENT.getCode().equals(verifyPaymentReq.getBizType())) {
            throw CashmanExceptionBuilder.build(ErrorCodeConstants.REPAY_VERIFY_ORDER_TYPE_ERROR);
        }

        if (Constant.APPLICATION_PAYMENT_SOURCE.equals(verifyPaymentReq.getRequestSource())) {
            throw CashmanExceptionBuilder.build(ErrorCodeConstants.REPAY_VERIFY_ORDER_SOURCE_ERROR);
        }
    }

    /**
     * 查询还款的汇总对象
     *
     * @param payCenterCallbackReq 支付中心的回调请求
     * @return
     */
    private RepaymentConfirmProcessParam getPaymentCallbackProcessParam(PayCenterCallbackReq payCenterCallbackReq) {
        Long paymentRequestId = new Long(payCenterCallbackReq.getOrderDetailId());

        RepaymentConfirmProcessParam dto = new RepaymentConfirmProcessParam();
        dto.setPayCenterCallbackReq(payCenterCallbackReq);

        PaymentRequest paymentRequest = paymentRequestService.getPaymentRequest(paymentRequestId);
        // 订单没找到，数据不一致问题，return error
        if (ObjectUtils.isEmpty(paymentRequest)) {
            throw new ServiceException("支付请求未找到");
        }

        BigDecimal totalPrincipalAmount = BigDecimal.ZERO;
        List<RepaymentRecord> repaymentRecords = repaymentRecordService.getRepaymentRecordsByPaymentRequestId(paymentRequestId);
        logger.info("查询还款记录RepaymentRecord List：{}", JSON.toJSONString(repaymentRecords));
        List<RepaymentPlan> matchedRepaymentPlans = new ArrayList<>();
        List<RepaymentPlan> allRepaymentPlans = repaymentPlanService.getAllRepaymentPlansBySingleId(repaymentRecords.get(0).getRepaymentPlanId());
        logger.info("查询借款订单的还款计划：Loan Order Id :{}, Repayment Plan List{}", repaymentRecords.get(0).getRepaymentPlanId(), JSON.toJSONString(allRepaymentPlans));
        LoanOrder loanOrder = loanOrderMapper.selectByTrdOrderId(allRepaymentPlans.get(0).getLoanOrderId());
        logger.info("查询借款订单：{}", JSON.toJSONString(loanOrder));
        Map<Long, Products> productsMap = new HashMap<>();
        for (Iterator<RepaymentRecord> iterator = repaymentRecords.iterator(); iterator.hasNext(); ) {
            RepaymentRecord repaymentRecord = iterator.next();
            RepaymentPlan repaymentPlan = repaymentPlanService.getRepaymentPlanByIdWithoutCheck(repaymentRecord.getRepaymentPlanId());
            totalPrincipalAmount = totalPrincipalAmount.add(new BigDecimal(repaymentRecord.getRepayPrincipalAmt()));
            matchedRepaymentPlans.add(repaymentPlan);
            Long productId = repaymentPlan.getProductId();
            Products product = productsMapper.selectById(productId);
            productsMap.put(productId, product);
            dto.setProductCategory(product.getProductCategory().toString());
        }

        dto.setTotalPrincipalAmount(totalPrincipalAmount.intValue());
        dto.setLoanOrder(loanOrder);
        dto.setPaymentRequest(paymentRequest);
        dto.setRepaymentRecordList(repaymentRecords);
        dto.setMatchedRepaymentPlans(matchedRepaymentPlans);
        dto.setAllRepaymentPlans(allRepaymentPlans);
        dto.setProductsMap(productsMap);

        logger.info("repay confirm dto:{}", JSON.toJSONString(dto));

        return dto;
    }

    //是否有非最终态的订单--
    @Override
    public Boolean hasNonUltimateOrder(Long userId) {
        logger.info("countByUserIdAndStatus 方法之前 {}", System.currentTimeMillis());
        int count = repaymentPlanService.countByUserIdAndStatus(userId, false);
        if (count > 0) {
            return true;
        }
        return false;
    }

    @Override
    public List<IndexRepaymentPlanDto> getRepaymentOrder(Long loanOrderId) throws ParseException {
        List<RepaymentPlan> repaymentPlans = repaymentPlanService.getRepaymentOrderListByLoanOrderId(loanOrderId);
        if (repaymentPlans == null || repaymentPlans.isEmpty()) {
            return null;
        }
        List<IndexRepaymentPlanDto> dto = new ArrayList<>(repaymentPlans.size());
        for (RepaymentPlan repaymentPlan : repaymentPlans) {
            dto.add(new IndexRepaymentPlanDto(repaymentPlan.getLoanOrderId(),
                    MoneyUtil.changeCentToYuan(repaymentPlan.getRepaymentTotalAmount()),
                    repaymentPlan.getRepaymentPlanTime(), repaymentPlan.getStatus(),
                    repaymentPlan.getIsOverdue(), repaymentPlan.getOverdueDayCount()));
        }
        return dto;
    }

    private boolean isLoanOrderRepayAll(Long loanOrderId) {
        boolean isRepayAll = true;
        //这里对数据进行重新查询，如果借款单下的所有还款计划都是Repaymented状态，则借款单是"已还清"的状态
        List<RepaymentPlan> allRepaymentPlans = repaymentPlanService.getRepaymentOrderListByLoanOrderId(loanOrderId);
        for (Iterator<RepaymentPlan> iterator = allRepaymentPlans.iterator(); iterator.hasNext(); ) {
            RepaymentPlan repaymentPlan = iterator.next();
            if (repaymentPlan.getStatus().intValue() != RepaymentPlanStatusEnum.Repaymented.getCode()) {
                logger.info("验证借款订单{}是否全部还清：{}, 还款计划{}还未还清", loanOrderId, isRepayAll, repaymentPlan.getId());
                isRepayAll = false;
                break;
            }
        }
        logger.info("验证借款订单{}是否全部还清：{}", loanOrderId, isRepayAll);
        return isRepayAll;
    }

    private MqMessage getUpdateLoanOrderStatusMessage(Long loanOrderId) {
        // 还清的情况下发送队列修改trade-app loan order的状态
        LoanOrderRepayedDto loanOrderRepayedDto = new LoanOrderRepayedDto();
        loanOrderRepayedDto.setLoanOrderId(loanOrderId);
        String messageBodyJsonStr = JSON.toJSONString(loanOrderRepayedDto);
        logger.info("支付回调：发送消息给trade-app，修改借款单的状态为已还款：{}", messageBodyJsonStr);
        return new MqMessage(messageBodyJsonStr, QueueConstants.LOAN_ORDER_REPAYMENTED_MQNAME);
    }

    private MqMessage getRecoveryUserAmountMessage(String seqNo, Long userId, Integer repayPrincipalAmount) {
        QuotaGiveBackDto quotaGiveBackDto = new QuotaGiveBackDto();
        quotaGiveBackDto.setUserId(userId);
        quotaGiveBackDto.setSeqNo(seqNo);
        quotaGiveBackDto.setMoneyAmount(repayPrincipalAmount.intValue());
        quotaGiveBackDto.setProductCategory(ProductCategoryEnum.PRODUCT_CATEGORY_BIG.getCode());

        logger.info("支付回调：发送消息给cashman，恢复用户额度：{}", quotaGiveBackDto.toString());
        return new MqMessage(JSON.toJSONString(quotaGiveBackDto), QueueConstants.USER_AVAILABLE_AMOUNT_MQ_NAME);
    }

    /**
     * 订单还款成功后 即时发送还款成功短信
     *
     * @param isLoanOrderRepayAll
     * @param payAmount--本次还款金额
     * @param loanOrder
     */
    public void sendNotice(Boolean isLoanOrderRepayAll, Long payAmount, LoanOrder loanOrder) {
        Boolean isSendSuccess = false;
        String payAmountStr = MoneyUtil.changeCentToYuan(payAmount.intValue()).toString();
        if (isLoanOrderRepayAll) {
            logger.info("本次还款金额为" + payAmountStr + "元,全部还清！");
            //快借钱包的短信模板--目前和大额分期不一致
            if (null != loanOrder.getMerchantNo() && (MerchantNoEnum.MERCHANT_KJQB.getMerchantNo().equals(loanOrder.getMerchantNo()))) {
                logger.info("快借钱包发送全额还款成功短信，商户号为：{}", loanOrder.getMerchantNo());
                isSendSuccess = smsService.sendSms(new SmsDto(loanOrder.getBizSeqNo(), loanOrder.getUserPhone(), new StringBuilder(MerchantNoEnum.MERCHANT_KJQB.getMerchantNoticePrex()).append(Constant.SMS_LOAN_SUCCESS_ALL_ID).toString(), "", null == loanOrder.getMerchantNo() ? "" : loanOrder.getMerchantNo()));
            } else {
                logger.info("非快借钱包发送放款成功短信");
                isSendSuccess = smsService.sendSms(new SmsDto(loanOrder.getBizSeqNo(), loanOrder.getUserPhone(), Constant.SMS_LOAN_SUCCESS_ALL_ID, "", null == loanOrder.getMerchantNo() ? "" : loanOrder.getMerchantNo()));
            }

        } else {
            //（非已还清的订单状态）获取待还还款计划--得到总计待还金额
            String[] loanStatus = new String[]{TrdLoanOrderStatusEnum.LOAN_SUCCESS.getCode(), TrdLoanOrderStatusEnum.OVERDUE.getCode()};
            String[] repaymentStatus = new String[]{String.valueOf(RepaymentPlanStatusEnum.Waiting.getCode()), String.valueOf(RepaymentPlanStatusEnum.Part.getCode())};
            List<RepaymentNoticeDto> repaymentNoticeDtos = repaymentPlanMapper.getPartRepaymentNotice(loanOrder.getTrdLoanOrderId(), loanStatus, repaymentStatus);
            if (null == repaymentNoticeDtos) {
                logger.info("未查到部分还款订单的待还款计划，业务号为：" + loanOrder.getBizSeqNo());
            } else {
                //总计剩余应还
                Integer repaymentAmount = 0;
                //逾期金额
                Integer overDuerepayment = 0;
                for (RepaymentNoticeDto repaymentNoticeDto : repaymentNoticeDtos) {
                    if (1 == repaymentNoticeDto.getIsOverdue()) {
                        overDuerepayment += repaymentNoticeDto.getRepaymentTotalAmount();
                    }
                    repaymentAmount += repaymentNoticeDto.getRepaymentTotalAmount();
                }
                //-本地已还金额**元，已逾期待还金额**元，剩余应还金额**元
                logger.info("本次还款金额为" + payAmountStr + "元,部分还清，已逾期待还金额为" + MoneyUtil.changeCentToYuan(overDuerepayment).toString() + "元，剩余应还金额" + MoneyUtil.changeCentToYuan(repaymentAmount).toString() + "元！");
                //快借钱包的短信模板--目前和大额分期不一致
                if (null != loanOrder.getMerchantNo() && (MerchantNoEnum.MERCHANT_KJQB.getMerchantNo().equals(loanOrder.getMerchantNo()))) {
                    logger.info("快借钱包发送部分还款成功短信，商户号为：{}", loanOrder.getMerchantNo());
                    isSendSuccess = smsService.sendSms(new SmsDto(loanOrder.getBizSeqNo(), loanOrder.getUserPhone(), new StringBuilder(MerchantNoEnum.MERCHANT_KJQB.getMerchantNoticePrex()).append(Constant.SMS_PAYMENT_SUCCESS_PART_ID).toString(), new StringBuffer(payAmountStr).append(",").append(MoneyUtil.changeCentToYuan(repaymentAmount).toString()).toString(), null == loanOrder.getMerchantNo() ? "" : loanOrder.getMerchantNo()));
                } else {
                    logger.info("非快借钱包发送部分还款成功短信");
                    isSendSuccess = smsService.sendSms(new SmsDto(loanOrder.getBizSeqNo(), loanOrder.getUserPhone(), Constant.SMS_PAYMENT_SUCCESS_PART_ID, new StringBuffer(payAmountStr).append(",").append(MoneyUtil.changeCentToYuan(repaymentAmount).toString()).toString(), null == loanOrder.getMerchantNo() ? "" : loanOrder.getMerchantNo()));
                }
            }
        }
        if (!isSendSuccess) {
            logger.info("短信发送失败，订单业务号为：" + loanOrder.getBizSeqNo());
        }
    }

    /**
     *
     *  alipay manual income
     *
     * 真命题：入账时间(当前时间) >= 还款时间  [dayDiff >= 0]（先还款才能入账）
     *
     * #1.入账时间 = 还款计划 >= 还款时间  [dayDiff >= 0，currentDayDiff == 0，repaymentDayDiff >= 0]
     *       全额还款：未逾期，正常计算（本金，利息）
     *       部分还款：未逾期，正常计算（本金，利息）
     *
     * #2.还款计划 > 入账时间 >= 还款时间  [dayDiff >= 0，currentDayDiff > 0，repaymentDayDiff > 0]
     *        全额还款：未逾期，正常计算（本金，利息）
     *        部分还款：未逾期，正常计算（本金，利息）
     *
     * #3.入账时间 > 还款计划 >= 还款时间  [dayDiff > 0，currentDayDiff < 0，repaymentDayDiff >= 0]
     *        全额还款：未逾期，通知催收关闭，清零滞纳金
     *        部分还款：未还清，逾期天数保持，根据还款金额依次扣除本金，利息，滞纳金
     *
     * #4.入账时间 > 还款时间 > 还款计划  [dayDiff > 0，currentDayDiff < 0，repaymentDayDiff < 0]
     *        全额还款：已逾期，已还清，通知催收关闭。滞纳金根据实际还款时间的逾期天数计算。
     *        部分还款：未还清，逾期天数保持，根据还款金额依次扣除本金，利息，滞纳金。
     * #5.入账时间 = 还款时间 > 还款计划  [dayDiff == 0，currentDayDiff < 0，repaymentDayDiff < 0]
     *        全额还款：逾期已还清，逾期天数保持，正常计算，通知催收关闭。
     *        部分还款：逾期未还清，逾期天数保持，正常计算。
     *
     * @param alipayRepayIncomeReq
     */
    @Override
    @Transactional
    public void repayIncomeByAlipayManual(AlipayRepayIncomeReq alipayRepayIncomeReq) {

        // 请求数据幂等
        idempotentService.idempotentCheck(IdempotentEventTypeEnum.ALIPAY_REPAY_INCOME, alipayRepayIncomeReq);
        PaymentRequest paymentRequestCheck = paymentRequestService.getPaymentRequestByRespOrderId(alipayRepayIncomeReq.getThirdOrderNo());
        if (paymentRequestCheck != null) {
            logger.info("流水号{}重复入账", alipayRepayIncomeReq.getThirdOrderNo());
            return;
        }

        Integer repaymentAmount = alipayRepayIncomeReq.getAmount();
        Long trdLoanOrderId = alipayRepayIncomeReq.getTrdLoanOrderId();
        Date repaymentDate = DateUtil.yyyyMMddHHmmssToDate(alipayRepayIncomeReq.getRepaymentTime());
        String repaymentDateStr = DateUtil.yyyyMMdd(repaymentDate);//还款时间
        String currentDateStr = DateUtil.yyyyMMdd(new Date());//当前时间

        //当还款金额剩余0的时候，跳出执行
        if (repaymentAmount.intValue() == 0 || repaymentAmount.intValue() < 0) {
            throw new ServiceException("还款金额错误，必须大于0，trdLoanOrderId= " + trdLoanOrderId + "amount= " + repaymentAmount);
        }

        LoanOrder loanOrder = loanOrderMapper.selectByTrdOrderId(trdLoanOrderId);

        if (loanOrder == null) {
            throw new ServiceException("未获取到该订单号对应的订单，trdLoanOrderId = " + trdLoanOrderId);
        }

        Long userId = loanOrder.getUserId();
        // 1.人工入账的时候不允许有在支付的请求
        this.checkPaymentRequestExist(userId);

        logger.info("匹配到的订单号，loanOrderId= {}, userId= {}", trdLoanOrderId, userId);


        // 2.根据ID数组 查询还款计划订单, 查询出所有需要的参数
        List<RepaymentPlan> allRepaymentPlanList = repaymentPlanService.getRepaymentOrderListByLoanOrderId(trdLoanOrderId, false);

        if (allRepaymentPlanList == null || allRepaymentPlanList.size() == 0) {
            throw new ServiceException("未获取到该订单号对应的还款计划信息，trdLoanOrderId = " + trdLoanOrderId);
        }

        int dayDiff = DateUtil.daysBetween(DateUtil.yyyyMMdd2Date(repaymentDateStr), DateUtil.yyyyMMdd2Date(currentDateStr));
        if (dayDiff < 0) {
            throw new ServiceException("还款时间参数异常，还款时间不能在当前入账时间之后，需先还款才能入账，repaymentDate = " + repaymentDateStr);
        }

        // 3.创建支付请求, 都是成功装填的订单
        PaymentRequest paymentRequest = paymentRequestService.createPaymentRequestByCmsIncome(
                userId,
                repaymentAmount,
                PaymentRequestStatusEnum.SUCCESS,
                PaymentBizTypeEnum.APLIPAY,
                alipayRepayIncomeReq.getThirdOrderNo(),
                alipayRepayIncomeReq.getIncomeAccount(),
                repaymentDate
        );

        List<MqMessage> messageList = new ArrayList<>();

        //根据借款期数升序排序
        List<RepaymentPlan> sortRepaymentPlanList = this.sortedByPeriodAsc(allRepaymentPlanList);

        int hasRepayedCount = 0;

        // 4.拆分订单分期还款计划，判断部分还款
        for (Iterator<RepaymentPlan> iterator = sortRepaymentPlanList.iterator(); iterator.hasNext(); ) {

            RepaymentPlan repaymentPlan = iterator.next();

            String repaymentPlanDateStr = DateUtil.yyyyMMdd(repaymentPlan.getRepaymentPlanTime());
            int repaymentDayDiff = DateUtil.daysBetween(DateUtil.yyyyMMdd2Date(repaymentDateStr), DateUtil.yyyyMMdd2Date(repaymentPlanDateStr));
            int currentDayDiff = DateUtil.daysBetween(DateUtil.yyyyMMdd2Date(currentDateStr), DateUtil.yyyyMMdd2Date(repaymentPlanDateStr));
            Boolean isRepaymented = false;

            logger.info("还款时间：{}, 入账时间：{}, 还款计划时间：{}, repaymentDayDiff= {}, currentDayDiff= {}, dayDiff= {}, repaymentAmount= {}", repaymentDateStr, currentDateStr, repaymentPlanDateStr, repaymentDayDiff, currentDayDiff, dayDiff, repaymentAmount);

            CountRepaymentPlanDto countRepaymentPlanDto;

            if (dayDiff >= 0 && currentDayDiff == 0 && repaymentDayDiff >= 0 && repaymentAmount.intValue() > 0) {
                logger.info("#1, 入账时间 = 还款计划 >= 还款时间,未逾期还款，未通知催收（正常计算还款）");
                countRepaymentPlanDto = countRepaymentPlan(paymentRequest.getId(), repaymentPlan, isRepaymented, repaymentAmount, repaymentDate, false, 0, ManualIncomOverdueStatusEnum.CONDITIION_1.getType());
                hasRepayedCount = countRepaymentPlanDto.isRepaymented() ? (hasRepayedCount + 1) : hasRepayedCount;
                repaymentAmount = countRepaymentPlanDto.getRepaymentAmount();
                logger.info("countRepaymentPlanDto：{}", JSON.toJSONString(countRepaymentPlanDto));
            } else if (dayDiff >= 0 && currentDayDiff > 0 && repaymentDayDiff > 0 && repaymentAmount.intValue() > 0) {
                logger.info("#2,还款计划 > 入账时间 >= 还款时间,未逾期还款，未通知催收（正常计算还款）");
                countRepaymentPlanDto = countRepaymentPlan(paymentRequest.getId(), repaymentPlan, isRepaymented, repaymentAmount, repaymentDate, false, 0, ManualIncomOverdueStatusEnum.CONDITIION_1.getType());
                hasRepayedCount = countRepaymentPlanDto.isRepaymented() ? (hasRepayedCount + 1) : hasRepayedCount;
                repaymentAmount = countRepaymentPlanDto.getRepaymentAmount();
                logger.info("countRepaymentPlanDto：{}", JSON.toJSONString(countRepaymentPlanDto));
            } else if (dayDiff > 0 && currentDayDiff < 0 && repaymentDayDiff >= 0 && repaymentAmount.intValue() > 0) {
                logger.info("#3,入账时间 > 还款计划 >= 还款时间");
                countRepaymentPlanDto = countRepaymentPlan(paymentRequest.getId(), repaymentPlan, isRepaymented, repaymentAmount, repaymentDate, false, 0, ManualIncomOverdueStatusEnum.CONDITIION_2.getType());
                hasRepayedCount = countRepaymentPlanDto.isRepaymented() ? (hasRepayedCount + 1) : hasRepayedCount;
                repaymentAmount = countRepaymentPlanDto.getRepaymentAmount();
                int type = countRepaymentPlanDto.isRepaymented() ? NotifyCollectionTypeEnum.CANCEL.getCode() : NotifyCollectionTypeEnum.NOTIFY.getCode();
                notifyCollection(repaymentPlan.getId(), type, messageList);
                logger.info("countRepaymentPlanDto：{}", JSON.toJSONString(countRepaymentPlanDto));
            } else if (dayDiff > 0 && currentDayDiff < 0 && repaymentDayDiff < 0 && repaymentAmount.intValue() > 0) {
                logger.info("#4,入账时间 > 还款时间 > 还款计划");
                countRepaymentPlanDto = countRepaymentPlan(paymentRequest.getId(), repaymentPlan, isRepaymented, repaymentAmount, repaymentDate, true, Math.abs(repaymentDayDiff), ManualIncomOverdueStatusEnum.CONDITIION_3.getType());
                hasRepayedCount = countRepaymentPlanDto.isRepaymented() ? (hasRepayedCount + 1) : hasRepayedCount;
                repaymentAmount = countRepaymentPlanDto.getRepaymentAmount();
                int type = countRepaymentPlanDto.isRepaymented() ? NotifyCollectionTypeEnum.CANCEL.getCode() : NotifyCollectionTypeEnum.NOTIFY.getCode();
                notifyCollection(repaymentPlan.getId(), type, messageList);
                logger.info("countRepaymentPlanDto：{}", JSON.toJSONString(countRepaymentPlanDto));
            } else if (dayDiff == 0 && currentDayDiff < 0 && repaymentDayDiff < 0 && repaymentAmount.intValue() > 0) {
                logger.info("#5,入账时间 = 还款时间 > 还款计划");
                countRepaymentPlanDto = countRepaymentPlan(paymentRequest.getId(), repaymentPlan, isRepaymented, repaymentAmount, repaymentDate, true, Math.abs(repaymentDayDiff), ManualIncomOverdueStatusEnum.CONDITIION_4.getType());
                hasRepayedCount = countRepaymentPlanDto.isRepaymented() ? (hasRepayedCount + 1) : hasRepayedCount;
                repaymentAmount = countRepaymentPlanDto.getRepaymentAmount();
                int type = countRepaymentPlanDto.isRepaymented() ? NotifyCollectionTypeEnum.CANCEL.getCode() : NotifyCollectionTypeEnum.NOTIFY.getCode();
                notifyCollection(repaymentPlan.getId(), type, messageList);
                logger.info("countRepaymentPlanDto：{}", JSON.toJSONString(countRepaymentPlanDto));
            }
        }

        if (repaymentAmount > 0){
            this.addRefundRecord(repaymentAmount, paymentRequest.getId(), sortRepaymentPlanList.get(sortRepaymentPlanList.size() - 1));
        }


        // 5.所有还款计划全部结清的情况下：处理外部的服务调用：用户额度恢复，修改trade-app订单状态判断
        if (hasRepayedCount == sortRepaymentPlanList.size()) {
            //用户额度恢复
            MqMessage recoveryUserAmountMessage = this.getRecoveryUserAmountMessage(paymentRequest.getId().toString(), paymentRequest.getUserId(), loanOrder.getOrderAmount());
            messageList.add(recoveryUserAmountMessage);
            //同步trade-app订单状态已结清(50)
            loanOrderService.updateLoanOrderStatusToSettledByTradeAppId(loanOrder.getTrdLoanOrderId());
            MqMessage syncLoanOrderMessage = new MqMessage();
            SyncLoanOrderDto syncLoanOrderDto = new SyncLoanOrderDto();
            syncLoanOrderDto.setLoanOrderId(loanOrder.getTrdLoanOrderId());
            syncLoanOrderDto.setStatus(TrdLoanOrderStatusEnum.SETTLED.getCode());
            syncLoanOrderDto.setProductCategory(loanOrder.getProductCategory());
            syncLoanOrderMessage.setMessage(new Gson().toJson(syncLoanOrderDto));
            syncLoanOrderMessage.setQueueName(QueueConstants.CASHMANAPP_SYNC_TRD_ORDER_STAUTS_TO_TRADE);
            messageList.add(syncLoanOrderMessage);
        } else {
            //6. 未还清，还款计划都为未逾期，同步trade-app订单状态为21
            List<RepaymentPlan> newAllRepaymentPlanList = repaymentPlanService.getRepaymentOrderListByLoanOrderId(trdLoanOrderId, false);
            if (newAllRepaymentPlanList != null && newAllRepaymentPlanList.size() > 0) {
                Boolean hasOverduePlan = false;
                for (RepaymentPlan repaymentPlan : newAllRepaymentPlanList) {
                    if (repaymentPlan.getIsOverdue()) {
                        //有逾期订单
                        hasOverduePlan = true;
                        break;
                    }
                }
                logger.info("同步订单状态status= 21");
                if (!hasOverduePlan) {
                    loanOrderService.updateLoanOrderStatusByTrdLoanOrderId(loanOrder.getTrdLoanOrderId(), TrdLoanOrderStatusEnum.LOAN_SUCCESS.getCode());
                    MqMessage syncLoanOrderMessage = new MqMessage();
                    SyncLoanOrderDto syncLoanOrderDto = new SyncLoanOrderDto();
                    syncLoanOrderDto.setLoanOrderId(loanOrder.getTrdLoanOrderId());
                    syncLoanOrderDto.setStatus(TrdLoanOrderStatusEnum.LOAN_SUCCESS.getCode());
                    syncLoanOrderDto.setProductCategory(loanOrder.getProductCategory());
                    syncLoanOrderMessage.setMessage(new Gson().toJson(syncLoanOrderDto));
                    syncLoanOrderMessage.setQueueName(QueueConstants.CASHMANAPP_SYNC_TRD_ORDER_STAUTS_TO_TRADE);
                    messageList.add(syncLoanOrderMessage);
                }
            }
        }

        if (messageList.size() > 0) {
            mqMessageService.sendMessageList(messageList);
        }
    }

    private void addRefundRecord(Integer refundAmt, Long paymentRequestId, RepaymentPlan repaymentPlan){
        //创建长款还款记录
        RepaymentRecord repaymentRecord = new RepaymentRecord();
        repaymentRecord.setUserId(repaymentPlan.getUserId());
        repaymentRecord.setRepaymentPlanId(repaymentPlan.getId());
        repaymentRecord.setPaymentRequestId(paymentRequestId);
        repaymentRecord.setStatus(PaymentRequestStatusEnum.SUCCESS.getCode());
        repaymentRecord.setPaymentType(PaymentOrderTypeEnum.ALIPAY.getCode());
        Date now = new Date();
        repaymentRecord.setCreatedAt(now);
        repaymentRecord.setUpdatedAt(now);
        repaymentRecord.setLoanOrderId(repaymentPlan.getLoanOrderId().toString());
        repaymentRecord.setRemark("支付宝人工入账");
        repaymentRecord.setAmount(refundAmt);
        repaymentRecord.setRepayPrincipalAmt(0);
        repaymentRecord.setRepayInterestAmt(0);
        repaymentRecord.setRepayOverdueAmt(0);
        repaymentRecord.setRefundAmt(refundAmt);
        logger.info("创建长款记录:{}", JSON.toJSONString(repaymentRecord));
        repaymentRecordService.addRepaymentRecord(repaymentRecord);
    }

    private void notifyCollection(Long repaymentPlanId, int type, List<MqMessage> messageList) {
        CollectionNotifyDto collectionNotifyDto = collectionNotifyDtoBuilder.build(repaymentPlanId, false);
        MqMessage collectionNotifyCancelMessage = new MqMessage();
        collectionNotifyCancelMessage.setMessage(JSON.toJSONString(collectionNotifyDto));
        if (type == NotifyCollectionTypeEnum.CANCEL.getCode()) {
            // 从逾期的任务列表中删除
            scheduleTaskOverdueMapper.updateIsRepaymentedByOrderId(repaymentPlanId, true);
        }
        collectionNotifyCancelMessage.setQueueName(QueueConstants.COLLECTION_REPAYMENT_PLAN_OVERDUE_REPAY);
        messageList.add(collectionNotifyCancelMessage);
        logger.info("推送逾期订单还款到催收系统:" + JSON.toJSONString(collectionNotifyDto));
    }

    private CountRepaymentPlanDto countRepaymentPlan(Long paymentRequestId, RepaymentPlan repaymentPlan, Boolean isRepaymented, Integer repaymentAmount, Date repaymentDate, boolean isOverdue, int overdueDayCount, int conditionType) {

        CountRepaymentPlanDto countRepaymentPlanDto = new CountRepaymentPlanDto();
        if (RepaymentPlanStatusEnum.Repaymented.getCode() == repaymentPlan.getStatus()) {
            countRepaymentPlanDto.setRepaymentAmount(repaymentAmount);
            countRepaymentPlanDto.setRepaymented(true);
            return countRepaymentPlanDto;
        }

        Integer remainingRepaymentPrincipalAmount = 0;//剩余应还本金
        Integer remainingRepaymentInterestAmount = 0;//剩余应还利息
        Integer remainingOverdueFeeAmount = 0;//剩余应还滞纳金

        Integer hasRepayedPrincipalAmt = 0;//已还本金
        Integer hasRepayInterestAmt = 0;//已还利息
        Integer hasRepayOverdueAmt = 0;//已还滞纳金
        Boolean updateOverdueStatus = false;

        //未逾期，正常计算（本金，利息）
        if (conditionType == 1) {
            if (repaymentAmount.intValue() >= repaymentPlan.getRepaymentTotalAmount().intValue()) {
                repaymentAmount = repaymentAmount - repaymentPlan.getRepaymentTotalAmount().intValue();
                hasRepayedPrincipalAmt = repaymentPlan.getRepaymentPrincipalAmount();
                hasRepayInterestAmt = repaymentPlan.getRepaymentInterestAmount();
                hasRepayOverdueAmt = repaymentPlan.getOverdueFeeAmount();
                isRepaymented = true;
                logger.info("拆分还款订单：[该期是Condition1的全额还款]:{}", JSON.toJSONString(repaymentPlan));
            }
        } else if (conditionType == 2) {
            int repaymentTotalAmount = repaymentPlan.getRepaymentPrincipalAmount() + repaymentPlan.getRepaymentInterestAmount();
            if (repaymentAmount.intValue() >= repaymentTotalAmount) {
                repaymentAmount = repaymentAmount - repaymentTotalAmount;
                hasRepayedPrincipalAmt = repaymentPlan.getRepaymentPrincipalAmount();
                hasRepayInterestAmt = repaymentPlan.getRepaymentInterestAmount();
                hasRepayOverdueAmt = 0;
                updateOverdueStatus = true;
                isRepaymented = true;
                logger.info("拆分还款订单：[该期是Condition2的全额还款]:{}", JSON.toJSONString(repaymentPlan));
            }
        } else if (conditionType == 3) {
            //剩余应还(本金+利息)
            Integer remainingRepaymentOriginAmount = repaymentPlan.getRepaymentPrincipalAmount() + repaymentPlan.getRepaymentInterestAmount();
            //还款时间所对应的应还罚息
            Integer realOverdueFeeAmount = calculateOverdueFeeAmountByDay(repaymentPlan.getRepaymentOriginPrincipalAmount(), overdueDayCount);
            if (repaymentAmount.intValue() > remainingRepaymentOriginAmount.intValue()) {
                if (remainingRepaymentOriginAmount == 0) {
                    //已还总罚息
                    Integer hasRepayedOverdueFeeAmount = repaymentPlan.getRepaymentIncomeAmount() - (repaymentPlan.getRepaymentOriginPrincipalAmount() + repaymentPlan.getRepaymentOriginInterestAmount());
                    Integer subtractionOverdueFeeAmount = realOverdueFeeAmount - hasRepayedOverdueFeeAmount;
                    //已还清
                    if (repaymentAmount.intValue() >= subtractionOverdueFeeAmount.intValue()) {
                        repaymentAmount = repaymentAmount - subtractionOverdueFeeAmount.intValue();
                        hasRepayedPrincipalAmt = repaymentPlan.getRepaymentPrincipalAmount();
                        hasRepayInterestAmt = repaymentPlan.getRepaymentInterestAmount();
                        hasRepayOverdueAmt = subtractionOverdueFeeAmount;
                        updateOverdueStatus = true;
                        isRepaymented = true;
                        logger.info("拆分还款订单：[该期是Condition3的第1种全额还款]:{}", JSON.toJSONString(repaymentPlan));
                    }
                } else {
                    Integer realOriginTotalAmount = remainingRepaymentOriginAmount + realOverdueFeeAmount;
                    //已还清
                    if (repaymentAmount.intValue() >= realOriginTotalAmount) {
                        repaymentAmount = repaymentAmount - realOriginTotalAmount.intValue();
                        hasRepayedPrincipalAmt = repaymentPlan.getRepaymentPrincipalAmount();
                        hasRepayInterestAmt = repaymentPlan.getRepaymentInterestAmount();
                        hasRepayOverdueAmt = realOverdueFeeAmount;
                        updateOverdueStatus = true;
                        isRepaymented = true;
                        logger.info("拆分还款订单：[该期是Condition3的第2种全额还款]:{}", JSON.toJSONString(repaymentPlan));
                    }
                }
            }
        } else if (conditionType == 4) {
            if (repaymentAmount.intValue() >= repaymentPlan.getRepaymentTotalAmount()) {
                repaymentAmount = repaymentAmount - repaymentPlan.getRepaymentTotalAmount().intValue();
                hasRepayedPrincipalAmt = repaymentPlan.getRepaymentPrincipalAmount();
                hasRepayInterestAmt = repaymentPlan.getRepaymentInterestAmount();
                hasRepayOverdueAmt = repaymentPlan.getOverdueFeeAmount();
                isRepaymented = true;
                logger.info("拆分还款订单：[该期是Condition4的全额还款]:{}", JSON.toJSONString(repaymentPlan));
            }
        }


        if (!isRepaymented) {

            // 依次减去本金，利息，罚息
            if (repaymentAmount.intValue() >= repaymentPlan.getRepaymentPrincipalAmount().intValue()) {
                repaymentAmount = repaymentAmount.intValue() - repaymentPlan.getRepaymentPrincipalAmount().intValue();
                hasRepayedPrincipalAmt = repaymentPlan.getRepaymentPrincipalAmount().intValue();
            } else {
                hasRepayedPrincipalAmt = repaymentAmount.intValue();
                remainingRepaymentPrincipalAmount = repaymentPlan.getRepaymentPrincipalAmount().intValue() - hasRepayedPrincipalAmt.intValue();
                repaymentAmount = 0;
            }

            //减去利息
            if (repaymentAmount.intValue() >= repaymentPlan.getRepaymentInterestAmount().intValue()) {
                repaymentAmount = repaymentAmount.intValue() - repaymentPlan.getRepaymentInterestAmount().intValue();
                hasRepayInterestAmt = repaymentPlan.getRepaymentInterestAmount().intValue();
            } else {
                hasRepayInterestAmt = repaymentAmount.intValue();
                remainingRepaymentInterestAmount = repaymentPlan.getRepaymentInterestAmount().intValue() - hasRepayInterestAmt.intValue();
                repaymentAmount = 0;
            }

            if (repaymentAmount.intValue() >= repaymentPlan.getOverdueFeeAmount().intValue()) {
                hasRepayOverdueAmt = repaymentPlan.getOverdueFeeAmount().intValue();
                repaymentAmount = repaymentAmount.intValue() - hasRepayOverdueAmt.intValue();
            } else {
                hasRepayOverdueAmt = repaymentAmount.intValue();
                remainingOverdueFeeAmount = repaymentPlan.getOverdueFeeAmount().intValue() - hasRepayOverdueAmt.intValue();
                repaymentAmount = 0;
            }

            logger.info("拆分还款订单：[该期是部分还款]:{}", JSON.toJSONString(repaymentPlan));
        }

        Integer remainingRepaymentTotalAmount = remainingRepaymentPrincipalAmount + remainingRepaymentInterestAmount + remainingOverdueFeeAmount;//剩余应还总金额
        Integer repayedRepaymentTotalAmount = hasRepayedPrincipalAmt + hasRepayInterestAmt + hasRepayOverdueAmt;//已还总金额

        //更新还款计划
        AlipayRepamentPlanDto alipayRepamentPlanDto = new AlipayRepamentPlanDto();
        alipayRepamentPlanDto.setId(repaymentPlan.getId());
        alipayRepamentPlanDto.setAmount(repayedRepaymentTotalAmount);
        alipayRepamentPlanDto.setRepaymentTotalAmount(remainingRepaymentTotalAmount);
        alipayRepamentPlanDto.setPrincipalAmount(remainingRepaymentPrincipalAmount);
        alipayRepamentPlanDto.setInterestAmount(remainingRepaymentInterestAmount);
        alipayRepamentPlanDto.setOverdueAmount(remainingOverdueFeeAmount);
        alipayRepamentPlanDto.setRepaymentTime(DateUtil.yyyyMMddHHmmss(repaymentDate));
        alipayRepamentPlanDto.setStatus(isRepaymented ? RepaymentPlanStatusEnum.Repaymented.getCode() : null);
        alipayRepamentPlanDto.setVersion(repaymentPlan.getVersion());
        if (updateOverdueStatus) {
            alipayRepamentPlanDto.setOverdue(isOverdue);
            alipayRepamentPlanDto.setOverdueDayCount(overdueDayCount);
        }
        alipayRepamentPlanDto.setRemark("支付宝人工入账更新数据");
        logger.info("更新还款计划：{}", JSON.toJSONString(alipayRepamentPlanDto));
        repaymentPlanService.updateRepaymentPlanAmount4Income(alipayRepamentPlanDto);

        //创建还款记录
        RepaymentRecord repaymentRecord = new RepaymentRecord();
        repaymentRecord.setUserId(repaymentPlan.getUserId());
        repaymentRecord.setRepaymentPlanId(repaymentPlan.getId());
        repaymentRecord.setPaymentRequestId(paymentRequestId);
        repaymentRecord.setStatus(PaymentRequestStatusEnum.SUCCESS.getCode());
        repaymentRecord.setPaymentType(PaymentOrderTypeEnum.ALIPAY.getCode());
        Date now = new Date();
        repaymentRecord.setCreatedAt(now);
        repaymentRecord.setUpdatedAt(now);
        repaymentRecord.setLoanOrderId(repaymentPlan.getLoanOrderId().toString());
        repaymentRecord.setRemark("支付宝人工入账");
        repaymentRecord.setAmount(repayedRepaymentTotalAmount);
        repaymentRecord.setRepayPrincipalAmt(hasRepayedPrincipalAmt);
        repaymentRecord.setRepayInterestAmt(hasRepayInterestAmt);
        repaymentRecord.setRepayOverdueAmt(hasRepayOverdueAmt);
        logger.info("创建还款记录:{}", JSON.toJSONString(repaymentRecord));
        repaymentRecordService.addRepaymentRecord(repaymentRecord);

        countRepaymentPlanDto.setRepaymented(isRepaymented);
        countRepaymentPlanDto.setRepaymentAmount(repaymentAmount);
        return countRepaymentPlanDto;
    }

    private Integer calculateOverdueFeeAmountByDay(Integer originPrincipalAmount, Integer overdueDayCount) {
        BigDecimal firstDayOverdueRate = new BigDecimal(0.05);
        BigDecimal notFirstDayOverdueRate = new BigDecimal(0.006);
        // 首日逾期：初始化本金的5%
        int firstOverdueFeeAmount = new BigDecimal(originPrincipalAmount).multiply(firstDayOverdueRate).setScale(0, BigDecimal.ROUND_HALF_UP).intValue();
        // 非首日： 日息0.006%
        int notFirstOverdueFeeAmount = new BigDecimal(originPrincipalAmount).multiply(notFirstDayOverdueRate).multiply(new BigDecimal(overdueDayCount - 1)).setScale(0, BigDecimal.ROUND_HALF_UP).intValue();
        int overdueFeeAmount = firstOverdueFeeAmount + notFirstOverdueFeeAmount;
        logger.info("实际逾期天数：{}, 实际逾期费：{}", overdueDayCount, overdueFeeAmount);
        return overdueFeeAmount;
    }

    // 根据period进行升序排列
    private List<RepaymentPlan> sortedByPeriodAsc(List<RepaymentPlan> repaymentPlans) {
        Collections.sort(repaymentPlans, new Comparator<RepaymentPlan>() {

            @Override
            public int compare(RepaymentPlan o1, RepaymentPlan o2) {
                if (o1.getPeriod().intValue() == o2.getPeriod().intValue()) {
                    throw new ServiceException("还款计划期数数据出错!");
                }
                if (o1.getPeriod().intValue() > o2.getPeriod().intValue()) {
                    return 1;
                }
                return -1;
            }
        });

        return repaymentPlans;
    }

}
