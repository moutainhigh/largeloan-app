package com.xianjinxia.cashman.service.repay.deduct;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.xianjinxia.cashman.constants.ErrorCodeConstants;
import com.xianjinxia.cashman.constants.QueueConstants;
import com.xianjinxia.cashman.domain.*;
import com.xianjinxia.cashman.dto.QuotaGiveBackDto;
import com.xianjinxia.cashman.dto.SyncLoanOrderDto;
import com.xianjinxia.cashman.enums.*;
import com.xianjinxia.cashman.exceptions.CashmanExceptionBuilder;
import com.xianjinxia.cashman.mapper.CollectRequestMapper;
import com.xianjinxia.cashman.mapper.LoanOrderMapper;
import com.xianjinxia.cashman.mapper.ScheduleTaskOverdueMapper;
import com.xianjinxia.cashman.request.CollectionDeductReq;
import com.xianjinxia.cashman.service.ILoanOrderService;
import com.xianjinxia.cashman.service.IMqMessageService;
import com.xianjinxia.cashman.service.repay.IPaymentRequestService;
import com.xianjinxia.cashman.service.repay.IRepaymentPlanService;
import com.xianjinxia.cashman.service.repay.IRepaymentRecordService;
import com.xianjinxia.cashman.service.repay.collection.CollectionNotifyDto;
import com.xianjinxia.cashman.service.repay.collection.CollectionNotifyDtoBuilder;
import com.xjx.mqclient.pojo.MqMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
/** @author ganminghui 后置减免策略类*/
@Service("DeductStrategyImplByRepayAfter")
public class DeductStrategyImplByRepayAfter implements DeductStrategy {
    /* 催收减免流水中的remark常量 */
    private static final String RECORD_REMARK = "催收减免, 减免金额:%d";
    /* 催收减免流水中的还款本金 */
    private static final Integer RECORD_PRINCIPAL_AMT = 0;
    /* 催收减免流水中的还款利息 */
    private static final Integer RECORD_INTEREST_AMT = 0;

    private final IRepaymentPlanService repaymentPlanService;

    private final IRepaymentRecordService repaymentRecordService;

    private final CollectionNotifyDtoBuilder collectionNotifyDtoBuilder;

    private final ILoanOrderService loanOrderService;

    private final ScheduleTaskOverdueMapper scheduleTaskOverdueMapper;

    private final IMqMessageService mqMessageService;

    private final CollectRequestMapper collectRequestMapper;

    private final IPaymentRequestService paymentRequestService;

    private final LoanOrderMapper loanOrderMapper;

    private static final Logger LOGGER = LoggerFactory.getLogger(DeductStrategyImplByRepayAfter.class);

    @Autowired public DeductStrategyImplByRepayAfter(IRepaymentPlanService repaymentPlanService, IRepaymentRecordService repaymentRecordService, CollectionNotifyDtoBuilder collectionNotifyDtoBuilder, ILoanOrderService loanOrderService, ScheduleTaskOverdueMapper scheduleTaskOverdueMapper, IMqMessageService mqMessageService, CollectRequestMapper collectRequestMapper, IPaymentRequestService paymentRequestService, LoanOrderMapper loanOrderMapper) {
        this.repaymentPlanService = repaymentPlanService;
        this.repaymentRecordService = repaymentRecordService;
        this.collectionNotifyDtoBuilder = collectionNotifyDtoBuilder;
        this.loanOrderService = loanOrderService;
        this.scheduleTaskOverdueMapper = scheduleTaskOverdueMapper;
        this.mqMessageService = mqMessageService;
        this.collectRequestMapper = collectRequestMapper;
        this.paymentRequestService = paymentRequestService;
        this.loanOrderMapper = loanOrderMapper;
    }

