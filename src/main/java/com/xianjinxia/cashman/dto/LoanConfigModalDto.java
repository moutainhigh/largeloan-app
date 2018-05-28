package com.xianjinxia.cashman.dto;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;

import com.xianjinxia.cashman.request.BaseRequest;

/**
 * 借款选择配置 请求参数
 * 
 * @author liuzhifang
 *
 *         2017年10月11日
 */
public class LoanConfigModalDto extends BaseRequest {

    /**
     * 产品ID
     */
    @NotNull(message = "productId is not null")
    @ApiModelProperty(name = "productId", value = "产品ID", example = "1", required = true,
            dataType = "Long")
    private Long productId;
    /**
     * 借款金额
     */
    @NotNull(message = "orderAmount is not null")
    @ApiModelProperty(name = "orderAmount", value = "借款金额,单位元", example = "5000", required = true,
            dataType = "Double")
    private Double orderAmount;
    /**
     * 借款期限
     */
    @NotNull(message = "periods is not null")
    @ApiModelProperty(name = "periods", value = "借款期限", example = "6", required = true,
            dataType = "int")
    private int periods;

    public LoanConfigModalDto(Long productId, Double orderAmount, int periods) {
        this.productId = productId;
        this.orderAmount = orderAmount;
        this.periods = periods;
    }

    public LoanConfigModalDto() {
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Double getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(Double orderAmount) {
        this.orderAmount = orderAmount;
    }

    public int getPeriods() {
        return periods;
    }

    public void setPeriods(int periods) {
        this.periods = periods;
    }



}
