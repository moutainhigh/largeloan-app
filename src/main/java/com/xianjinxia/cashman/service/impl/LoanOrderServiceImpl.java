package com.xianjinxia.cashman.service.impl;

import java.util.Date;
import java.util.List;

import com.xianjinxia.cashman.constants.Constant;
import com.xianjinxia.cashman.domain.*;
import com.xianjinxia.cashman.dto.*;
import com.xianjinxia.cashman.enums.*;
import com.xianjinxia.cashman.mapper.*;
import com.xianjinxia.cashman.request.SyncLoanOrderReq;
import com.xianjinxia.cashman.request.SyncShoppingLoanOrderDeliverRequest;
import com.xianjinxia.cashman.response.SpeedCardRepayRsp;
import com.xianjinxia.cashman.service.ISmsService;
import com.xianjinxia.cashman.service.repay.IRepaymentPlanService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.xianjinxia.cashman.constants.QueueConstants;
import com.xianjinxia.cashman.exceptions.SqlUpdateException;
import com.xianjinxia.cashman.idempotent.IdempotentService;
import com.xianjinxia.cashman.remote.TradeAppRemoteService;
import com.xianjinxia.cashman.request.GroupRiskResultReq;
import com.xianjinxia.cashman.service.ILoanOrderService;
import com.xianjinxia.cashman.service.loan.risk.RiskCallbackContext;
import com.xianjinxia.cashman.strategy.money.MoneyContext;
import com.xjx.mqclient.pojo.MqMessage;
import com.xjx.mqclient.service.MqClient;
import com.xianjinxia.cashman.exceptions.ServiceException;

@Service
public class LoanOrderServiceImpl implements ILoanOrderService{

    private static final Logger logger = LoggerFactory.getLogger(LoanOrderServiceImpl.class);

    @Autowired
    private LoanOrderMapper loanOrderMapper;

    @Autowired
    private TradeAppRemoteService tradeAppRemoteService;

    @Autowired
    private MqClient mqClient;

    @Autowired
    private MoneyContext moneyContext;

    @Autowired
    private RepaymentPlanMapper repaymentPlanMapper;

    @Autowired
    private IdempotentService idempotentService;


    @Autowired
    private RiskCallbackContext riskCallbackContext;
    
    @Autowired
    private ProductsMapper productsMapper;

    @Autowired
    private ContractMapper contractMapper;

    @Autowired
    private LoanContractMapper loanContractMapper;
    @Autowired
    private ISmsService smsService;
    @Autowired
    private IRepaymentPlanService repaymentPlanService;

    @Autowired
    private RepaymentRecordMapper repaymentRecordMapper;

    @Override
    @Transactional
    public void updateLoanOrderStatusToOverdueByTradeAppId(Long trdLoanOrderId) {
        loanOrderMapper.updateLoanOrderStatusByTrdLoanOrderId(trdLoanOrderId, TrdLoanOrderStatusEnum.OVERDUE.getCode());
        logger.info("修改cashman-app的借款订单[trdLoanOrderId{}]的状态为已逾期", trdLoanOrderId);
//        tradeAppRemoteService.updateLoanOrderStatusToOverdue(trdLoanOrderId);
//        logger.info("修改trade-app的借款订单[trdLoanOrderId{}]的状态为已逾期", trdLoanOrderId);
    }

    @Override
    @Transactional
    public void updateLoanOrderStatusToOverdueById(Long loanOrderId) {
        LoanOrder loanOrder = loanOrderMapper.selectByPrimaryKey(loanOrderId);
        this.updateLoanOrderStatusToOverdueByTradeAppId(loanOrder.getTrdLoanOrderId());
    }

    @Override
    @Transactional
    public void updateLoanOrderStatusToSettledByTradeAppId(Long trdLoanOrderId) {
        loanOrderMapper.updateLoanOrderStatusByTrdLoanOrderId(trdLoanOrderId, TrdLoanOrderStatusEnum.SETTLED.getCode());
        logger.info("修改cashman-app的借款订单[trdLoanOrderId{}]的状态为已结清", trdLoanOrderId);
//        tradeAppRemoteService.updateLoanOrderStatusToSettled(trdLoanOrderId);
//        logger.info("修改trade-app的借款订单[trdLoanOrderId{}]的状态为已结清", trdLoanOrderId);
    }

