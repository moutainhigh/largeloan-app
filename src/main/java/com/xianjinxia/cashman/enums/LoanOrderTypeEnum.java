package com.xianjinxia.cashman.enums;

public enum LoanOrderTypeEnum {

	BIG(1, "大额"),
	SMALL(2, "小额");


	private int		code;
	private String	text;

	LoanOrderTypeEnum(int code, String text) {
		this.code = code;
		this.text = text;
	}

	public int getCode() {
		return this.code;
	}

	public String getText() {
		return this.text;
	}

}
