package com.xianjinxia.cashman.enums;


public enum AbleRenewalEnum {

	    ABLE_RENEWAL(1,"支持续期"),
	    DISABLE_RENEWAL(0,"不支持续期");

	    private int code;
	    private String value;

	    AbleRenewalEnum(int code, String value) {
	        this.code = code;
	        this.value = value;
	    }

	    public int getCode() {
	        return code;
	    }

	    public void setCode(int code) {
	        this.code = code;
	    }

	    public String getValue() {
	        return value;
	    }

	    public void setValue(String value) {
	        this.value = value;
	    }
}