    @Override
    @Transactional
    public void updateLoanOrderStatusByTrdLoanOrderId(Long trdLoanOrderId,String status) {
        loanOrderMapper.updateLoanOrderStatusByTrdLoanOrderId(trdLoanOrderId, status);
        logger.info("修改cashman-app的借款订单[trdLoanOrderId{}]的状态为{}", trdLoanOrderId, status);
    }

    @Override
    @Transactional
    public void updateLoanOrderStatusToSettledById(Long loanOrderId) {
        LoanOrder loanOrder = loanOrderMapper.selectByPrimaryKey(loanOrderId);
        this.updateLoanOrderStatusToSettledByTradeAppId(loanOrder.getTrdLoanOrderId());
    }


    /**
     * 处理风控回调结果
     * @param groupRiskResultReq
     */
    @Override
    @Transactional
    public void handleGroupRiskCallbackResult(GroupRiskResultReq groupRiskResultReq){
        logger.info(groupRiskResultReq.getOrder_id()+" handleGroupRiskCallbackResult开始=====");
        // 1.验证：接口幂等，订单不为空，风控结果
        idempotentService.idempotentCheck(IdempotentEventTypeEnum.GROUP_RISK_CALLBACK,groupRiskResultReq);

        LoanOrder loanOrder = loanOrderMapper.selectByPrimaryKey(Long.valueOf(groupRiskResultReq.getOrder_id()));
        if(loanOrder == null){
            logger.info("loanorder is null,orderId={}",groupRiskResultReq.getOrder_id());
            return ;
        }
        if(StringUtils.equals(TrdLoanOrderStatusEnum.REFUSED.getCode(),loanOrder.getStatus())||StringUtils.equals(TrdLoanOrderStatusEnum.APPROVED.getCode(),loanOrder.getStatus())){
            logger.info("loanorder is already callback,orderId={},status={} ",loanOrder.getTrdLoanOrderId(),loanOrder.getStatus());
            return;
        }

        GroupRiskResultEnum groupRiskResultEnum = GroupRiskResultEnum.getByCode(groupRiskResultReq.getResult());
        if(groupRiskResultEnum == null){
            logger.info("传入的result为["+groupRiskResultReq.getResult()+"]有误,orderId为["+groupRiskResultReq.getOrder_id()+"]");
            return ;
        }


        // 2. 根据不同的产品类型对风控回调订单做不同的处理
        riskCallbackContext.process(loanOrder, groupRiskResultEnum);

//        TrdLoanOrderStatusEnum orderStatusEnum = groupRiskResultEnum.getOrderStatus();
//
//        String updatedToStatus = orderStatusEnum.getCode();
//
//        // 判断订单前置状态是否符合条件
//        if(!TrdLoanOrderStatusEnum.canUpdate(updatedToStatus,loanOrder.getStatus())){
//            logger.info("订单号:{},订单前置状态为:{},要修改成状态为:{}",groupRiskResultReq.getOrder_id(),loanOrder.getStatus(),updatedToStatus);
//            return;
//        }
//
//        int updateNum;
//        if(TrdLoanOrderStatusEnum.REFUSED.getCode().equals(updatedToStatus)){//审核拒绝，设置审核失败时间
//            updateNum = loanOrderMapper.updateStatus(loanOrder.getId(), updatedToStatus, loanOrder.getStatus(), new Date());//乐观锁,防并发更新
//            int repaymentNum = repaymentPlanMapper.updateStatus(loanOrder.getTrdLoanOrderId(), RepaymentPlanStatusEnum.Canceled.getCode());
//            if(repaymentNum == 0){
//                logger.error("repaymentNum=0,loanOrderId={}",loanOrder.getTrdLoanOrderId());
//            }
//        }else {
//            updateNum = loanOrderMapper.updateStatus(loanOrder.getId(), updatedToStatus, loanOrder.getStatus(), null);//乐观锁,防并发更新
//        }
//
//        if(updateNum != 1){
//            logger.error("更新订单状态失败,订单id为:{}",loanOrder.getId());
//            return ;
//        }
//
//        List<MqMessage> messageList = new ArrayList<MqMessage>();
//
//
//
//
//        // 发送MQ到trade-app， 同步订单的状态
//        SyncLoanOrderDto syncLoanOrderDto = new SyncLoanOrderDto();
//        syncLoanOrderDto.setLoanOrderId(loanOrder.getTrdLoanOrderId());
//        syncLoanOrderDto.setStatus(updatedToStatus);
//        syncLoanOrderDto.setProductCategory(loanOrder.getProductCategory());
//        MqMessage mqMessage2 = new MqMessage(JSON.toJSONString(syncLoanOrderDto), QueueConstants.CASHMANAPP_SYNC_TRD_ORDER_STAUTS_TO_TRADE);
//        messageList.add(mqMessage2);
//
//        mqClient.sendMessagesByCommit(messageList, true);

        logger.info(groupRiskResultReq.getOrder_id()+" handleGroupRiskCallbackResult结束======");


    }


