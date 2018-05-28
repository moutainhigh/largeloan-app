package com.xianjinxia.cashman.domain;

import java.util.Date;

public class RenewalOrder {
	
	
	private Long id;
	
	/**
	 * 还款计划Id
	 */
	private Long repaymentPlanId;
	
	/**
	 * 支付中心请求Id
	 */
	private Long paymentRequestId;
	
	/**
	 * 借款订单Id
	 */
	private Long loanOrderId;
	
	/**
	 * 创建时间
	 */
	private Date createdTime;
	
	/**
	 * 创建用户
	 */
	private String createdUser;
	
	/**
	 * 更新时间
	 */
	private Date updatedTime;
	
	/**
	 * 数据有效性
	 */
	private Boolean dataValid;
	
	/**
	 * 用户id
	 */
	private Long userId;
	
	/**
	 * 续期手续费
	 */
	private int renewalFee;
	
	/**
	 * 续期状态
	 */
	private int status;
	
	/**
	 * 续期前还款时间
	 */
	private Date preRepaymentTime;
	
	/**
	 * 续期后还款时间
	 */
	private Date renewalRepaymentTime;
	
	/**
	 * 续期金额
	 */
	private int renewalAmount;
	
	/**
	 * 续期利息费用
	 */
	private int interestAmount;
	
	/**
	 * 备注信息
	 */
	private String remark;
	
	/**
	 * 用于唯一性约束的字段
	 */
	private String renewalUnique;
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getRepaymentPlanId() {
		return repaymentPlanId;
	}

	public void setRepaymentPlanId(Long repaymentPlanId) {
		this.repaymentPlanId = repaymentPlanId;
	}

	public Long getPaymentRequestId() {
		return paymentRequestId;
	}

	public void setPaymentRequestId(Long paymentRequestId) {
		this.paymentRequestId = paymentRequestId;
	}

	public Long getLoanOrderId() {
		return loanOrderId;
	}

	public void setLoanOrderId(Long loanOrderId) {
		this.loanOrderId = loanOrderId;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public String getCreatedUser() {
		return createdUser;
	}

	public void setCreatedUser(String createdUser) {
		this.createdUser = createdUser;
	}

	public Date getUpdatedTime() {
		return updatedTime;
	}

	public void setUpdatedTime(Date updatedTime) {
		this.updatedTime = updatedTime;
	}

	public Boolean getDataValid() {
		return dataValid;
	}

	public void setDataValid(Boolean dataValid) {
		this.dataValid = dataValid;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public int getRenewalFee() {
		return renewalFee;
	}

	public void setRenewalFee(int renewalFee) {
		this.renewalFee = renewalFee;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Date getPreRepaymentTime() {
		return preRepaymentTime;
	}

	public void setPreRepaymentTime(Date preRepaymentTime) {
		this.preRepaymentTime = preRepaymentTime;
	}

	public Date getRenewalRepaymentTime() {
		return renewalRepaymentTime;
	}

	public void setRenewalRepaymentTime(Date renewalRepaymentTime) {
		this.renewalRepaymentTime = renewalRepaymentTime;
	}

	public int getRenewalAmount() {
		return renewalAmount;
	}

	public void setRenewalAmount(int renewalAmount) {
		this.renewalAmount = renewalAmount;
	}

	public int getInterestAmount() {
		return interestAmount;
	}

	public void setInterestAmount(int interestAmount) {
		this.interestAmount = interestAmount;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getRenewalUnique() {
		return renewalUnique;
	}

	public void setRenewalUnique(String renewalUnique) {
		this.renewalUnique = renewalUnique;
	}
	
}
