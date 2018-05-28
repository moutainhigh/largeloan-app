package com.xianjinxia.cashman.dto;

import java.util.Date;

public class RenewalFeeInfoDto {
	
	
	private Integer renewalAmount;//续期总金额
	
	private Integer renewalFee;//续期手续费
	
	private Integer renewalInterest;//续期利息
	
	private Integer totalRenewalFee;//总续期费用
	
	private Integer term;//续期天数
	
	private Date repaymentTime;//应还款时间
	
	private Integer nowApplyCount;//已经续期次数
	
	private Integer usebleCoupon;//可用优惠券数目
	
	private Date nextRepayDay;//续期后应还款时间
	

	public Integer getRenewalAmount() {
		return renewalAmount;
	}

	public void setRenewalAmount(Integer renewalAmount) {
		this.renewalAmount = renewalAmount;
	}

	public Integer getRenewalFee() {
		return renewalFee;
	}

	public void setRenewalFee(Integer renewalFee) {
		this.renewalFee = renewalFee;
	}

	public Integer getRenewalInterest() {
		return renewalInterest;
	}

	public void setRenewalInterest(Integer renewalInterest) {
		this.renewalInterest = renewalInterest;
	}

	public Integer getTotalRenewalFee() {
		return totalRenewalFee;
	}

	public void setTotalRenewalFee(Integer totalRenewalFee) {
		this.totalRenewalFee = totalRenewalFee;
	}

	public Integer getTerm() {
		return term;
	}

	public void setTerm(Integer term) {
		this.term = term;
	}

	public Date getRepaymentTime() {
		return repaymentTime;
	}

	public void setRepaymentTime(Date repaymentTime) {
		this.repaymentTime = repaymentTime;
	}

	public Integer getNowApplyCount() {
		return nowApplyCount;
	}

	public void setNowApplyCount(Integer nowApplyCount) {
		this.nowApplyCount = nowApplyCount;
	}

	public Integer getUsebleCoupon() {
		return usebleCoupon;
	}

	public void setUsebleCoupon(Integer usebleCoupon) {
		this.usebleCoupon = usebleCoupon;
	}

	public Date getNextRepayDay() {
		return nextRepayDay;
	}

	public void setNextRepayDay(Date nextRepayDay) {
		this.nextRepayDay = nextRepayDay;
	}
	
	

}
