package com.xianjinxia.cashman.dto;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;


public class ServiceChargeFee implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /**
     * 费用类型
     */
    @ApiModelProperty(name = "feeType", value = "费用类型", example = "consultation_fee",
            required = true, dataType = "String")
    private String feeType;
    /**
     * 费用名
     */
    @ApiModelProperty(name = "feeName", value = "费用名", example = "咨询费", required = true,
            dataType = "String")
    private String feeName;
    /**
     * 费用金额
     */
    @ApiModelProperty(name = "feeMoney", value = "费用金额", example = "200", required = true,
            dataType = "BigDecimal")
    private BigDecimal feeMoney;


    public String getFeeType() {
        return feeType;
    }

    public void setFeeType(String feeType) {
        this.feeType = feeType;
    }

    public String getFeeName() {
        return feeName;
    }

    public void setFeeName(String feeName) {
        this.feeName = feeName;
    }

    public BigDecimal getFeeMoney() {
        return feeMoney;
    }

    public void setFeeMoney(BigDecimal feeMoney) {
        this.feeMoney = feeMoney;
    }

    public ServiceChargeFee() {
    }

    public ServiceChargeFee(String feeType, String feeName, BigDecimal feeMoney) {
        this.feeType = feeType;
        this.feeName = feeName;
        this.feeMoney = feeMoney;
    }
}
