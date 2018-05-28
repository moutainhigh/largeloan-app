package com.xianjinxia.cashman.request;

import com.xianjinxia.cashman.idempotent.IdempotentKey;

import javax.validation.constraints.NotNull;

public class BeforeHandPayReq extends BaseRequest {

    @NotNull(message="orderNo cannot be null")
    @IdempotentKey(order=1)
    private String orderNo;

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    @Override
    public String toString() {
        return "BeforeHandPayReq{" +
                "orderNo='" + orderNo + '\'' +
                '}';
    }
}
