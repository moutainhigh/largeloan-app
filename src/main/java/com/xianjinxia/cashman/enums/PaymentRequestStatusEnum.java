package com.xianjinxia.cashman.enums;

public enum PaymentRequestStatusEnum {

	
	NEW(0,"初始化创建还款请求", null),
//	FREEZE(3, "支付中心已经收到此订单，冻结状态", PaymentRequestStatusEnum.NEW),
	SUCCESS(2,"还款成功", PaymentRequestStatusEnum.NEW),
	FAILURE(1,"还款失败",  PaymentRequestStatusEnum.NEW),
	CANCEL(-1,"取消",PaymentRequestStatusEnum.NEW);
	

	private int code;
	private String text;
	private PaymentRequestStatusEnum preConditionStatus;
	
	PaymentRequestStatusEnum(int code, String text, PaymentRequestStatusEnum preConditionStatus) {
		this.code = code;
		this.text = text;
		this.preConditionStatus = preConditionStatus;
	}
	
	public int getCode() {
		return this.code;
	}
	
	public String getText() {
		return this.text;
	}

	public PaymentRequestStatusEnum getPreConditionStatus() {
		return preConditionStatus;
	}
}
