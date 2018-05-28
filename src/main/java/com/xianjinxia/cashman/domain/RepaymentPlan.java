package com.xianjinxia.cashman.domain;

import com.xianjinxia.cashman.constants.Constant;
import com.xianjinxia.cashman.enums.DataValidEnum;
import com.xianjinxia.cashman.enums.LoanOrderTypeEnum;
import com.xianjinxia.cashman.enums.RepaymentBadLevelEnum;
import com.xianjinxia.cashman.enums.RepaymentPlanStatusEnum;

import java.util.Date;

public class RepaymentPlan {

    public RepaymentPlan(){}

    /**
     * 构造函数用于生产还款明细的时候使用
     */
    public RepaymentPlan(Date repaymentPlanTime, Integer period) {
        this.period = period;
        this.repaymentPlanTime = repaymentPlanTime;
        this.isOverdue = false;
        this.badLevel = RepaymentBadLevelEnum.ZERO.getCode();
        this.orderType = LoanOrderTypeEnum.BIG.getCode();
        this.repaymentIncomeAmount = Constant.ZERO;
        this.repaymentWaitingAmount = Constant.ZERO;
        this.overdueDayCount = Constant.ZERO;
        this.overdueFeeAmount = Constant.ZERO;
        this.renewalCount = Constant.ZERO;
        this.version = Constant.ZERO;
        this.dataValid= DataValidEnum.DATA_VALID_YES.getCode();
        this.status = RepaymentPlanStatusEnum.Waiting.getCode();
    }

    private Long id;

    private Long productId;

    private Integer orderType;

    private Long userId;

    private Long loanOrderId;

    private Integer repaymentTotalAmount;

    private Integer repaymentOriginAmount;

    private Integer repaymentOriginPrincipalAmount;

    private Integer repaymentOriginInterestAmount;

    private Integer repaymentIncomeAmount;

    private Integer repaymentWaitingAmount;

    private Integer repaymentPrincipalAmount;

    private Integer repaymentInterestAmount;

    private Date repaymentPlanTime;

    private Date repaymentRealTime;

    private Integer period;

    private Integer status;

    private Boolean isCollection;

    private Boolean isOverdue;

    private Integer overdueFeeAmount;

    private Integer overdueDayCount;

    //新增1个字段
    private Date overdueCalTime;

    private String operationFlag;

    private Integer renewalCount;

    private Integer badLevel;

    private Date createdTime;

    private Date updatedTime;

    private String remark;

    private Boolean dataValid;

    private Integer version;
;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Integer getOrderType() {
        return orderType;
    }

