/****************************************
 * Copyright (c) 2017 XinJinXia.
 * All rights reserved.
 * Created on 2017年8月30日
 * 
 * Contributors:
 * tennyqin - initial implementation
 ****************************************/
package com.xianjinxia.cashman.response;


public class RepaymentResponse {

	//订单金额
	private String orderAmount;

	//借款服务费
	private String feeAmount;

	//实际到账金额
	private String paymentAmount;

	//应还金额
	private String repaymentAmount;

	public String getOrderAmount() {
		return orderAmount;
	}

	public void setOrderAmount(String orderAmount) {
		this.orderAmount = orderAmount;
	}

	public String getFeeAmount() {
		return feeAmount;
	}

	public void setFeeAmount(String feeAmount) {
		this.feeAmount = feeAmount;
	}

	public String getPaymentAmount() {
		return paymentAmount;
	}

	public void setPaymentAmount(String paymentAmount) {
		this.paymentAmount = paymentAmount;
	}

	public String getRepaymentAmount() {
		return repaymentAmount;
	}

	public void setRepaymentAmount(String repaymentAmount) {
		this.repaymentAmount = repaymentAmount;
	}
}