    @Override @Transactional(rollbackFor = Exception.class) public DeductResult deduct(CollectionDeductReq collectionDeductReq) {
        /* 1. 检查催收减免策略路由是否是后置减免策略 */
        if (collectionDeductReq.getDeductType() != DeductTypeEnum.AFTER.getType()){ return new DeductResult(false); }

        /* 2. 查询还款计划 */
        RepaymentPlan repaymentPlan = Optional.ofNullable(repaymentPlanService.getRepaymentPlanById(collectionDeductReq.getRepaymentId(), true)).orElse(null);
        LOGGER.info("催收减免请求借款单编号:{}查询的最新借款单明细为:{}", collectionDeductReq.getRepaymentId(), repaymentPlan);

        /* 3. 业务验证 */
        validRepayDeduct(collectionDeductReq, repaymentPlan);
        LOGGER.info("催收减免请求业务验证通过!!!");

        /* 4. 催收代扣请求落库*/
        CollectRequest request = collectionDeductReq.buildDeduct();
        collectRequestMapper.insert(request);
        LOGGER.info("催收减免请求落库成功!!!请求id:{}", request.getId());

        /* 5.  催收减免支付请求落库 */
        PaymentRequest paymentRequest = paymentRequestService.createPaymentRequest(repaymentPlan.getUserId(), collectionDeductReq.getReductionMoney().intValue(),
                PaymentRequestStatusEnum.SUCCESS, PaymentBizTypeEnum.COLLECT_Deduct,PaymentChannelEnum.RELIEF_CHANNEL);
        LOGGER.info("催收减免支付请求落库完成!!!支付请求id:{}", paymentRequest.getId());

        /* 5. 保存催收减免的还款流水 */
        RepaymentRecord deductRecord = new RepaymentRecord(repaymentPlan.getUserId(), repaymentPlan.getId(), paymentRequest.getId(),
                PaymentRequestStatusEnum.SUCCESS.getCode(), collectionDeductReq.getReductionMoney().intValue(),
                String.format(RECORD_REMARK, collectionDeductReq.getReductionMoney()), PaymentOrderTypeEnum.COLLECTION_DEDUCT.getCode(),
                repaymentPlan.getLoanOrderId().toString(), RECORD_PRINCIPAL_AMT, RECORD_INTEREST_AMT, collectionDeductReq.getReductionMoney().intValue());
        repaymentRecordService.addRepaymentRecord(deductRecord);
        LOGGER.info("催收减免还款流水落库完成!!!还款流水id:{}", deductRecord.getId());

        /* 6. 更新还款计划为终态(已还清) */
        repaymentPlanService.deductTotalAmount(repaymentPlan.getId(), collectionDeductReq.getReductionMoney().intValue(), repaymentPlan.getVersion());
        LOGGER.info("催收减免还款计划:【{}】更新为已还清成功!!!", repaymentPlan.getId());

        /* 7. 更新借款单状态(当该借款订单下的所有还款计划全部还清时修改借款单状态)
        *  <p>
        *      如果借款订单更新为终态, 则需要同步订单状态到trade-app,同时恢复用户额度.
        *      如果借款订单没有更新为终态,则不需要同步订单状态, 也不需要恢复用户额度.
        *  </p>
        * */
        int count = loanOrderService.updateLoanOrderStatus2Over(repaymentPlan.getLoanOrderId());

        /* 7.1  根据更新借款订单的结果判断构造同步订单状态到trade-app的消息、恢复用户额度的消息 */
        List<MqMessage> mqMessageList = new ArrayList<>();
        if(count >0 ){
            LoanOrder loanOrder = loanOrderMapper.selectByTrdOrderId(repaymentPlan.getLoanOrderId());
            LOGGER.info("查询到借款订单编号:{}状态:{}即将状态同步到trade-app去!!!",loanOrder.getTrdLoanOrderId(), loanOrder.getStatus());

            QuotaGiveBackDto quotaGiveBackDto = new QuotaGiveBackDto(loanOrder.getUserId(), loanOrder.getOrderAmount(), String.valueOf(paymentRequest.getId()), ProductCategoryEnum.PRODUCT_CATEGORY_BIG.getCode());
            mqMessageList.add(new MqMessage(JSON.toJSONString(quotaGiveBackDto), QueueConstants.USER_AVAILABLE_AMOUNT_MQ_NAME));

            SyncLoanOrderDto syncLoanOrderDto = new SyncLoanOrderDto(loanOrder.getTrdLoanOrderId(), TrdLoanOrderStatusEnum.SETTLED.getCode(), loanOrder.getProductCategory());
            mqMessageList.add(new MqMessage(JSON.toJSONString(syncLoanOrderDto), QueueConstants.CASHMANAPP_SYNC_TRD_ORDER_STAUTS_TO_TRADE));

            LOGGER.info("同步状态mq、恢复额度mq已就绪!!!");
        }

        /* 8. 更新逾期任务表 */
        scheduleTaskOverdueMapper.updateIsRepaymentedByOrderId(repaymentPlan.getId(), true);
        LOGGER.info("催收减免还款计划:【{}】逾期任务表状态更新成功!!!", repaymentPlan.getId());

        /* 9. 通知催收关闭订单、通知催收告知减免结果 */
        CollectionNotifyDto collectionNotifyDto = collectionNotifyDtoBuilder.build(repaymentPlan.getId(), true);

        ImmutableMap<Object, Object> collectionNotifyMap = ImmutableMap.builder().put("result", true).put("msg", "success")
                .put("uuid", collectionDeductReq.getUuid())
                .put("reductionMoney", collectionDeductReq.getReductionMoney())
                .put("status", collectionDeductReq.getStatus())
                .put("repaymentId", collectionDeductReq.getRepaymentId()).build();

        mqMessageList.addAll(ImmutableList.of(
                new MqMessage(JSON.toJSONString(collectionNotifyDto), QueueConstants.COLLECTION_REPAYMENT_PLAN_OVERDUE_REPAY),
                new MqMessage(JSON.toJSONString(collectionNotifyMap), QueueConstants.CUISHOU_DEDUCT_QUEUE_BIG)
        ));
        mqMessageService.sendMessageList(mqMessageList);
        LOGGER.info("催收减免还款计划:【{}】发送催收通知成功,一共发送{}(条)消息!!!", repaymentPlan.getId(),mqMessageList.size());
        return new DeductResult(true);
    }

