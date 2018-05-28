package com.xianjinxia.cashman.dto;

/**
 * 总还款计划
 * Created by Myron on 2017/12/22.
 */
public class CustodyRepayScheduleInfo {
    // 还款类型
    private Integer repaymentType;
    // 总还款金额
    private Integer repaymentAmount;
    // 最迟还款时间
    private Long repaymentTime;
    // 总还款期数
    private Integer period;
    // 总还款本金
    private Integer repaymentPrincipal;
    // 总还款利息
    private Integer repaymentInterest;

    public Integer getRepaymentType() {
        return repaymentType;
    }

    public void setRepaymentType(Integer repaymentType) {
        this.repaymentType = repaymentType;
    }

    public Integer getRepaymentAmount() {
        return repaymentAmount;
    }

    public void setRepaymentAmount(Integer repaymentAmount) {
        this.repaymentAmount = repaymentAmount;
    }

    public Long getRepaymentTime() {
        return repaymentTime;
    }

    public void setRepaymentTime(Long repaymentTime) {
        this.repaymentTime = repaymentTime;
    }

    public Integer getPeriod() {
        return period;
    }

    public void setPeriod(Integer period) {
        this.period = period;
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

    @Override
    public String toString() {
        return "CustodyRepayScheduleInfo{" +
                "repaymentType=" + repaymentType +
                ", repaymentAmount=" + repaymentAmount +
                ", repaymentTime='" + repaymentTime + '\'' +
                ", period='" + period + '\'' +
                ", repaymentPrincipal=" + repaymentPrincipal +
                ", repaymentInterest=" + repaymentInterest +
                '}';
    }
}
