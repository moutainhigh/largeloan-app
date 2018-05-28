package com.xianjinxia.cashman.enums;

/**
 * 
 * @author chunliny
 *
 */
public enum PlatformInterfaceEnum {
	
    CREDIT_RISK_PUSH("200112","风控推送订单审核"),
    CREDIT_SHOPPING_RISK_PUSH("200113","风控推送订单审核");

    private String code;

    private String text;

    PlatformInterfaceEnum(String code, String text) {
        this.code = code;
        this.text = text;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
