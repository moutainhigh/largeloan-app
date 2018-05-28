package com.xianjinxia.cashman.request;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import java.util.List;

public class LoanCalculateRequest extends BaseRequest {

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
    @ApiModelProperty(name = "orderAmount", value = "借款金额", example = "5000", required = true,
            dataType = "Integer")
    private List<Integer> orderAmountList;
    /**
     * 借款期限
     */
    @NotNull(message = "periods is not null")
    @ApiModelProperty(name = "periods", value = "借款期限", example = "6", required = true,
            dataType = "int")
    private int periods;


    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public List<Integer> getOrderAmountList() {
        return orderAmountList;
    }

    public void setOrderAmountList(List<Integer> orderAmountList) {
        this.orderAmountList = orderAmountList;
    }

    public int getPeriods() {
        return periods;
    }

    public void setPeriods(int periods) {
        this.periods = periods;
    }
}
