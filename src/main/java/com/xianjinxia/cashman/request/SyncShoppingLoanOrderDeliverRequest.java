package com.xianjinxia.cashman.request;

import java.util.Date;

import javax.validation.constraints.NotNull;

import com.xianjinxia.cashman.idempotent.IdempotentKey;
import com.xianjinxia.cashman.request.BaseRequest;

public class SyncShoppingLoanOrderDeliverRequest extends BaseRequest {

    @IdempotentKey(order=1)
    @NotNull(message="trdLoanOrderId could not be null")
    private Long trdLoanOrderId;

    @NotNull(message="paymentPlanTime could not be null")
    private Date paymentPlanTime;
    
	public SyncShoppingLoanOrderDeliverRequest(Long trdLoanOrderId, Date paymentPlanTime) {
		super();
		this.trdLoanOrderId = trdLoanOrderId;
		this.paymentPlanTime = paymentPlanTime;
	}


	public Long getTrdLoanOrderId() {
		return trdLoanOrderId;
	}

	public void setTrdLoanOrderId(Long trdLoanOrderId) {
		this.trdLoanOrderId = trdLoanOrderId;
	}

	public Date getPaymentPlanTime() {
		return paymentPlanTime;
	}

	public void setPaymentPlanTime(Date paymentPlanTime) {
		this.paymentPlanTime = paymentPlanTime;
	}
	
	@Override
	public String toString() {
		return "SyncShoppingLoanOrderDeliverRequest [trdLoanOrderId=" + trdLoanOrderId
				+ ", paymentPlanTime=" + paymentPlanTime + "]";
	}


}
