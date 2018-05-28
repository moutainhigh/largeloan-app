package com.xianjinxia.cashman.request;

import javax.validation.constraints.NotNull;

public class RepayFreezeReq  extends BaseRequest {

    @NotNull(message="payment center received param(biz id), as paymentRequestId")
    private Long bizId;

    public Long getBizId() {
        return bizId;
    }

    public void setBizId(Long bizId) {
        this.bizId = bizId;
    }
}