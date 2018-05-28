package com.xianjinxia.cashman.service;

import com.xianjinxia.cashman.domain.RepaymentPlan;
import com.xianjinxia.cashman.dto.IndexRepaymentPlanDto;
import com.xianjinxia.cashman.request.*;

import java.text.ParseException;
import java.util.List;

/**
 * 
 * 还款接口（ 上层业务逻辑入口 ）, 包含一键还款和银行卡还款
 * 
 * @version 1.0.0
 * 
 * @author 张永嘉
 *
 */
public interface IRepaymentService {

	void repayCommit(RepaymentReq repaymentReq);

    //支付宝还款入账
	void repayIncomeByAlipay(AlipayRepayIncomeReq alipayRepayIncomeReq);

	void repayCallback(PayCenterCallbackReq payCenterCallbackReq);

	void repayDeduct(CollectionDeductReq repaymentDeductDto);

	void repayCheckWithPayCenter(Long paymentRequestId);

	void repayVerify(VerifyPaymentReq verifyPaymentReq);

	RepaymentPlan getRepaymentOrderById(String id);
	
	List<RepaymentPlan> getRepaymentPlanListByTrdLoanOrderId(Long loanOrderId);

	Boolean hasNonUltimateOrder(Long userId);

    List<IndexRepaymentPlanDto> getRepaymentOrder(Long loanOrderId) throws ParseException;

    /**
	 * 催收代扣
	 *	@param collectWithholdReq 催收代扣请求对象
	 */
    void repayWithhold(CollectWithholdReq collectWithholdReq);

	// 人工后台直接入账
	void repayIncomeByAlipayManual(AlipayRepayIncomeReq alipayRepayIncomeReq);
}
