package com.xianjinxia.cashman.domain;

import java.util.Date;

public class RepaymentRecord {
    private Long id;

    private Long userId;

    private Long repaymentPlanId;

    private Long paymentRequestId;

    private Integer status;

    private Integer amount;

    private String remark;

    private Date createdAt;

    private Date updatedAt;

    private String paymentType;

    private String loanOrderId;

    private Integer repayPrincipalAmt;

    private Integer repayInterestAmt;

    private Integer repayOverdueAmt;

    private Integer refundAmt;

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

    public Long getRepaymentPlanId() {
        return repaymentPlanId;
    }

    public void setRepaymentPlanId(Long repaymentPlanId) {
        this.repaymentPlanId = repaymentPlanId;
    }

    public Long getPaymentRequestId() {
        return paymentRequestId;
    }

    public void setPaymentRequestId(Long paymentRequestId) {
        this.paymentRequestId = paymentRequestId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType == null ? null : paymentType.trim();
    }

    public String getLoanOrderId() {
        return loanOrderId;
    }

    public void setLoanOrderId(String loanOrderId) {
        this.loanOrderId = loanOrderId == null ? null : loanOrderId.trim();
    }

    public Integer getRepayPrincipalAmt() {
        return repayPrincipalAmt;
    }

    public void setRepayPrincipalAmt(Integer repayPrincipalAmt) {
        this.repayPrincipalAmt = repayPrincipalAmt;
    }

    public Integer getRepayInterestAmt() {
        return repayInterestAmt;
    }

    public void setRepayInterestAmt(Integer repayInterestAmt) {
        this.repayInterestAmt = repayInterestAmt;
    }

    public Integer getRepayOverdueAmt() {
        return repayOverdueAmt;
    }

    public void setRepayOverdueAmt(Integer repayOverdueAmt) {
        this.repayOverdueAmt = repayOverdueAmt;
    }

    public Integer getRefundAmt() {
        return refundAmt;
    }

    public void setRefundAmt(Integer refundAmt) {
        this.refundAmt = refundAmt;
    }

    @Override
    public String toString() {
        return "RepaymentRecord{" + "id=" + id + ", userId=" + userId + ", repaymentPlanId=" + repaymentPlanId + ", paymentRequestId=" + paymentRequestId + ", status=" + status + ", amount=" + amount + ", remark='" + remark + '\'' + ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + ", paymentType='" + paymentType + '\'' + ", loanOrderId='" + loanOrderId + '\'' + ", repayPrincipalAmt=" + repayPrincipalAmt + ", repayInterestAmt=" + repayInterestAmt + ", repayOverdueAmt=" + repayOverdueAmt + '}';
    }

    public RepaymentRecord() {
    }

    public RepaymentRecord(Long userId, Long repaymentPlanId, Long paymentRequestId, Integer status, Integer amount, String remark, String paymentType, String loanOrderId, Integer repayPrincipalAmt, Integer repayInterestAmt, Integer repayOverdueAmt) {
        this.userId = userId;
        this.repaymentPlanId = repaymentPlanId;
        this.paymentRequestId = paymentRequestId;
        this.status = status;
        this.amount = amount;
        this.remark = remark;
        this.paymentType = paymentType;
        this.loanOrderId = loanOrderId;
        this.repayPrincipalAmt = repayPrincipalAmt;
        this.repayInterestAmt = repayInterestAmt;
        this.repayOverdueAmt = repayOverdueAmt;
        this.createdAt = new Date();
        this.updatedAt = new Date();
    }
}