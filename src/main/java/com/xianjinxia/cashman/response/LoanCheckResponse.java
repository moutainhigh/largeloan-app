/****************************************
 * Copyright (c) 2017 XinJinXia.
 * All rights reserved.
 * Created on 2017年8月30日
 * 
 * Contributors:
 * tennyqin - initial implementation
 ****************************************/
package com.xianjinxia.cashman.response;

import com.xianjinxia.cashman.enums.LoanCodeMsgEnum;
import com.xianjinxia.cashman.dto.ContractDto;

import java.math.BigDecimal;
import java.util.List;

/**
 * @title ConfirmLoanReq.java
 *
 * @author fanmaowen
 * @version 1.0
 * @created 2017年8月30日
 */
public class LoanCheckResponse extends LoanBaseResponse{

	//订单金额
	private BigDecimal orderAmount;

	//借款服务费
	private BigDecimal feeAmount;

	//实际到账金额
	private BigDecimal paymentAmount;

	//应还金额
	private BigDecimal repaymentAmount;

	//应还金额
	private BigDecimal interestAmount;

	//选择的期数
	private String periods ;

	private String productCategory;

	private String quietPeriod;
	//合同list
	private List<ContractDto> list ;

	private String isDepository;//是否存管

	private String termUnit;//期数单位

	private BigDecimal termRate;//期利率


	public BigDecimal getInterestAmount() {
		return interestAmount;
	}

	public void setInterestAmount(BigDecimal interestAmount) {
		this.interestAmount = interestAmount;
	}

	public String getQuietPeriod() {
		return quietPeriod;
	}

	public void setQuietPeriod(String quietPeriod) {
		this.quietPeriod = quietPeriod;
	}

	public LoanCheckResponse(LoanCodeMsgEnum code) {
		super(code);
	}

	public String getProductCategory() {
		return productCategory;
	}

	public void setProductCategory(String productCategory) {
		this.productCategory = productCategory;
	}

	public String getPeriods() {
		return periods;
	}

	public void setPeriods(String periods) {
		this.periods = periods;
	}

	public List<ContractDto> getList() {
		return list;
	}

	public void setList(List<ContractDto> list) {
		this.list = list;
	}

	public BigDecimal getOrderAmount() {
		return orderAmount;
	}

	public void setOrderAmount(BigDecimal orderAmount) {
		this.orderAmount = orderAmount;
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

	public String getIsDepository() {
		return isDepository;
	}

	public void setIsDepository(String isDepository) {
		this.isDepository = isDepository;
	}

	public String getTermUnit() {
		return termUnit;
	}

	public void setTermUnit(String termUnit) {
		this.termUnit = termUnit;
	}

	public BigDecimal getTermRate() {
		return termRate;
	}

	public void setTermRate(BigDecimal termRate) {
		this.termRate = termRate;
	}
}
