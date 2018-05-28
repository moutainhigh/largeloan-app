package com.xianjinxia.cashman.domain;

import java.util.Date;

public class CashmanRefundLog {
    private Long id;

    private String userPhone;

    private Long loanOrderId;

    private Long repaymentPlanId;

    private Long repaymentRecordId;

    private Integer refundAmt;

    private String refundChannel;

    private String refundOrderNo;

    private Date refundTime;

    private Date createdTime;

    private Date updatedTime;

    private Boolean dataValid;

    private Integer version;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getLoanOrderId() {
        return loanOrderId;
    }

    public void setLoanOrderId(Long loanOrderId) {
        this.loanOrderId = loanOrderId;
    }

    public Long getRepaymentPlanId() {
        return repaymentPlanId;
    }

    public void setRepaymentPlanId(Long repaymentPlanId) {
        this.repaymentPlanId = repaymentPlanId;
    }

    public Long getRepaymentRecordId() {
        return repaymentRecordId;
    }

    public void setRepaymentRecordId(Long repaymentRecordId) {
        this.repaymentRecordId = repaymentRecordId;
    }

    public Integer getRefundAmt() {
        return refundAmt;
    }

    public void setRefundAmt(Integer refundAmt) {
        this.refundAmt = refundAmt;
    }

    public String getRefundChannel() {
        return refundChannel;
    }

    public void setRefundChannel(String refundChannel) {
        this.refundChannel = refundChannel == null ? null : refundChannel.trim();
    }

    public String getRefundOrderNo() {
        return refundOrderNo;
    }

    public void setRefundOrderNo(String refundOrderNo) {
        this.refundOrderNo = refundOrderNo == null ? null : refundOrderNo.trim();
    }

    public Date getRefundTime() {
        return refundTime;
    }

    public void setRefundTime(Date refundTime) {
        this.refundTime = refundTime;
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

    public Boolean getDataValid() {
        return dataValid;
    }

    public void setDataValid(Boolean dataValid) {
        this.dataValid = dataValid;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public CashmanRefundLog() {
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public CashmanRefundLog(String userPhone, Long loanOrderId, Long repaymentPlanId, Long repaymentRecordId, Integer refundAmt, String refundChannel, String refundOrderNo, Date refundTime) {
        this.userPhone = userPhone;
        this.loanOrderId = loanOrderId;
        this.repaymentPlanId = repaymentPlanId;
        this.repaymentRecordId = repaymentRecordId;
        this.refundAmt = refundAmt;
        this.refundChannel = refundChannel;
        this.refundOrderNo = refundOrderNo;
        this.refundTime = refundTime;
        this.createdTime = new Date();
    }
}