    /**
     * 发送消息到mq，同步订单状态到trade-app
     * @param trdLoanOrderId
     * @param status
     */
	public void sendMessage(Long  trdLoanOrderId, String status, Integer productCategory) {
		SyncLoanOrderDto syncLoanOrderDto = new SyncLoanOrderDto();
        syncLoanOrderDto.setLoanOrderId(trdLoanOrderId);
        syncLoanOrderDto.setStatus(status);
        syncLoanOrderDto.setProductCategory(productCategory);
        MqMessage message = new MqMessage(JSON.toJSONString(syncLoanOrderDto), QueueConstants.CASHMANAPP_SYNC_TRD_ORDER_STAUTS_TO_TRADE);
        mqClient.sendMessage(message);
	}

    @Override
    @Transactional
    public void processConfirmSuccess(LoanOrder trdLoanOrder) {
        LoanOrder loanOrder = new LoanOrder();
        BeanUtils.copyProperties(trdLoanOrder, loanOrder);
        loanOrder.setTrdLoanOrderId(trdLoanOrder.getId());
        loanOrder.setId(null);
        loanOrderMapper.insert(loanOrder);
        logger.info("===insert loanorder success===");


        List<RepaymentPlan> list = moneyContext.calcPeriodData(loanOrder.getProductId(),loanOrder.getOrderAmount(), loanOrder.getPeriods(), new Date());
        for (int i=0;i<list.size();i++) {
            RepaymentPlan repayment=list.get(i);
            repayment.setUserId(loanOrder.getUserId());
            repayment.setRepaymentOriginAmount(repayment.getRepaymentPrincipalAmount()+repayment.getRepaymentInterestAmount());
            repayment.setRepaymentOriginPrincipalAmount(repayment.getRepaymentPrincipalAmount());
            repayment.setRepaymentOriginInterestAmount(repayment.getRepaymentInterestAmount());
            repayment.setRepaymentTotalAmount(repayment.getRepaymentOriginAmount());
            repayment.setLoanOrderId(trdLoanOrder.getId());
            repayment.setProductId(loanOrder.getProductId());
            repayment.setOperationFlag(RepaymentPlanOperationFlagEnum.INIT.getCode());
            repaymentPlanMapper.insert(repayment);
        }

        logger.info("===生成还款计划成功===");

        //给用户发送短信，通知申请提交成功
        Boolean isSendShortMsgSuccess = false;
        if(null!=loanOrder.getMerchantNo()&&(MerchantNoEnum.MERCHANT_KJQB.getMerchantNo().equals(loanOrder.getMerchantNo()))){
            logger.info("快借钱包发送申请提交成功短信，商户号为：{}",loanOrder.getMerchantNo());
            isSendShortMsgSuccess = smsService.sendSms(new SmsDto(loanOrder.getBizSeqNo(), loanOrder.getUserPhone(), new StringBuilder(MerchantNoEnum.MERCHANT_KJQB.getMerchantNoticePrex()).append(Constant.SMS_APPLY_SUCCESS_ID).toString(),
                    "",null==loanOrder.getMerchantNo()?"":loanOrder.getMerchantNo()));
        }else {
            logger.info("非快借钱包发送申请成功短信");
            isSendShortMsgSuccess = smsService.sendSms(new SmsDto(loanOrder.getBizSeqNo(), loanOrder.getUserPhone(), Constant.SMS_APPLY_SUCCESS_ID,
                    "", null == loanOrder.getMerchantNo() ? "" : loanOrder.getMerchantNo()));
        }
        if(!isSendShortMsgSuccess){
            logger.warn("Send short msg for app-success is failed,bizSeqNo is "+loanOrder.getBizSeqNo());
        }

        logger.info("insert repaymentPlan order success");
    }
    
