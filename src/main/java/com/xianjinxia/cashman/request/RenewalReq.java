package com.xianjinxia.cashman.request;

import javax.validation.constraints.NotNull;

public class RenewalReq extends BaseRequest {
	
	@NotNull(message="userId couldn't be null")
	private Long userId;
	
	@NotNull(message="repaymentId couldn't be null")
	private Long repaymentId;
	
	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getRepaymentId() {
		return repaymentId;
	}

	public void setRepaymentId(Long repaymentId) {
		this.repaymentId = repaymentId;
	}

	@Override
	public String toString() {
		return "RenewalReq [userId=" + userId + ", repaymentId=" + repaymentId + "]";
	}
	

}
