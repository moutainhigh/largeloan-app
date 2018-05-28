package com.xianjinxia.cashman.dto;

import java.util.Date;

/**
 * Created by liquan on 2017/11/25.
 */
public class LoanOrderContractDto {
    /**
     * trdLoanOrderId
     */
    private Long id;

    private Long userId;

    private Integer orderAmount;

    private Integer feeAmount;

    private Integer periods;

    private Long productId;

    private Integer interestAmount;

    private Date createdTime;

    private String status;

    private String loanUsage;

    private String source;

    private String merchantNo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(Integer orderAmount) {
        this.orderAmount = orderAmount;
    }

    public Integer getFeeAmount() {
        return feeAmount;
    }

    public void setFeeAmount(Integer feeAmount) {
        this.feeAmount = feeAmount;
    }

    public Integer getPeriods() {
        return periods;
    }

    public void setPeriods(Integer periods) {
        this.periods = periods;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Integer getInterestAmount() {
        return interestAmount;
    }

    public void setInterestAmount(Integer interestAmount) {
        this.interestAmount = interestAmount;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLoanUsage() {
        return loanUsage;
    }

    public void setLoanUsage(String loanUsage) {
        this.loanUsage = loanUsage;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getMerchantNo() {
        return merchantNo;
    }

    public void setMerchantNo(String merchantNo) {
        this.merchantNo = merchantNo;
    }

    @Override
    public String toString(){
        return "LoanOrderContractDto{" +
                "id = " + id
                +",userId = "+userId
                +",orderAmount = "+orderAmount+
                "feeAmount = " + feeAmount
                +",periods = "+periods
                +",interestAmount = "+interestAmount
                +",productId = "+productId
                +",createdTime = "+createdTime
                +",status = "+status
                +",loanUsage = "+loanUsage
                +",source = "+source
                +",merchantNo = "+merchantNo
                +'}';
    }
}
