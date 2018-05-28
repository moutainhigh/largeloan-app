package com.xianjinxia.cashman.service;

import com.xianjinxia.cashman.domain.Products;
import com.xianjinxia.cashman.domain.RenewalOrder;
import com.xianjinxia.cashman.domain.RepaymentPlan;
import com.xianjinxia.cashman.dto.RenewalFeeInfoDto;
import com.xianjinxia.cashman.dto.PaymentParamDto;
import com.xianjinxia.cashman.request.PayCenterCallbackReq;
import com.xianjinxia.cashman.request.UserCouponReq;

public interface IRenewalService {
	
	void checkCanRenewal(Long userId, RepaymentPlan repaymentPlan, Products products);
	
	RenewalFeeInfoDto calculateRenewalFee(RepaymentPlan repaymentPlan, Products products);

	PaymentParamDto repayRenewalFee(RepaymentPlan repaymentPlan, RenewalFeeInfoDto renewalFeeInfo);
	
	int updateRenewalOrder(RenewalOrder renewalOrder);
	
	int countRenewalOrder(RenewalOrder renewalOrder);
	
	Integer getUsebleCouponSize(UserCouponReq userCouponReq);
	
	void renewalPayCallback(PayCenterCallbackReq payCenterCallbackReq);

}
