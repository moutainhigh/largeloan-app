package com.xianjinxia.cashman.enums;


public enum BuyCardEnum {

    BUY_CARD_YES("y", "购买"),
    BUY_CARD_NO("n", "不购买");

    private String code;
    private String value;

    BuyCardEnum(String code, String value) {
        this.code = code;
        this.value = value;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
