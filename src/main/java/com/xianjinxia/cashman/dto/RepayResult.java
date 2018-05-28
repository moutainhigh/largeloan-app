package com.xianjinxia.cashman.dto;

import com.xianjinxia.cashman.enums.PaymentBizTypeEnum;

public class RepayResult {
    private String		userId;
    private Integer		amount;
    private Long	paymentRequestId;
    private PaymentBizTypeEnum paymentBizTypeEnum;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Long getPaymentRequestId() {
        return paymentRequestId;
    }

    public void setPaymentRequestId(Long paymentRequestId) {
        this.paymentRequestId = paymentRequestId;
    }

    public PaymentBizTypeEnum getPaymentBizTypeEnum() {
        return paymentBizTypeEnum;
    }

    public void setPaymentBizTypeEnum(PaymentBizTypeEnum paymentBizTypeEnum) {
        this.paymentBizTypeEnum = paymentBizTypeEnum;
    }
}
