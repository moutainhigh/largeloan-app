package com.xianjinxia.cashman.request;

import javax.validation.constraints.NotNull;

public class UserCouponReq extends BaseRequest {

	@NotNull(message="userId couldn't be null")
	private Long userId; //用户id
	
	@NotNull(message="repaymentTime couldn't be null")
	private Long repaymentTime; //还款时间
	
	@NotNull(message="loanTerm couldn't be null")
	private Integer loanTerm; //借款期限

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}


	public Long getRepaymentTime() {
		return repaymentTime;
	}

	public void setRepaymentTime(Long repaymentTime) {
		this.repaymentTime = repaymentTime;
	}

	public Integer getLoanTerm() {
		return loanTerm;
	}

	public void setLoanTerm(Integer loanTerm) {
		this.loanTerm = loanTerm;
	}
	
}
