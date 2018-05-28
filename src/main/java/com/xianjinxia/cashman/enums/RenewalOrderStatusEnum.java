package com.xianjinxia.cashman.enums;


public enum RenewalOrderStatusEnum {

	    PAYING(0,"付款中"),
	    SUCCESS(1,"续期成功"),
	    FAIL(2,"续期失败");

	    private int code;
	    private String value;

	    RenewalOrderStatusEnum(int code, String value) {
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
