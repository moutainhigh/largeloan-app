package com.xianjinxia.cashman.service.repay.paycenter;

import com.xianjingxia.paymentclient.paycenter.enums.PaymentMethodEnum;
import com.xianjingxia.paymentclient.paycenter.params.OrderInfo;
import com.xianjingxia.paymentclient.paycenter.params.UserInfo;
import com.xianjinxia.cashman.constants.Constant;
import com.xianjinxia.cashman.constants.QueueConstants;
import com.xianjinxia.cashman.domain.LoanOrder;
import com.xianjinxia.cashman.domain.RepaymentPlan;
import com.xianjinxia.cashman.domain.RepaymentRecord;
import com.xianjinxia.cashman.dto.PaymentInstallmentDto;
import com.xianjinxia.cashman.dto.PaymentParamDto;
import com.xianjinxia.cashman.enums.PayCenterRouteStrategyEnum;
import com.xianjinxia.cashman.enums.PaymentCenterBizTypeEnum;
import com.xianjinxia.cashman.enums.ProductCategoryEnum;
import com.xianjinxia.cashman.enums.RepayTypeEnum;
import com.xianjinxia.cashman.mapper.PaymentRequestConfigMapper;
import com.xianjinxia.cashman.remote.OldCashmanRemoteService;
import com.xianjinxia.cashman.service.IMqMessageService;
import com.xianjinxia.cashman.service.repay.IRepaymentPlanService;
import com.xianjinxia.cashman.service.repay.IRepaymentRecordService;
import com.xianjinxia.cashman.service.repay.confirm.RepaymentConfirmProcessor;
import com.xianjinxia.cashman.utils.Md5Util;
import com.xjx.mqclient.pojo.MqMessage;
import com.xjx.mqclient.utils.common.FastJson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

@Service
public class PayCenterServiceImpl implements IPayCenterService {

    private static final Logger logger = LoggerFactory.getLogger(PayCenterServiceImpl.class);

    @Autowired
    private PaymentRequestConfigMapper paymentRequestConfigMapper;

    @Autowired
    private IMqMessageService mqMessageService;

    @Autowired
    private List<RepaymentConfirmProcessor> repaymentConfirmProcessors;

    @Autowired
    private OldCashmanRemoteService oldCashmanRemoteService;


    @Autowired
    private IRepaymentRecordService repaymentRecordService;


    @Autowired
    private IRepaymentPlanService repaymentPlanService;

