package com.xianjinxia.cashman.request;

import javax.validation.constraints.NotNull;

/**
 * 还款详情请求参数
 * @author hym
 * 2017年9月8日
 */
public class RepaymentDetailReq extends BaseRequest {

	@NotNull(message="loanId couldn't be null")
    private long loanId;

	private  Long userId;
	//期数
	private Integer period;
    //类型 1大额 2 小额
	private String orderType;
	public Integer getPeriod() {
		return period;
	}

	public void setPeriod(Integer period) {
		this.period = period;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}


	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public long getLoanId() {
		return loanId;
	}

	public void setLoanId(long loanId) {
		this.loanId = loanId;
	}





}
