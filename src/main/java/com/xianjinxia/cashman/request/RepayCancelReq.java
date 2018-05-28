package com.xianjinxia.cashman.request;

import javax.validation.constraints.NotNull;

public class RepayCancelReq extends BaseRequest {

    @NotNull(message="paymentRequestId couldn't be null")
    private Long paymentRequestId;

    public Long getPaymentRequestId() {
        return paymentRequestId;
    }

    public void setPaymentRequestId(Long paymentRequestId) {
        this.paymentRequestId = paymentRequestId;
    }
}
