package com.xianjinxia.cashman.enums;


public enum EventControlTypeEnum {

    REPAYMENT_PLAN("repayment_plan","还款计划");
    
    private String value;
	
	private String desc;

    EventControlTypeEnum(String value, String desc) {
        this.value = value;
    }

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
    
}