    @Override
    public void payWithMQ(String userId, Long paymentRequestId, Integer amount, PaymentCenterBizTypeEnum bizType, String extData, boolean isHistoryOverdue, boolean isRepayAll, boolean isLoanRepayOneDay, boolean isWithhold, List<PaymentInstallmentDto> installmentDtos,LoanOrder loanOrder, UserInfo userInfo) {
        RequestPayParam requestPayParam = new RequestPayParam();

        requestPayParam.setBizId(paymentRequestId.toString());
        requestPayParam.setBizType(bizType.getCode());
        requestPayParam.setRequestSource(Constant.APPLICATION_PAYMENT_SOURCE);
        requestPayParam.setPaymentMethod(PaymentMethodEnum.GPAY.getCode());
        boolean isShoppingOrder = loanOrder.getProductCategory() != null && loanOrder.getProductCategory().intValue() == ProductCategoryEnum.PRODUCT_CATEGORY_SHOPPING.getCode().intValue();
        requestPayParam.setRouteStrategy(this.getPayCenterRouteStrategyEnum(isHistoryOverdue, isWithhold, isLoanRepayOneDay, isShoppingOrder).getCode());
        logger.info("用户：{}，是否为商城订单：{}，还款路由：{}", userId, isShoppingOrder, requestPayParam.getRouteStrategy());
        OrderInfo order = new OrderInfo();
        requestPayParam.setExecuteName(bizType.getText());
        requestPayParam.setOerderInfo(order);

        if (userInfo == null){
            requestPayParam.setUserInfo(this.getUserInfo(userId));//查询用户支付的银行卡信息
        }else{
            requestPayParam.setUserInfo(userInfo);//查询用户支付的银行卡信息
        }

        requestPayParam.setWithholdingAmount(Long.parseLong(amount.toString()));
        requestPayParam.setExextData(extData);
        requestPayParam.setTxSerialNo(loanOrder.getBizSeqNo());
        //添加商户号
        requestPayParam.setMerchant(loanOrder.getMerchantNo());

        //查询分期数据
        List<RepaymentRecord> repaymentRecordList = repaymentRecordService.getRepaymentRecordsByPaymentRequestId(paymentRequestId);
        RepayTypeEnum repayTypeEnum = this.getRepayTypeEnum(isHistoryOverdue, isRepayAll);
        for (Iterator<PaymentInstallmentDto> iterator = installmentDtos.iterator(); iterator.hasNext(); ) {
            PaymentInstallmentDto paymentInstallmentDto = iterator.next();
            paymentInstallmentDto.setRepayType(repayTypeEnum.getCode());
        }
//        List<PaymentInstallmentDto> installments = this.getInstallments(installmentDtos, isHistoryOverdue, isRepayAll);
        requestPayParam.setInstallments(installmentDtos);

        //通过封装方法调用注册中心
        String messageBodyJsonStr = FastJson.toJson(requestPayParam);
        mqMessageService.sendMessage(new MqMessage(messageBodyJsonStr,QueueConstants.PAYCENTER_MQNAME));
        logger.info("send pay center request:" + messageBodyJsonStr);
    }


//    @Override
//    @Deprecated
//    public PaymentParamDto payWithH5(String md5salt, Integer userId, Long paymentRequestId, Integer paymentAmount, Integer couponId, Integer couponAmount, String exextData, PaymentCenterBizTypeEnum bizType, String requestSource, Date expiredDate,  LoanOrder loanOrder, List<RepaymentPlan> repaymentPlanList, boolean isHistoryOverdue, boolean isRepayAll, boolean isLoanRepayOneDay) {
//        // md5key
//        // userId
//        // bizId
//        // withholdingAmount
//        // couponId
//        // amount
//        // exextData
//        // bizType
//        // requestSource
//        // expireDate
//        StringBuffer buf = new StringBuffer();
//        buf.append(md5salt);
//        buf.append(userId);
//        buf.append(paymentRequestId.toString());
//        buf.append(paymentAmount.toString());
//        if (!ObjectUtils.isEmpty(couponId)){
//            buf.append(couponId.toString());
//            buf.append(couponAmount.toString());
//        }
////
////        if (!StringUtils.isEmpty(exextData)){
////            buf.append(exextData);
////        }
//
//        buf.append(bizType.getCode());
//        buf.append(requestSource);
//        buf.append(expiredDate.getTime());
////        System.out.println(buf.toString());
//        //添加商户号
//        buf.append(loanOrder.getMerchantNo());
//        logger.info("加密前字符串为："+buf.toString());
//        String sign = Md5Util.md5(buf.toString());
//        logger.info("加密后字符串为："+sign);
//
//        PaymentParamDto paymentParamDto = new PaymentParamDto();
//        paymentParamDto.setBizId(paymentRequestId.toString());
//        paymentParamDto.setWithholdingAmount(paymentAmount.longValue());
//        paymentParamDto.setCouponId(couponId == null ? null : couponId.toString());
//        paymentParamDto.setAmount(couponAmount == null ? null : couponAmount.longValue());
//        paymentParamDto.setExextData(exextData);
//        paymentParamDto.setBizType(bizType.getCode());
//        paymentParamDto.setRequestSource(requestSource);
//        paymentParamDto.setExpireDate(expiredDate);
//        paymentParamDto.setSign(sign);
//        //唯一标识为-bizSeqNo
//        paymentParamDto.setTxSerialNo(loanOrder.getBizSeqNo());
//        paymentParamDto.setInstallments(null, isHistoryOverdue, isRepayAll));
//        boolean isShoppingOrder = loanOrder.getProductCategory() != null && loanOrder.getProductCategory().intValue() == ProductCategoryEnum.PRODUCT_CATEGORY_SHOPPING.getCode().intValue();
//        paymentParamDto.setRouteStrategy(this.getPayCenterRouteStrategyEnum(isHistoryOverdue, false, isLoanRepayOneDay,isShoppingOrder).getCode());
//        logger.info("用户：{}，是否为商城订单：{}，还款路由：{}", userId, isShoppingOrder, paymentParamDto.getRouteStrategy());
//        //添加商户号
//        paymentParamDto.setMerchant(loanOrder.getMerchantNo());
//        return paymentParamDto;
//    }

