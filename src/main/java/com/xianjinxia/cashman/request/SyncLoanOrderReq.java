package com.xianjinxia.cashman.request;

import com.xianjinxia.cashman.idempotent.IdempotentKey;

import javax.validation.constraints.NotNull;

public class SyncLoanOrderReq extends BaseRequest {

    @IdempotentKey(order=1)
    @NotNull(message="loanOrderId could not be null")
    private Long loanOrderId;

    @IdempotentKey(order=2)
    @NotNull(message="status could not be null")
    private String status;

    @IdempotentKey(order=3)
    @NotNull(message="product category could not be null")
    private Integer productCategory;


    public Long getLoanOrderId() {
        return loanOrderId;
    }

    public void setLoanOrderId(Long loanOrderId) {
        this.loanOrderId = loanOrderId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(Integer productCategory) {
        this.productCategory = productCategory;
    }


    @Override
    public String toString() {
        return "SyncLoanOrderReq{" + "loanOrderId=" + loanOrderId + ", status='" + status + '\'' + ", productCategory=" + productCategory + '}';
    }
}
