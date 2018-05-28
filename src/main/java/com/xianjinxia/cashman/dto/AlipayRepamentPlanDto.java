package com.xianjinxia.cashman.dto;

/**
 *
 *
 * @author JaJian
 * @create 2018-05-04 14:07
 **/
public class AlipayRepamentPlanDto {

    private Long id;

    private Integer amount;

    private Integer repaymentTotalAmount;

    private Integer principalAmount;

    private Integer interestAmount;

    private Integer overdueAmount;

    private String repaymentTime;

    private Integer version;

    private Integer status;

    private Boolean isOverdue;

    private Integer overdueDayCount;

    private String remark;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Integer getRepaymentTotalAmount() {
        return repaymentTotalAmount;
    }

    public void setRepaymentTotalAmount(Integer repaymentTotalAmount) {
        this.repaymentTotalAmount = repaymentTotalAmount;
    }

    public Integer getPrincipalAmount() {
        return principalAmount;
    }

    public void setPrincipalAmount(Integer principalAmount) {
        this.principalAmount = principalAmount;
    }

    public Integer getInterestAmount() {
        return interestAmount;
    }

    public void setInterestAmount(Integer interestAmount) {
        this.interestAmount = interestAmount;
    }

    public Integer getOverdueAmount() {
        return overdueAmount;
    }

    public void setOverdueAmount(Integer overdueAmount) {
        this.overdueAmount = overdueAmount;
    }

    public String getRepaymentTime() {
        return repaymentTime;
    }

    public void setRepaymentTime(String repaymentTime) {
        this.repaymentTime = repaymentTime;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