    /**
     * 支付中心
     * @param
     * @return
     */
    private List<PaymentInstallmentDto> getInstallments(List<PaymentInstallmentDto> installmentDtos, boolean isHistoryOverdue, boolean isRepayAll){

        RepayTypeEnum repayTypeEnum = this.getRepayTypeEnum(isHistoryOverdue, isRepayAll);

        List<PaymentInstallmentDto> paymentInstallmentDtos = new ArrayList<>();
        for (Iterator<PaymentInstallmentDto> iterator = installmentDtos.iterator(); iterator.hasNext(); ) {
            PaymentInstallmentDto next =  iterator.next();
            PaymentInstallmentDto paymentInstallmentDto = new PaymentInstallmentDto();

            // 这里的费用按照匹配到的repayment plan进行封装，
            paymentInstallmentDto.setPeriod(next.getPeriod());
            paymentInstallmentDto.setInstallmentAmount(next.getInstallmentAmount());
            paymentInstallmentDto.setInstallmentPrincipal(next.getInstallmentPrincipal());
            paymentInstallmentDto.setInstallInterest(next.getInstallInterest());
            paymentInstallmentDto.setRepayType(repayTypeEnum.getCode());//this.getRepayType(repaymentPlan)

            paymentInstallmentDtos.add(paymentInstallmentDto);

        }
        return paymentInstallmentDtos;
    }

//
//    private int getRepayType(List<RepaymentPlan> repaymentPlanList){
//        int repayType;
//        if (repaymentPlanList.size() == total){
//            repayType = RepayTypeEnum.PRE_REPAY.getCode();
//        }else{
//            repayType = RepayTypeEnum.NORMAL_REPAY.getCode();
//        }
//        return repayType;
//    }


    private RepayTypeEnum getRepayTypeEnum(boolean isHistoryOverdue, boolean isRepayAll){
        if (isHistoryOverdue){
            return RepayTypeEnum.NORMAL_REPAY;
        }

        //2.如果是全部一次性还清，则是提前还款，否则是正常还款
        if (isRepayAll){
            return RepayTypeEnum.PRE_REPAY;
        }

        return RepayTypeEnum.NORMAL_REPAY;
    }


    private PayCenterRouteStrategyEnum getPayCenterRouteStrategyEnum(boolean isHistoryOverdue, boolean isWithhold, boolean isLoanRepayOneDay, boolean isShoppingOrder){
        if (isShoppingOrder) {
            return PayCenterRouteStrategyEnum.PETTYLOAN_CUSTODY_INITIATIVE;
        }
        if (isHistoryOverdue){
            return PayCenterRouteStrategyEnum.CUSTODY_OVERDUE;
        }

        if (isWithhold){
            return PayCenterRouteStrategyEnum.CUSTODY_TITMING;
        }

        if (isLoanRepayOneDay){
            return PayCenterRouteStrategyEnum.PETTYLOAN_CUSTODY_INITIATIVE;//当天借还临时处理策略
        }

        return PayCenterRouteStrategyEnum.CUSTODY_INITIATIVE;
    }

    private UserInfo getUserInfo(String userId){
        UserInfo userInfo = oldCashmanRemoteService.getUserInfo(Long.parseLong(userId));
        return userInfo;
    }
}
