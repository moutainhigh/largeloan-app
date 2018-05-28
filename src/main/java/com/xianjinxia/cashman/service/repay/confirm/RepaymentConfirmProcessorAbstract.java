package com.xianjinxia.cashman.service.repay.confirm;

import com.alibaba.fastjson.JSON;
import com.xianjinxia.cashman.constants.QueueConstants;
import com.xianjinxia.cashman.domain.PaymentRequest;
import com.xianjinxia.cashman.domain.RepaymentPlan;
import com.xianjinxia.cashman.domain.RepaymentRecord;
import com.xianjinxia.cashman.mapper.LoanOrderMapper;
import com.xianjinxia.cashman.mapper.PaymentRequestConfigMapper;
import com.xianjinxia.cashman.mapper.ProductsMapper;
import com.xianjinxia.cashman.mapper.ScheduleTaskOverdueMapper;
import com.xianjinxia.cashman.remote.PayCenterRemoteService;
import com.xianjinxia.cashman.request.PayCenterCallbackReq;
import com.xianjinxia.cashman.request.PaymentCenterCallbackRequestCode;
import com.xianjinxia.cashman.service.IMqMessageService;
import com.xianjinxia.cashman.service.repay.paycenter.IPayCenterService;
import com.xianjinxia.cashman.service.repay.IPaymentRequestService;
import com.xianjinxia.cashman.service.repay.IRepaymentPlanService;
import com.xianjinxia.cashman.service.repay.IRepaymentRecordService;
import com.xianjinxia.cashman.service.repay.checker.RepaymentChecker;
import com.xianjinxia.cashman.service.repay.collection.CollectionNotifyDto;
import com.xianjinxia.cashman.service.repay.collection.CollectionNotifyDtoBuilder;
import com.xianjinxia.cashman.service.repay.collection.CollectionNotifyService;
import com.xjx.mqclient.pojo.MqMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public abstract class RepaymentConfirmProcessorAbstract implements RepaymentConfirmProcessor {

	private Logger logger = LoggerFactory.getLogger(RepaymentConfirmProcessorAbstract.class);


	@Autowired
	protected IRepaymentPlanService repaymentPlanService;

	@Autowired
	protected RepaymentConfirmContext paymentCallbackProcessor;

	@Autowired
	protected IPaymentRequestService paymentRequestService;

	@Autowired
	protected IRepaymentRecordService repaymentRecordService;

	@Autowired
	protected RepaymentChecker repaymnetCheck;

	@Autowired
	protected IPayCenterService payCenterService;

	@Autowired
	protected PaymentRequestConfigMapper paymentRequestConfigMapper;

	@Autowired
	protected ProductsMapper productsMapper;

	@Autowired
	protected LoanOrderMapper loanOrderMapper;

	@Autowired
	protected IMqMessageService mqMessageService;

	@Autowired
	protected PayCenterRemoteService payCenterRemoteService;

	@Autowired
	protected CollectionNotifyDtoBuilder collectionNotifyDtoBuilder;

	@Autowired
	protected CollectionNotifyService collectionNotifyService;

	@Autowired
	protected ScheduleTaskOverdueMapper scheduleTaskOverdueMapper;


	protected void processRepayment(RepaymentConfirmProcessParam repaymentConfirmProcessParam) {
		PaymentRequest paymentRequest = repaymentConfirmProcessParam.getPaymentRequest();
		PayCenterCallbackReq payCenterCallbackReq = repaymentConfirmProcessParam.getPayCenterCallbackReq();
		List<RepaymentRecord> repaymentRecordList = repaymentConfirmProcessParam.getRepaymentRecordList();
		String payCenterRespCode = payCenterCallbackReq.getCode();
		String payCenterRespId = payCenterCallbackReq.getOrderNo();
		String payCenterRespMsg = payCenterCallbackReq.getMsg();
		String paymentChannel = payCenterCallbackReq.getPayType();
		String thirdOrderNo = payCenterCallbackReq.getThirdOrderNo();

		if (PaymentCenterCallbackRequestCode.SUCCESS.equals(payCenterRespCode)) {
			logger.info("支付中心回调：支付请求[{}]处理成功", paymentRequest.getId());
			logger.info("支付中心回调成功，支付渠道={},thirdOrderNo={},requestId={}",paymentChannel,thirdOrderNo,paymentRequest.getId());
			paymentRequestService.updatePaymentRequestStatusToSuccess(paymentRequest.getId(), payCenterRespId, payCenterRespMsg,paymentChannel,thirdOrderNo);
			repaymentRecordService.updateRepaymentRecordToSuccess(paymentRequest.getId(), repaymentRecordList);
			this.increaseRepaymentPlanIncomeAmount(repaymentRecordList);
			this.paymentCenterCallbackSuccAction(repaymentConfirmProcessParam);
			// TODO 逾期的情况下修改借款订单状态
			
		} else {
			logger.info("支付中心回调：支付请求[{}]处理失败", paymentRequest.getId());
			logger.info("支付中心回调失败，支付渠道={},thirdOrderNo={},requestId={}",paymentChannel,thirdOrderNo,paymentRequest.getId());
			paymentRequestService.updatePaymentRequestStatusToFailure(paymentRequest.getId(), payCenterRespId, payCenterRespMsg,paymentChannel,thirdOrderNo);
			repaymentRecordService.updateRepaymentRecordToFailure(paymentRequest.getId(), repaymentRecordList);
			this.increaseRepaymentPlanTotalAmount(repaymentRecordList);
			this.paymentCenterCallbackFailAction(repaymentConfirmProcessParam);
		}
	}

	private void increaseRepaymentPlanIncomeAmount(List<RepaymentRecord> repaymentRecords){
		List<MqMessage> messageList = new ArrayList<>();
		for (RepaymentRecord repaymentRecord : repaymentRecords) {
			RepaymentPlan repaymentPlan = repaymentPlanService.getRepaymentPlanByIdWithoutCheck(repaymentRecord.getRepaymentPlanId());

			// 根据total_amount、wait_amount判断订单是否全额还款
			// wait_amount 如果是 300，500 2笔订单，共800，那么最后一个的paymentOrder.getAmount是等于300或者500的，此时的wait_amount也必然等于300或者500
			boolean isTotalAmountEqZero = repaymentPlan.getRepaymentTotalAmount() == 0;
			boolean isPayAmountEqWaitingAmount = (repaymentRecord.getAmount().intValue() == repaymentPlan.getRepaymentWaitingAmount().intValue());

			// 是否全额还款
			boolean isRepayAll = isTotalAmountEqZero && isPayAmountEqWaitingAmount;

			// 还款成功的时候需要：
			// a.减去本金、利息、罚息
			// b.冻结金额加到入账金额上
			int repayPrincipalAmt = repaymentRecord.getRepayPrincipalAmt() == null ? 0 : repaymentRecord.getRepayPrincipalAmt();
			int repayInterestAmt = repaymentRecord.getRepayInterestAmt() == null ? 0 : repaymentRecord.getRepayInterestAmt();
			int repayOverdueAmt = repaymentRecord.getRepayOverdueAmt() == null ? 0 : repaymentRecord.getRepayOverdueAmt();

			if(isRepayAll){
				// 修改repayment_plan表的real_repay_time，修改为当前时间
				logger.info("全额还款订单[{}], 当前还款金额：[{}], 之前已入账金额：[{}]", repaymentPlan.getId(), repaymentRecord.getAmount(), repaymentPlan.getRepaymentIncomeAmount());
				repaymentPlanService.increaseIncomingAmount(repaymentRecord.getRepaymentPlanId(), repaymentRecord.getAmount(), repaymentPlan.getVersion(), true);
			}else{
				logger.info("部分还款订单[{}], 当前还款金额：[{}]，剩余待还款金额:[{}]", repaymentPlan.getId(), repaymentRecord.getAmount(), repaymentPlan.getRepaymentWaitingAmount());
				repaymentPlanService.increaseIncomingAmount(repaymentRecord.getRepaymentPlanId(), repaymentRecord.getAmount(), repaymentPlan.getVersion());
			}

			// 全额还款和催收
			if (repaymentPlan.getIsOverdue()) {
				//通知催收关闭此订单
				CollectionNotifyDto collectionNotifyDto = collectionNotifyDtoBuilder.build(repaymentPlan.getId(), true);
				MqMessage message = new MqMessage();
				message.setMessage(JSON.toJSONString(collectionNotifyDto));
				message.setQueueName(QueueConstants.COLLECTION_REPAYMENT_PLAN_OVERDUE_REPAY);
				messageList.add(message);
				//collectionNotifyService.cancel(collectionNotifyDto);
				//从逾期的任务列表中删除
				if(isRepayAll){
					scheduleTaskOverdueMapper.updateIsRepaymentedByOrderId(repaymentPlan.getId(), true);
				}
			}
		}
		mqMessageService.sendMessageList(messageList);
	}


	private void increaseRepaymentPlanTotalAmount(List<RepaymentRecord> repaymentRecords){
		for (RepaymentRecord repaymentRecord : repaymentRecords) {
			RepaymentPlan repaymentPlan = repaymentPlanService.getRepaymentPlanByIdWithoutCheck(repaymentRecord.getRepaymentPlanId());
			repaymentPlanService.substractWaitingAmount(repaymentRecord.getRepaymentPlanId(), repaymentRecord.getAmount(),repaymentRecord.getRepayPrincipalAmt(), repaymentRecord.getRepayInterestAmt(), repaymentRecord.getRepayOverdueAmt(), repaymentPlan.getVersion());
		}
	}
}
