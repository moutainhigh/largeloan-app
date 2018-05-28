package com.xianjinxia.cashman.response;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 还款计划
 * 
 * @author liuzhifang
 *
 *         2017年10月11日
 */
public class RepaymentPlanAdvance implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /**
     * 产品ID
     */
    @ApiModelProperty(name = "productId", value = "产品ID", example = "1", required = true,
            dataType = "Long")
    private Long productId;
    /**
     * 第几期
     */
    @ApiModelProperty(name = "periodsNum", value = "第几期", example = "1", required = true,
            dataType = "int")
    private int periodsNum;
    /**
     * 还款日
     */
    @ApiModelProperty(name = "repaymentTime", value = "还款日", example = "1509242923352",
            required = true, dataType = "Date")
    private Date repaymentTime;
    /**
     * 每期应还
     */
    @ApiModelProperty(name = "repaymentEachPeriodAmount", value = "每期应还", example = "1413.33",
            required = true, dataType = "BigDecimal")
    private BigDecimal repaymentEachPeriodAmount;
    /**
     * 利息
     */
    @ApiModelProperty(name = "interest", value = "利息", example = "80", required = true,
            dataType = "BigDecimal")
    private BigDecimal interest;

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public int getPeriodsNum() {
        return periodsNum;
    }

    public void setPeriodsNum(int periodsNum) {
        this.periodsNum = periodsNum;
    }

    public Date getRepaymentTime() {
        return repaymentTime;
    }

    public void setRepaymentTime(Date repaymentTime) {
        this.repaymentTime = repaymentTime;
    }

    public BigDecimal getRepaymentEachPeriodAmount() {
        return repaymentEachPeriodAmount;
    }

    public void setRepaymentEachPeriodAmount(BigDecimal repaymentEachPeriodAmount) {
        this.repaymentEachPeriodAmount = repaymentEachPeriodAmount;
    }

    public BigDecimal getInterest() {
        return interest;
    }

    public void setInterest(BigDecimal interest) {
        this.interest = interest;
    }

    @Override
    public String toString() {
        return "RepaymentPlan [productId=" + productId + ", periodsNum=" + periodsNum
                + ", repaymentTime=" + repaymentTime + ", repaymentEachPeriodAmount="
                + repaymentEachPeriodAmount + ", interest=" + interest + "]";
    }



}