    public void setOrderType(Integer orderType) {
        this.orderType = orderType;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getLoanOrderId() {
        return loanOrderId;
    }

    public void setLoanOrderId(Long loanOrderId) {
        this.loanOrderId = loanOrderId;
    }

    public Integer getRepaymentTotalAmount() {
        return repaymentTotalAmount;
    }

    public void setRepaymentTotalAmount(Integer repaymentTotalAmount) {
        this.repaymentTotalAmount = repaymentTotalAmount;
    }

    public Integer getRepaymentOriginAmount() {
        return repaymentOriginAmount;
    }

    public void setRepaymentOriginAmount(Integer repaymentOriginAmount) {
        this.repaymentOriginAmount = repaymentOriginAmount;
    }

    public Integer getRepaymentIncomeAmount() {
        return repaymentIncomeAmount;
    }

    public void setRepaymentIncomeAmount(Integer repaymentIncomeAmount) {
        this.repaymentIncomeAmount = repaymentIncomeAmount;
    }

    public Integer getRepaymentWaitingAmount() {
        return repaymentWaitingAmount;
    }

    public void setRepaymentWaitingAmount(Integer repaymentWaitingAmount) {
        this.repaymentWaitingAmount = repaymentWaitingAmount;
    }

    public Integer getRepaymentPrincipalAmount() {
        return repaymentPrincipalAmount;
    }

    public void setRepaymentPrincipalAmount(Integer repaymentPrincipalAmount) {
        this.repaymentPrincipalAmount = repaymentPrincipalAmount;
    }

    public Integer getRepaymentInterestAmount() {
        return repaymentInterestAmount;
    }

    public void setRepaymentInterestAmount(Integer repaymentInterestAmount) {
        this.repaymentInterestAmount = repaymentInterestAmount;
    }

    public Date getRepaymentPlanTime() {
        return repaymentPlanTime;
    }

    public void setRepaymentPlanTime(Date repaymentPlanTime) {
        this.repaymentPlanTime = repaymentPlanTime;
    }

    public Date getRepaymentRealTime() {
        return repaymentRealTime;
    }

    public void setRepaymentRealTime(Date repaymentRealTime) {
        this.repaymentRealTime = repaymentRealTime;
    }

    public Integer getPeriod() {
        return period;
    }

    public void setPeriod(Integer period) {
        this.period = period;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Boolean getIsCollection() {
        return isCollection;
    }

    public void setIsCollection(Boolean isCollection) {
        this.isCollection = isCollection;
    }

    public Boolean getIsOverdue() {
        return isOverdue;
    }

    public void setIsOverdue(Boolean isOverdue) {
        this.isOverdue = isOverdue;
    }

    public Integer getOverdueFeeAmount() {
        return overdueFeeAmount;
    }

    public void setOverdueFeeAmount(Integer overdueFeeAmount) {
        this.overdueFeeAmount = overdueFeeAmount;
    }

    public Integer getOverdueDayCount() {
        return overdueDayCount;
    }

    public void setOverdueDayCount(Integer overdueDayCount) {
        this.overdueDayCount = overdueDayCount;
    }

    public String getOperationFlag() {
        return operationFlag;
    }

    public void setOperationFlag(String operationFlag) {
        this.operationFlag = operationFlag;
    }

    public Integer getRenewalCount() {
        return renewalCount;
    }

    public void setRenewalCount(Integer renewalCount) {
        this.renewalCount = renewalCount;
    }

    public Integer getBadLevel() {
        return badLevel;
    }

    public void setBadLevel(Integer badLevel) {
        this.badLevel = badLevel;
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
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

    public Boolean getCollection() {
        return isCollection;
    }

    public void setCollection(Boolean collection) {
        isCollection = collection;
    }

    public Boolean getOverdue() {
        return isOverdue;
    }

    public void setOverdue(Boolean overdue) {
        isOverdue = overdue;
    }

    public Date getOverdueCalTime() {
        return overdueCalTime;
    }

    public void setOverdueCalTime(Date overdueCalTime) {
        this.overdueCalTime = overdueCalTime;
    }

    public Integer getRepaymentOriginPrincipalAmount() {
        return repaymentOriginPrincipalAmount;
    }

    public void setRepaymentOriginPrincipalAmount(Integer repaymentOriginPrincipalAmount) {
        this.repaymentOriginPrincipalAmount = repaymentOriginPrincipalAmount;
    }

    public Integer getRepaymentOriginInterestAmount() {
        return repaymentOriginInterestAmount;
    }

    public void setRepaymentOriginInterestAmount(Integer repaymentOriginInterestAmount) {
        this.repaymentOriginInterestAmount = repaymentOriginInterestAmount;
    }

    @Override
    public String toString() {
        return "RepaymentPlan{" + "id=" + id + ", productId=" + productId + ", orderType=" + orderType + ", userId=" + userId + ", loanOrderId=" + loanOrderId + ", repaymentTotalAmount=" + repaymentTotalAmount + ", repaymentOriginAmount=" + repaymentOriginAmount + ", repaymentOriginPrincipalAmount=" + repaymentOriginPrincipalAmount + ", repaymentOriginInterestAmount=" + repaymentOriginInterestAmount + ", repaymentIncomeAmount=" + repaymentIncomeAmount + ", repaymentWaitingAmount=" + repaymentWaitingAmount + ", repaymentPrincipalAmount=" + repaymentPrincipalAmount + ", repaymentInterestAmount=" + repaymentInterestAmount + ", repaymentPlanTime=" + repaymentPlanTime + ", repaymentRealTime=" + repaymentRealTime + ", period=" + period + ", status=" + status + ", isCollection=" + isCollection + ", isOverdue=" + isOverdue + ", overdueFeeAmount=" + overdueFeeAmount + ", overdueDayCount=" + overdueDayCount + ", overdueCalTime=" + overdueCalTime + ", operationFlag='" + operationFlag + '\'' + ", renewalCount=" + renewalCount + ", badLevel=" + badLevel + ", createdTime=" + createdTime + ", updatedTime=" + updatedTime + ", remark='" + remark + '\'' + ", dataValid=" + dataValid + ", version=" + version + '}';
    }
}