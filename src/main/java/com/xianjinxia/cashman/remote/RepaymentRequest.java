package com.xianjinxia.cashman.remote;

import java.io.Serializable;

public class RepaymentRequest implements Serializable {

	private String	userId;
	private Long	amount;
	private Boolean	isFirstRepay;
	private Boolean	isRepayLate;
	private Boolean	isRepayAll;
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public Long getAmount() {
		return amount;
	}
	public void setAmount(Long amount) {
		this.amount = amount;
	}
	public Boolean getIsFirstRepay() {
		return isFirstRepay;
	}
	public void setIsFirstRepay(Boolean isFirstRepay) {
		this.isFirstRepay = isFirstRepay;
	}
	public Boolean getIsRepayLate() {
		return isRepayLate;
	}
	public void setIsRepayLate(Boolean isRepayLate) {
		this.isRepayLate = isRepayLate;
	}
	public Boolean getIsRepayAll() {
		return isRepayAll;
	}
	public void setIsRepayAll(Boolean isRepayAll) {
		this.isRepayAll = isRepayAll;
	}
}
