package com.xianjinxia.cashman.dto;

public class SyncLoanOrderDto {

    private Long loanOrderId;

    private String status;

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

    public SyncLoanOrderDto(Long loanOrderId, String status, Integer productCategory) {
        this.loanOrderId = loanOrderId;
        this.status = status;
        this.productCategory = productCategory;
    }

    public SyncLoanOrderDto() {
    }

    @Override
    public String toString() {
        return "SyncLoanOrderDto{" +
                "loanOrderId=" + loanOrderId +
                ", status='" + status + '\'' +
                ", productCategory=" + productCategory +
                '}';
    }
}
