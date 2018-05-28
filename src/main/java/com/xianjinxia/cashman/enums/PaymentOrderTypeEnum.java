package com.xianjinxia.cashman.enums;

public enum PaymentOrderTypeEnum {

    ALIPAY("1"," 支付宝"),
    BANK_CARD("2","银行卡主动还款 "),
    BANK_CARD_AUTO("3"," 银行卡自动扣款"),
    BANK_CARD_TRANSFER("4","对公银行卡转账 "),
    OFF_LINE("5"," 线下还款"),
    OFF_LINE_DEDUCTION("6","线下还款"),
    COUPON("10"," 优惠券还款"),
    PAY_CENTER("12","支付中心"),
	COLLECTION_DEDUCT("99","催收减免");
	

	private String code;
	private String text;
	
	PaymentOrderTypeEnum(String code, String text) {
		this.code = code;
		this.text = text;
	}
	
	public String getCode() {
		return this.code;
	}
	
	public String getText() {
		return this.text;
	}


	public static String getText(String code){
		PaymentOrderTypeEnum[] values = PaymentOrderTypeEnum.values();
		for (int i = 0; i < values.length; i++) {
			PaymentOrderTypeEnum enumPaymentOrderType = values[i];
			if (enumPaymentOrderType.getCode().equals(code)){
				return enumPaymentOrderType.getText();
			}
		}

		return null;
	}

}
