package com.xianjinxia.cashman.request;

import com.xianjingxia.paymentclient.paycenter.params.UserInfo;
import com.xianjinxia.cashman.enums.PaymentBizTypeEnum;

import javax.validation.constraints.NotNull;

public class RepaymentReq extends BaseRequest {

	@NotNull(message = "用户不可以为空")
	private Long		userId;

	@NotNull(message = "还款金额不能为空")
	private Integer		amount;

//	@Size(min = 1, message = "还款订单不能为空")
//	private Long[]	repaymentOrderIds;

	@NotNull(message = "借款订单ID不能为空")
	private Long trdLoanOrderId;

	private PaymentBizTypeEnum paymentBizTypeEnum;

	private Integer couponId;

	private Integer couponAmount;

	private UserInfo userInfo;

	public PaymentBizTypeEnum getPaymentBizTypeEnum() {
		return paymentBizTypeEnum;
	}

	public void setPaymentBizTypeEnum(PaymentBizTypeEnum paymentBizTypeEnum) {
		this.paymentBizTypeEnum = paymentBizTypeEnum;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}
//
//	public Long[] getRepaymentOrderIds() {
//		return repaymentOrderIds;
//	}
//
//	public void setRepaymentOrderIds(Long[] repaymentOrderIds) {
//		this.repaymentOrderIds = repaymentOrderIds;
//	}

	public Integer getCouponId() {
		return couponId;
	}

	public void setCouponId(Integer couponId) {
		this.couponId = couponId;
	}

	public Integer getCouponAmount() {
		return couponAmount;
	}

	public void setCouponAmount(Integer couponAmount) {
		this.couponAmount = couponAmount;
	}

	public UserInfo getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(UserInfo userInfo) {
		this.userInfo = userInfo;
	}

	public Long getTrdLoanOrderId() {
		return trdLoanOrderId;
	}

	public void setTrdLoanOrderId(Long trdLoanOrderId) {
		this.trdLoanOrderId = trdLoanOrderId;
	}
}
