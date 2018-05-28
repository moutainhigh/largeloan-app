package com.xianjinxia.cashman.service.repay;

import com.xianjinxia.cashman.domain.PaymentRequest;
import com.xianjinxia.cashman.enums.PaymentBizTypeEnum;
import com.xianjinxia.cashman.enums.PaymentChannelEnum;
import com.xianjinxia.cashman.enums.PaymentRequestStatusEnum;

import java.util.Date;

public interface IPaymentRequestService {

    PaymentRequest createPaymentRequest(Long userId, Integer amount, PaymentRequestStatusEnum paymentRequestStatusEnum, PaymentBizTypeEnum paymentBizTypeEnum);

    PaymentRequest createPaymentRequestByCmsIncome(Long userId, Integer amount, PaymentRequestStatusEnum paymentRequestStatusEnum, PaymentBizTypeEnum paymentBizTypeEnum, String thirdOrderNo, String incomeAccount, Date respTime);

    /**
     *  创建支付请求(目前用于催收减免)
     *  @param userId 用户编号
     *  @param amount 支付金额
     *  @param paymentRequestStatusEnum 支付状态
     *  @param paymentBizTypeEnum 支付类型
     *  @param paymentChannelEnum 支付通道
     *  @return 支付请求对象
     * */
    PaymentRequest createPaymentRequest(Long userId, Integer amount, PaymentRequestStatusEnum paymentRequestStatusEnum, PaymentBizTypeEnum paymentBizTypeEnum, PaymentChannelEnum paymentChannelEnum);

    PaymentRequest getPaymentRequest(Long paymentRequestId);

    PaymentRequest getPaymentRequestByUserIdAndStatus(Long userId, PaymentRequestStatusEnum paymentRequestStatusEnum);

    //    void updatePaymentRequestStatusToFreeze(Long paymentRequestId, Date expiredTime);

    void updatePaymentRequestStatusToSuccess(Long id, String respOrderId, String respMsg,String paymentChannel,String thirdOrderNo);

    void updatePaymentRequestStatusToFailure(Long id, String respOrderId, String respMsg,String paymentChannel,String thirdOrderNo);

    void updatePaymentRequestStatusToCancel(Long id);


    int countByUserIdAndStatus(Long userId, PaymentRequestStatusEnum paymentRequestStatusEnum);

    PaymentRequest getPaymentRequestByRespOrderId(String thirdOrderNo);
}
