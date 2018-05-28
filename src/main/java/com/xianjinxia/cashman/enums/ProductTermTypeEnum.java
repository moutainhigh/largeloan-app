package com.xianjinxia.cashman.enums;

public enum ProductTermTypeEnum {

	DAY("01"),
	MONTH("02");

	private String code;

	ProductTermTypeEnum(String code) {
		this.code = code;
	}

	public String getCode() {
		return this.code;
	}

}