    /** 验证催收减免相关条件 */
    private void validRepayDeduct(CollectionDeductReq collectionDeductReq, RepaymentPlan repaymentPlan) {
        /* 1.0  验证催收减免金额是否大于0 */
        if (collectionDeductReq.getReductionMoney() <= 0) {
            LOGGER.error("催收减免还款计划编号:【{}】代扣金额:【{}】不能小于等于0!!!", collectionDeductReq.getRepaymentId(), collectionDeductReq.getReductionMoney());
            throw CashmanExceptionBuilder.build(ErrorCodeConstants.DEDUCT_AMOUNT_ERROR);
        }

        /* 1.1  验证还款计划是否存在或该还款计划是否是逾期标志 */
        if (Objects.isNull(repaymentPlan) || !repaymentPlan.getOperationFlag().equals(RepaymentPlanOperationFlagEnum.OVERDUE.getCode())) {
            LOGGER.error("催收减免还款计划不存在,或操作标识不等于【{}】!!!", RepaymentPlanOperationFlagEnum.OVERDUE.getCode());
            throw CashmanExceptionBuilder.build(ErrorCodeConstants.DEDUCT_REPAYMENT_PLAN_STATUS_HOLDED);
        }

        /* 1.2  验证还款计划是否逾期 */
        if (!repaymentPlan.getIsOverdue()) {
            LOGGER.error("催收减免还款计划编号:【{}】不是逾期状态,不可以进行催收减免!!!", collectionDeductReq.getRepaymentId());
            throw CashmanExceptionBuilder.build(ErrorCodeConstants.DEDUCT_NOT_SUPPORT_REPAYMENT_PLAN_STATUS);
        }

        /* 1.3  验证还款计划是否存在未入账 */
        if (repaymentPlan.getRepaymentWaitingAmount() > 0) {
            LOGGER.error("催收减免还款计划编号:【{}】存在在途金额,暂时不允许催收减免!!!", collectionDeductReq.getRepaymentId());
            throw CashmanExceptionBuilder.build(ErrorCodeConstants.DEDUCT_NOT_SUPPORT_REPAYMENT_PLAN_WAITING_AMT);
        }

        /* 1.4  验证待还本金和待还利息都为0 */
        if(repaymentPlan.getRepaymentPrincipalAmount() != 0 || repaymentPlan.getRepaymentInterestAmount() != 0){
            LOGGER.error("催收减免还款计划编号:【{}】没有还清本利和利息, 其中待还本金:【{}】, 待还利息:【{}】!!!",
                    collectionDeductReq.getRepaymentId(), repaymentPlan.getRepaymentPrincipalAmount(), repaymentPlan.getRepaymentInterestAmount());
            throw CashmanExceptionBuilder.build(ErrorCodeConstants.DEDUCT_AMOUNT_PRINCIPAL_INTEREST_ERROR);
        }

        /* 1.5  验证待减免金额是否等于待还款总额, 是否等于待还滞纳金 */
        if(repaymentPlan.getRepaymentTotalAmount() != collectionDeductReq.getReductionMoney().intValue() || !repaymentPlan.getRepaymentTotalAmount().equals(repaymentPlan.getOverdueFeeAmount())){
            LOGGER.error("催收减免还款计划编号:【{}】待减免金额不等于待还款总金额,或不等于滞纳金, 其中减免金额:【{}】, 待还款总金额:【{}】, 滞纳金:【{}】",
                    collectionDeductReq.getRepaymentId(), collectionDeductReq.getReductionMoney(), repaymentPlan.getRepaymentTotalAmount(), repaymentPlan.getOverdueFeeAmount());
            throw CashmanExceptionBuilder.build(ErrorCodeConstants.DEDUCT_AMOUNT_OVERDUEFEE_ERROR);
        }
    }

}