    /**
     * 更新还款计划表(还款计划时间)
     * @param syncShoppingLoanOrderDeliverRequest
     */
	public void updateListRepaymentPlanTime(SyncShoppingLoanOrderDeliverRequest syncShoppingLoanOrderDeliverRequest) {
    	// 1、接口幂等验证
        idempotentService.idempotentCheck(IdempotentEventTypeEnum.SYNC_REPAYMENT_PLAN_TIME, syncShoppingLoanOrderDeliverRequest);
        LoanOrder loanOrder = loanOrderMapper.selectByTrdOrderId(syncShoppingLoanOrderDeliverRequest.getTrdLoanOrderId());
        if(null == loanOrder){
            logger.error("未获取到订单信息,订单order_no:{}", syncShoppingLoanOrderDeliverRequest.getTrdLoanOrderId());
            throw new SqlUpdateException("未获取到订单信息");
        }

        int i = loanOrderMapper.updateStatus(loanOrder.getId(), TrdLoanOrderStatusEnum.LOAN_SUCCESS.getCode(), loanOrder.getStatus(), null);//乐观锁,防并发更新
        logger.info("更新借款订单的状态为待还款:{}", loanOrder.getId());
		Long trdLoanOrderId = syncShoppingLoanOrderDeliverRequest.getTrdLoanOrderId();
		Date paymentPlanTime = syncShoppingLoanOrderDeliverRequest.getPaymentPlanTime();
		Long productId = loanOrder.getProductId();
		Products product = productsMapper.getTermTypeAndTermById(productId);
		if(null == product){
			logger.error("未获取到产品信息,订单productId:{}", productId);
			throw new SqlUpdateException("未获取到产品信息");
		}


        //添加数据--用户的合同数据
        List<ContractDto> contracts=contractMapper.selectByProductId(loanOrder.getProductId());
        for(ContractDto contract:contracts) {
            LoanContract loanContract = new LoanContract(contract.getContractName(), contract.getContractType(), loanOrder.getTrdLoanOrderId(), loanOrder.getUserId(), LoanContractStatusEnum.NOT_HANDLE.getCode());
            loanContractMapper.insert(loanContract);
        }

		Long loanOrderId = loanOrder.getId();
		List<RepaymentPlan> listRepayMentPlan = repaymentPlanMapper.selectRepaymentPlanByLoanOrderId(loanOrderId, loanOrder.getProductId());
		for (RepaymentPlan repaymentPlan : listRepayMentPlan) {
			int period = repaymentPlan.getPeriod();
			Date currPeriodRepaymentPlanTime = moneyContext.getCurrPeriodRepaymentPlanTime(product, paymentPlanTime, period);
			int updateRows = repaymentPlanMapper.updateRepaymentPlanTime(loanOrderId, currPeriodRepaymentPlanTime, period);
			if (updateRows <= 0) {
				logger.error("更新还款计划表预期还款时间失败,订单order_no:{},period:{}",trdLoanOrderId, period);
				throw new SqlUpdateException("更新还款计划表预期还款时间失败");
			}
		}
	}


    public void updateSpeedCardPayStatus(SpeedCardPayResultDto speedCardPayResultDto){
        String speedCardPayStatus = null;
        if(Constant.YES.equals(speedCardPayResultDto.getPayResult())){
            speedCardPayStatus = SpeedCardPayStatusEnum.SPEED_CARD_PAY_SUCCESS.getCode();
        }else if(Constant.NO.equals(speedCardPayResultDto.getPayResult())){
            speedCardPayStatus = SpeedCardPayStatusEnum.SPEED_CARD_PAY_FAIL.getCode();
        }else if(Constant.PAYING.equals(speedCardPayResultDto.getPayResult())){
            speedCardPayStatus = SpeedCardPayStatusEnum.SPEED_CARD_PAYING.getCode();
        } else{
            logger.error(speedCardPayResultDto.getOrderId()+"参数有误");
            return;
        }
        LoanOrder loanOrder = loanOrderMapper.selectByPrimaryKey(speedCardPayResultDto.getOrderId());
        if(loanOrder == null){
            logger.info("loanorder is null,id="+speedCardPayResultDto.getOrderId());
            return ;
        }

        int updateNum = loanOrderMapper.updateSpeedCardPayStatus(speedCardPayResultDto.getOrderId(),speedCardPayStatus);
        if(updateNum != 1){
            logger.error("更新提速卡状态失败"+speedCardPayResultDto.getOrderId());
            throw new ServiceException("更新提速卡状态失败"+speedCardPayResultDto.getOrderId());
        }
    }

