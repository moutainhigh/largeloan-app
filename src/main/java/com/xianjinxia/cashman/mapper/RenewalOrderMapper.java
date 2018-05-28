package com.xianjinxia.cashman.mapper;

import java.util.Map;

import com.xianjinxia.cashman.domain.RenewalOrder;

public interface RenewalOrderMapper {
	
	int insert(RenewalOrder renewalOrder);
	
	int update(RenewalOrder renewalOrder);
	
	int countRenewalOrder(RenewalOrder renewalOrder);
	
	Map<String,Long> getRepaymentPlanIdByPaymentRequestId(Long paymentRequestId);

}
