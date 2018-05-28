package com.xianjinxia.cashman.enums;

public enum IdempotentEventTypeEnum {

	RENEWAL_APPLY("renewal_apply","续期申请"),
	
	SYNC_REPAYMENT_PLAN_TIME("sync_repayment_plan_time","异步更新还款计划时间"),

	GROUP_RISK_CALLBACK("group_risk_callback","集团风控结果通知"),

	COLLECT_WITH_HOLD("collect_withhold","催收代扣"),

	COLLECT_DEDUCT("collect_deduct","催收减免"),

	ALIPAY_REPAY_INCOME("alipay_repay_income","支付宝还款入账"),

	BEFORE_HAND_PAY("before_hand_pay","冲正提前还款"),

	SYNC_SHOPPING_ORDER_STATUS("sync_shopping_order_status", "同步订单状态");


	IdempotentEventTypeEnum(String code,String desc){
			this.code=code;
			this.desc=desc;
		}

	private String code;
	private String desc;

	public String getCode() {
			return code;
		}

	public String getDesc() {
		return desc;
	}
}
