package com.xianjinxia.cashman.domain;

import java.util.Date;

public class FeeDetail {
    private Long id;

    private String feeType;

    private Integer feeAmount;

    private Long trdLoanOrderId;

    private String createdUser;

    private Date createdTime;

    private Date updatedTime;

    public FeeDetail(){}

    public FeeDetail(String feeType, Integer feeAmount, Long trdLoanOrderId) {
        this.feeType = feeType;
        this.feeAmount = feeAmount;
        this.trdLoanOrderId = trdLoanOrderId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFeeType() {
        return feeType;
    }

    public void setFeeType(String feeType) {
        this.feeType = feeType == null ? null : feeType.trim();
    }

    public Integer getFeeAmount() {
        return feeAmount;
    }

    public void setFeeAmount(Integer feeAmount) {
        this.feeAmount = feeAmount;
    }

    public Long getTrdLoanOrderId() {
        return trdLoanOrderId;
    }

    public void setTrdLoanOrderId(Long trdLoanOrderId) {
        this.trdLoanOrderId = trdLoanOrderId;
    }

    public String getCreatedUser() {
        return createdUser;
    }

    public void setCreatedUser(String createdUser) {
        this.createdUser = createdUser == null ? null : createdUser.trim();
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public Date getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }
}