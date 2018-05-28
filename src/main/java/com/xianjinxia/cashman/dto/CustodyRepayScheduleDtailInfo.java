package com.xianjinxia.cashman.dto;

import java.math.BigDecimal;

/**
 * 还款计划明细
 * Created by Myron on 2017/12/22.
 */
public class CustodyRepayScheduleDtailInfo {
    // 还款期数
    private String period;
    // 预期还款金额
    private Integer planRepaymentAmount;
    // 预期还款时间
    private Long planRepaymentTime;
    // 预期还款本金
    private Integer planRepaymentPrincipal;
    // 预期还款利息
    private Integer planRepaymentInterest;
    // 年利率
    private BigDecimal apr;

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public Integer getPlanRepaymentAmount() {
        return planRepaymentAmount;
    }

    public void setPlanRepaymentAmount(Integer planRepaymentAmount) {
        this.planRepaymentAmount = planRepaymentAmount;
    }

    public Long getPlanRepaymentTime() {
        return planRepaymentTime;
    }

    public void setPlanRepaymentTime(Long planRepaymentTime) {
        this.planRepaymentTime = planRepaymentTime;
    }

    public Integer getPlanRepaymentPrincipal() {
        return planRepaymentPrincipal;
    }

    public void setPlanRepaymentPrincipal(Integer planRepaymentPrincipal) {
        this.planRepaymentPrincipal = planRepaymentPrincipal;
    }

    public Integer getPlanRepaymentInterest() {
        return planRepaymentInterest;
    }

    public void setPlanRepaymentInterest(Integer planRepaymentInterest) {
        this.planRepaymentInterest = planRepaymentInterest;
    }

    public BigDecimal getApr() {
        return apr;
    }

    public void setApr(BigDecimal apr) {
        this.apr = apr;
    }

    @Override
    public String toString() {
        return "CustodyRepayScheduleDtailInfo{" +
                "period='" + period + '\'' +
                ", planRepaymentAmount=" + planRepaymentAmount +
                ", planRepaymentTime=" + planRepaymentTime +
                ", planRepaymentPrincipal=" + planRepaymentPrincipal +
                ", planRepaymentInterest=" + planRepaymentInterest +
                ", apr=" + apr +
                '}';
    }

    public CustodyRepayScheduleDtailInfo() {
    }

    public CustodyRepayScheduleDtailInfo(BigDecimal apr) {
        this.apr = apr;
    }
}

