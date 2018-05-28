package com.xianjinxia.cashman.schedule.dto;

import com.xianjinxia.cashman.constants.Constant;
import com.xianjinxia.cashman.enums.LoanOrderTypeEnum;
import com.xianjinxia.cashman.enums.RepaymentBadLevelEnum;
import com.xianjinxia.cashman.enums.RepaymentPlanStatusEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;
@ApiModel
public class RepaymentPlanDTO extends BaseDTO{

    public RepaymentPlanDTO() { }

    /** 构造函数用于生产还款明细的时候使用 */
    public RepaymentPlanDTO(Date repaymentPlanTime,Integer period) {
        this.period = period;
        this.repaymentPlanTime = repaymentPlanTime;
        this.isOverdue=false;
        this.badLevel= RepaymentBadLevelEnum.ZERO.getCode();
        this.orderType= LoanOrderTypeEnum.BIG.getCode();
        this.repaymentIncomeAmount=Constant.ZERO;
        this.repaymentWaitingAmount=Constant.ZERO;
        this.lateDay=Constant.ZERO;
        this.autoDebitFailTimes=Constant.ZERO;
        this.renewalCount=Constant.ZERO;
        this.status= RepaymentPlanStatusEnum.Waiting.getCode();
    }
    @ApiModelProperty(example = "14545",value = "还款计划主键")
    private Long id;
    @ApiModelProperty(example = "2",value = "产品id")
    private Long productId;

    private Integer orderType;
    @ApiModelProperty(example = "527",value = "用户id")
    private Long userId;
    @ApiModelProperty(example = "63",value = "用户id")
    private Long loanOrderId;
    @ApiModelProperty(example = "100000",value = "待还款总金额")
    private Integer repaymentTotalAmount;
    @ApiModelProperty(example = "85000",value = "已入账还款金额")
    private Integer repaymentIncomeAmount;
    @ApiModelProperty(example = "85000",value = "在途金额, 详情表可查")
    private Integer repaymentWaitingAmount;
    @ApiModelProperty(example = "85000",value = "本金")
    private Integer repaymentPrincipal;
    @ApiModelProperty(example = "85000",value = "利息")
    private Integer repaymentInterest;
    @ApiModelProperty(example = "6",value = "期数")
    private Integer period;
    @ApiModelProperty(example = "1545454",value = "计划还款时间")
    private Date repaymentPlanTime;
    @ApiModelProperty(example = "1545454",value = "实际还款时间")
    private Date repaymentRealTime;
    @ApiModelProperty(example = "1545454",value = "状态 10 未还款 30 已还款")
    private Integer status;
    @ApiModelProperty(example = "1000",value = "滞纳金")
    private Integer lateFee;
    @ApiModelProperty(example = "3",value = "滞纳天数")
    private Integer lateDay;
    @ApiModelProperty(example = "3",value = "自动扣款失败次数")
    private Integer autoDebitFailTimes;
    @ApiModelProperty(example = "3",value = "续期次数")
    private Integer renewalCount;
    @ApiModelProperty(example = "false",value = "是否已催收 ")
    private Boolean isCollection;
    @ApiModelProperty(example = "false",value = "是否已已逾期")
    private Boolean isOverdue;
    @ApiModelProperty(example = "0",value = "坏账级别")
    private Integer badLevel;
    @ApiModelProperty(example = "111145470",value = "创建时间")
    private Date createdTime;
    @ApiModelProperty(example = "111145470",value = "更新时间")
    private Date updatedTime;
    @ApiModelProperty(example = "备注",value = "备注")
    private String remark;

    private Boolean dataValid;

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

    public Integer getRepaymentPrincipal() {
        return repaymentPrincipal;
    }

    public void setRepaymentPrincipal(Integer repaymentPrincipal) {
        this.repaymentPrincipal = repaymentPrincipal;
    }

    public Integer getRepaymentInterest() {
        return repaymentInterest;
    }

    public void setRepaymentInterest(Integer repaymentInterest) {
        this.repaymentInterest = repaymentInterest;
    }

    public Integer getPeriod() {
        return period;
    }

    public void setPeriod(Integer period) {
        this.period = period;
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }



    public Integer getLateDay() {
        return lateDay;
    }

    public void setLateDay(Integer lateDay) {
        this.lateDay = lateDay;
    }

    public Integer getAutoDebitFailTimes() {
        return autoDebitFailTimes;
    }

    public void setAutoDebitFailTimes(Integer autoDebitFailTimes) {
        this.autoDebitFailTimes = autoDebitFailTimes;
    }

    public Integer getRenewalCount() {
        return renewalCount;
    }

    public void setRenewalCount(Integer renewalCount) {
        this.renewalCount = renewalCount;
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

    public Integer getOrderType() {
        return orderType;
    }

    public void setOrderType(Integer orderType) {
        this.orderType = orderType;
    }

    public Integer getLateFee() {
        return lateFee;
    }

    public void setLateFee(Integer lateFee) {
        this.lateFee = lateFee;
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

    public Integer getBadLevel() {
        return badLevel;
    }

    public void setBadLevel(Integer badLevel) {
        this.badLevel = badLevel;
    }

    public Integer getRepaymentTotalAmount() {
        return repaymentTotalAmount;
    }

    public void setRepaymentTotalAmount(Integer repaymentTotalAmount) {
        this.repaymentTotalAmount = repaymentTotalAmount;
    }

    @Override
    public String toString() {
        return "RepaymentPlan{" +
                "id=" + id +
                ", productId=" + productId +
                ", orderType=" + orderType +
                ", userId=" + userId +
                ", loanOrderId=" + loanOrderId +
                ", repaymentTotalAmount=" + repaymentTotalAmount +
                ", repaymentIncomeAmount=" + repaymentIncomeAmount +
                ", repaymentWaitingAmount=" + repaymentWaitingAmount +
                ", repaymentPrincipal=" + repaymentPrincipal +
                ", repaymentInterest=" + repaymentInterest +
                ", period=" + period +
                ", repaymentPlanTime=" + repaymentPlanTime +
                ", repaymentRealTime=" + repaymentRealTime +
                ", status=" + status +
                ", lateFee=" + lateFee +
                ", lateDay=" + lateDay +
                ", autoDebitFailTimes=" + autoDebitFailTimes +
                ", renewalCount=" + renewalCount +
                ", isCollection=" + isCollection +
                ", isOverdue=" + isOverdue +
                ", badLevel=" + badLevel +
                ", createdTime=" + createdTime +
                ", updatedTime=" + updatedTime +
                ", remark='" + remark + '\'' +
                ", dataValid=" + dataValid +
                '}';
    }
}