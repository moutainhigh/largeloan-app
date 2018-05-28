/****************************************
 * Copyright (c) 2017 XinJinXia.
 * All rights reserved.
 * Created on 2017年8月30日
 * 
 * Contributors:
 * tennyqin - initial implementation
 ****************************************/
package com.xianjinxia.cashman.request;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;

/**
 * @title ConfirmLoanReq.java
 *
 * @author fanmaowen
 * @version 1.0
 * @created 2017年8月30日
 */
public class LoanCheckRequest extends BaseRequest {

	@NotNull(message="orderAmount couldn't be null")
	@ApiModelProperty(name = "orderAmount",value = "订单金额",example = "5000",required = true,dataType = "String")
	private BigDecimal orderAmount;

	@NotNull(message="periods couldn't be null")
	@ApiModelProperty(name = "periods",value = "期数",example = "5",required = true,dataType = "String")
	private String periods;

	@NotNull(message="productId couldn't be null")
	@ApiModelProperty(name = "productId",value = "产品ID",example = "1",required = true,dataType = "String")
	private String productId;

	@NotNull(message="userId couldn't be null")
	@ApiModelProperty(name = "userId",value = "用户ID",example = "428",required = true,dataType = "String")
	private Long userId;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public BigDecimal getOrderAmount() {
		return orderAmount;
	}

	public void setOrderAmount(BigDecimal orderAmount) {
		this.orderAmount = orderAmount;
	}

	public String getPeriods() {
		return periods;
	}

	public void setPeriods(String periods) {
		this.periods = periods;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	@Override
	public String toString() {
		return "LoanCheckRequest{" +
				"orderAmount=" + orderAmount +
				", periods='" + periods + '\'' +
				", productId='" + productId + '\'' +
				", userId='" + userId + '\'' +
				'}';
	}
}
