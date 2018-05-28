package com.xianjinxia.cashman.dto;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by liquan on 2018/5/14.
 *
 * @Author: liquan
 * @Description: 统计某天应还的订单信息Dto
 * @Date: Created in 15:04 2018/5/14
 * @Modified By:
 */
public class StatisticRepayOrderDto {
    /**
     * 应还本金--总和
     */
    private BigDecimal repayPricipleAmount;
    /**
     * 应还订单总数
     */
    private Integer orderCount;
    /**
     * 计划应还日期
     */
    private String repaymentPlanTime;

    @Override
    public String toString() {
        return "StatisticRepayOrderDto{" +
                "repayPricipleAmount=" + repayPricipleAmount +
                ", orderCount=" + orderCount +
                ", repaymentPlanTime=" + repaymentPlanTime +
                '}';
    }

    public StatisticRepayOrderDto() {
    }

    public StatisticRepayOrderDto(BigDecimal repayPricipleAmount, Integer orderCount, String repaymentPlanTime) {
        this.repayPricipleAmount = repayPricipleAmount;
        this.orderCount = orderCount;
        this.repaymentPlanTime = repaymentPlanTime;
    }

    public BigDecimal getRepayPricipleAmount() {
        return repayPricipleAmount;
    }

    public void setRepayPricipleAmount(BigDecimal repayPricipleAmount) {
        this.repayPricipleAmount = repayPricipleAmount;
    }

    public Integer getOrderCount() {
        return orderCount;
    }

    public void setOrderCount(Integer orderCount) {
        this.orderCount = orderCount;
    }

    public String getRepaymentPlanTime() {
        return repaymentPlanTime;
    }

    public void setRepaymentPlanTime(String repaymentPlanTime) {
        this.repaymentPlanTime = repaymentPlanTime;
    }
}