    @Override
    public SpeedCardRepayRsp getSpeedRepayStatus(Long loanOrderId) {
        SpeedCardRepayRsp speedCardRepayRsp =new SpeedCardRepayRsp();
        //订单审核通过状态
        String[] orderStatus  = new String[]{ TrdLoanOrderStatusEnum.MANUAL_APPROVED.getCode() , TrdLoanOrderStatusEnum.APPROVED.getCode() };
        SpeedCardRepayDto speedCardRepayDto = loanOrderMapper.getSpeedRepayStatus(loanOrderId,orderStatus);

        logger.info("订单id为:"+loanOrderId+",订单的提速卡数据为："+speedCardRepayDto.toString());
        if(SpeedCardPayStatusEnum.SPEED_CARD_PAY_FAIL.getCode().equals(String.valueOf(speedCardRepayDto.getSpeedCardPayStatus()))){
            //如果为支付失败
            speedCardRepayRsp =new SpeedCardRepayRsp(true,speedCardRepayDto.getOrderId());
        }else{
            speedCardRepayRsp =new SpeedCardRepayRsp(false,speedCardRepayDto.getOrderId());
        }
        return speedCardRepayRsp;
    }

    @Override
    public Integer getOverdueAmtByLoanId(Long trdLoanOrderId) {
        Integer overdueAmt = 0;
        //获取还款计划
        List<RepaymentPlan> repaymentPlans = repaymentPlanService.getRepaymentOrderListByLoanOrderId(trdLoanOrderId, true);
        if (CollectionUtils.isNotEmpty(repaymentPlans)) {
            for (RepaymentPlan repaymentPlan : repaymentPlans) {
                if (repaymentPlan.getIsOverdue()) {
                    //还款信息
                    List<RepaymentRecord> repaymentRecords = repaymentRecordMapper.selectRepaymentedRecords(repaymentPlan.getId());
                    if (repaymentRecords != null && repaymentRecords.size() > 0) {
                        for (RepaymentRecord repaymentRecord : repaymentRecords) {
                            overdueAmt += repaymentRecord.getRepayOverdueAmt();
                        }
                    }
                    overdueAmt += repaymentPlan.getOverdueFeeAmount();
                }
            }
        }
        logger.info("订单号为{}，逾期金额为{}", trdLoanOrderId, overdueAmt);
        return overdueAmt;
    }

    @Override
    public LoanOrder selectByUserPhone(String userPhone, String merchantNo) {
        return loanOrderMapper.selectByUserPhone(userPhone, merchantNo);
    }


    @Override
    @Transactional
    public void syncLoanOrderStatus(SyncLoanOrderReq syncLoanOrderReq) {
        // 1、接口幂等验证
        idempotentService.idempotentCheck(IdempotentEventTypeEnum.SYNC_SHOPPING_ORDER_STATUS, syncLoanOrderReq);

        int shoppingProductEnumVal = ProductCategoryEnum.PRODUCT_CATEGORY_SHOPPING.getCode().intValue();
        if (syncLoanOrderReq.getProductCategory().intValue() != shoppingProductEnumVal) {
            logger.info("非金融商品的订单状态同步请求，忽略此请求，请求参数：{}", syncLoanOrderReq.toString());
            return;
        }

        // 2、订单信息验证
        LoanOrder loanOrder = loanOrderMapper.selectByTrdOrderId(syncLoanOrderReq.getLoanOrderId());
        if(null == loanOrder){
            logger.error("未获取到订单信息,订单trd_loan_order_id:{}", syncLoanOrderReq.getLoanOrderId());
            throw new SqlUpdateException("未获取到订单信息");
        }
        logger.info("to update loan order,parameter id:{},new status:{}, old status:{}", loanOrder.getId(), syncLoanOrderReq.getStatus(), loanOrder.getStatus());
        int i = loanOrderMapper.updateStatus(loanOrder.getId(), syncLoanOrderReq.getStatus(), loanOrder.getStatus(), null);//乐观锁,防并发更新
        logger.info("更新借款订单的状态为失败:{},number:{}", loanOrder.getId(), i);

    }

    /* 更新失败不需要回滚, 可能多期的还款计划不是全部都还清了,因此可能存在更新不成功,如果更新成功了就需要同步trade-app和恢复用户额度. */
    @Override public int updateLoanOrderStatus2Over(Long trdLoanOrderId) {
        int count = loanOrderMapper.updateLoanOrderStatus2Over(trdLoanOrderId);
        logger.info("更新借款订单编号:{}状态为终态的结果是:{}!!!", trdLoanOrderId, count == 0 ? "失败":"成功");
        return count;
    }
}
