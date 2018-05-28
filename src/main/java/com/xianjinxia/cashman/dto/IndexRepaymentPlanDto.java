package com.xianjinxia.cashman.dto;


import java.math.BigDecimal;
import java.util.Date;

public class IndexRepaymentPlanDto {

    private Long loanOrderId;

    private BigDecimal repaymentTotalAmount;

    private Date repaymentPlanTime;

    private Integer status;

    private Boolean isOverdue;

    private Integer overdueDayCount;


    public IndexRepaymentPlanDto() { }

    public IndexRepaymentPlanDto(Long loanOrderId, BigDecimal repaymentTotalAmount,
                                 Date repaymentPlanTime, Integer status,
                                 Boolean isOverdue, Integer overdueDayCount) {
        this.loanOrderId = loanOrderId;
        this.repaymentTotalAmount = repaymentTotalAmount;
        this.repaymentPlanTime = repaymentPlanTime;
        this.status = status;
        this.isOverdue = isOverdue;
        this.overdueDayCount = overdueDayCount;
    }

    public Long getLoanOrderId() {
        return loanOrderId;
    }

    public void setLoanOrderId(Long loanOrderId) {
        this.loanOrderId = loanOrderId;
    }

    public BigDecimal getRepaymentTotalAmount() {
        return repaymentTotalAmount;
    }

    public void setRepaymentTotalAmount(BigDecimal repaymentTotalAmount) {
        this.repaymentTotalAmount = repaymentTotalAmount;
    }

    public Date getRepaymentPlanTime() {
        return repaymentPlanTime;
    }

    public void setRepaymentPlanTime(Date repaymentPlanTime) {
        this.repaymentPlanTime = repaymentPlanTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Boolean getOverdue() {
        return isOverdue;
    }

    public void setOverdue(Boolean overdue) {
        isOverdue = overdue;
    }

    public Integer getOverdueDayCount() {
        return overdueDayCount;
    }

    public void setOverdueDayCount(Integer overdueDayCount) {
        this.overdueDayCount = overdueDayCount;
    }
}