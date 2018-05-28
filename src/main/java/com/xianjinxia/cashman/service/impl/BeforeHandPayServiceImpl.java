package com.xianjinxia.cashman.service.impl;

import com.alibaba.fastjson.JSON;
import com.xianjingxia.paymentclient.paycenter.params.UserInfo;
import com.xianjinxia.cashman.constants.Constant;
import com.xianjinxia.cashman.constants.QueueConstants;
import com.xianjinxia.cashman.domain.LoanOrder;
import com.xianjinxia.cashman.domain.PaymentRequest;
import com.xianjinxia.cashman.domain.RepaymentPlan;
import com.xianjinxia.cashman.domain.RepaymentRecord;
import com.xianjinxia.cashman.dto.PaymentInstallmentDto;
import com.xianjinxia.cashman.enums.*;
import com.xianjinxia.cashman.exceptions.ServiceException;
import com.xianjinxia.cashman.idempotent.IdempotentService;
import com.xianjinxia.cashman.mapper.LoanOrderMapper;
import com.xianjinxia.cashman.mapper.RepaymentPlanMapper;
import com.xianjinxia.cashman.remote.OldCashmanRemoteService;
import com.xianjinxia.cashman.request.BeforeHandPayReq;
import com.xianjinxia.cashman.service.BeforeHandPayService;
import com.xianjinxia.cashman.service.repay.IPaymentRequestService;
import com.xianjinxia.cashman.service.repay.IRepaymentPlanService;
import com.xianjinxia.cashman.service.repay.IRepaymentRecordService;
import com.xianjinxia.cashman.service.repay.paycenter.BeforeHandRequestPayParam;
import com.xjx.mqclient.pojo.MqMessage;
import com.xjx.mqclient.service.MqClient;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Service
public class BeforeHandPayServiceImpl implements BeforeHandPayService {

    @Autowired
    private LoanOrderMapper loanOrderMapper;

    @Autowired
    private RepaymentPlanMapper repaymentPlanMapper;


    @Autowired
    private IPaymentRequestService paymentRequestService;

    @Autowired
    private IRepaymentRecordService repaymentRecordService;

    @Autowired
    private OldCashmanRemoteService oldCashmanRemoteService;
    @Autowired
    private IRepaymentPlanService repaymentPlanService;

    @Autowired
    private IdempotentService idempotentService;

    @Autowired
    private MqClient mqClient;
    private static final org.slf4j.Logger logger = LoggerFactory
            .getLogger(BeforeHandPayServiceImpl.class);

    @Override
    @Transactional
    public void beforeHandPay(BeforeHandPayReq beforeHandPayReq) {

        //幂等校验
        idempotentService.idempotentCheck(IdempotentEventTypeEnum.BEFORE_HAND_PAY,beforeHandPayReq);

        LoanOrder loanOrder = loanOrderMapper.selectByTrdOrderId(Long.valueOf(beforeHandPayReq.getOrderNo()));
        if (loanOrder == null) {
            throw new ServiceException("订单号为[" + beforeHandPayReq.getOrderNo() + "]不存在");
        }
        List<RepaymentPlan> repaymentPlanList = repaymentPlanMapper.selectRepaymentPlanByLoanOrderId(loanOrder.getTrdLoanOrderId(),loanOrder.getProductId());
        //创建request请求--添加request记录
        PaymentRequest paymentRequest = paymentRequestService.createPaymentRequest(loanOrder.getUserId(), loanOrder.getOrderAmount() + loanOrder.getInterestAmount(), PaymentRequestStatusEnum.NEW, PaymentBizTypeEnum.BEFOREHAND_PAY);
        List<RepaymentRecord> repaymentRecords = new ArrayList<>();
        for (RepaymentPlan repaymentPlan : repaymentPlanList) {
            //创建record--添加record记录
            RepaymentRecord repaymentRecord = repaymentRecordService.buildRepaymentRecord(paymentRequest.getId(), repaymentPlan, PaymentBizTypeEnum.BEFOREHAND_PAY);
            repaymentRecordService.addRepaymentRecord(repaymentRecord);
            repaymentRecords.add(repaymentRecord);
        }
        repaymentPlanService.freezeTotalAmountToWaitingAmount(repaymentRecords);
        UserInfo userInfo = oldCashmanRemoteService.getUserInfo(loanOrder.getUserId());
        BeforeHandRequestPayParam beforeHandRequestPayParam = buildBeforeHandRequestParam(loanOrder, userInfo, paymentRequest,repaymentPlanList);
        logger.info("推支付中心的数据为：{}",JSON.toJSONString(beforeHandRequestPayParam));
        MqMessage mqMessage = new MqMessage(JSON.toJSONString(beforeHandRequestPayParam), QueueConstants.PAYMENT_BEFORE_HAND_PAY_QUEUE);
        mqClient.sendMessage(mqMessage);
    }


    private BeforeHandRequestPayParam buildBeforeHandRequestParam(LoanOrder loanOrder, UserInfo userInfo, PaymentRequest paymentRequest, List<RepaymentPlan> repaymentPlanList) {
        BeforeHandRequestPayParam beforeHandRequestPayParam = new BeforeHandRequestPayParam();
        beforeHandRequestPayParam.setBizId(String.valueOf(paymentRequest.getId()));
        beforeHandRequestPayParam.setBizType(PaymentCenterBizTypeEnum.BIG_AMT_UNILATERAL.getCode());
        beforeHandRequestPayParam.setRequestSource(Constant.APPLICATION_PAYMENT_SOURCE);
        beforeHandRequestPayParam.setRouteStrategy(PayCenterRouteStrategyEnum.CUSTODY_INITIATIVE.getCode());
        beforeHandRequestPayParam.setAmount(Long.valueOf(loanOrder.getInterestAmount()));
        beforeHandRequestPayParam.setIdCardNo(userInfo.getIdCardNo());
        beforeHandRequestPayParam.setCardHolder(userInfo.getCardHolder());
        beforeHandRequestPayParam.setPhoneNo(userInfo.getPhoneNo());
        beforeHandRequestPayParam.setOrderStartTime(loanOrder.getCreatedTime());
        beforeHandRequestPayParam.setExtData("");//拓展字段，暂不传
        beforeHandRequestPayParam.setTxSerialNo(loanOrder.getBizSeqNo());
        beforeHandRequestPayParam.setLoanInterest(Long.valueOf(loanOrder.getInterestAmount()));
        beforeHandRequestPayParam.setRepaymentUser(String.valueOf(loanOrder.getUserId()));
        List<PaymentInstallmentDto> paymentInstallmentDtos = new ArrayList<>();
        for (RepaymentPlan repaymentPlan : repaymentPlanList) {
            PaymentInstallmentDto paymentInstallmentDto = new PaymentInstallmentDto();
            paymentInstallmentDto.setPeriod(repaymentPlan.getPeriod());
            paymentInstallmentDto.setInstallmentAmount(repaymentPlan.getRepaymentOriginInterestAmount());
            paymentInstallmentDto.setInstallmentPrincipal(0);
            paymentInstallmentDto.setInstallInterest(repaymentPlan.getRepaymentOriginInterestAmount());
            paymentInstallmentDto.setRepayType(RepayTypeEnum.ERROR_REPAY.getCode());
            paymentInstallmentDtos.add(paymentInstallmentDto);
        }
        beforeHandRequestPayParam.setDetailData(JSON.toJSONString(paymentInstallmentDtos));
        beforeHandRequestPayParam.setMerchant(loanOrder.getMerchantNo());
        return beforeHandRequestPayParam;
    }


}
