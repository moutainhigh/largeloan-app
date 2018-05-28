package com.xianjinxia.cashman.enums;

public enum PaymentBizTypeEnum {

    REPAYMENT("1"," 还款"),
    RENEWAL("2","续期"),
	WITHHOLD("3","定时代扣"),
	COLLECT_WITHHOLD("4", "催收代扣"),
	APLIPAY("5", "支付宝还款"),
	CUSTOMER("6", "客服代扣"),
	COLLECT_Deduct("7","催收减免"),
	BEFOREHAND_PAY("8","冲正提前还款");

	private String code;
	private String text;
	
	PaymentBizTypeEnum(String code, String text) {
		this.code = code;
		this.text = text;
	}
	
	public String getCode() {
		return this.code;
	}
	
	public String getText() {
		return this.text;
	}
	
}
