package com.xianjinxia.cashman.response;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import com.xianjinxia.cashman.dto.ServiceChargeFee;

/**
 * 借款选择配置（费用说明信息）
 * 
 * @author liuzhifang
 *
 *         2017年10月11日
 */
public class LoanFeeDetails implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /**
     * 借款金额
     */
    @ApiModelProperty(name = "orderAmount", value = "借款金额", example = "8000", required = true,
            dataType = "Double")
    private Double orderAmount;
    /**
     * 产品ID
     */
    @ApiModelProperty(name = "productId", value = "产品ID", example = "1", required = true,
            dataType = "Long")
    private Long productId;
    /**
     * 借款期限
     */
    @ApiModelProperty(name = "periods", value = "借款期限", example = "6", required = true,
            dataType = "int")
    private int periods;
    /**
     * 服务费
     */
    @ApiModelProperty(name = "feeAmount", value = "服务费", example = "1000", required = true,
            dataType = "BigDecimal")
    private BigDecimal feeAmount;
    /**
     * 实际到账金额
     */
    @ApiModelProperty(name = "paymentAmount", value = "实际到账金额", example = "7000", required = true,
            dataType = "BigDecimal")
    private BigDecimal paymentAmount;
    /**
     * 总计应还
     */
    @ApiModelProperty(name = "repaymentAmount", value = "总计应还", example = "8480", required = true,
            dataType = "BigDecimal")
    private BigDecimal repaymentAmount;
    /**
     * 每期应还
     */
    @ApiModelProperty(name = "repaymentEachPeriodAmount", value = "每期应还", example = "1413.33",
            required = true, dataType = "BigDecimal")
    private BigDecimal repaymentEachPeriodAmount;

    private BigDecimal feeRate;
    /**
     * 费用明细
     * 
     */
    @ApiModelProperty(name = "serviceChargeFeeList", value = "费用明细", example = "", required = true,
            dataType = "List<ServiceChargeFee>")
    private List<ServiceChargeFee> serviceChargeFeeList;
    /**
     * 总利息
     */
    private BigDecimal interestAmount;

    public Double getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(Double orderAmount) {
        this.orderAmount = orderAmount;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public int getPeriods() {
        return periods;
    }

    public void setPeriods(int periods) {
        this.periods = periods;
    }

    public BigDecimal getFeeAmount() {
        return feeAmount;
    }

    public void setFeeAmount(BigDecimal feeAmount) {
        this.feeAmount = feeAmount;
    }

    public BigDecimal getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(BigDecimal paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public BigDecimal getRepaymentAmount() {
        return repaymentAmount;
    }

    public void setRepaymentAmount(BigDecimal repaymentAmount) {
        this.repaymentAmount = repaymentAmount;
    }

    public BigDecimal getRepaymentEachPeriodAmount() {
        return repaymentEachPeriodAmount;
    }

    public void setRepaymentEachPeriodAmount(BigDecimal repaymentEachPeriodAmount) {
        this.repaymentEachPeriodAmount = repaymentEachPeriodAmount;
    }

    public List<ServiceChargeFee> getServiceChargeFeeList() {
        return serviceChargeFeeList;
    }

    public void setServiceChargeFeeList(List<ServiceChargeFee> serviceChargeFeeList) {
        this.serviceChargeFeeList = serviceChargeFeeList;
    }

    public BigDecimal getInterestAmount() {
        return interestAmount;
    }

    public void setInterestAmount(BigDecimal interestAmount) {
        this.interestAmount = interestAmount;
    }

    public BigDecimal getFeeRate() {
        return feeRate;
    }

    public void setFeeRate(BigDecimal feeRate) {
        this.feeRate = feeRate;
    }
}
