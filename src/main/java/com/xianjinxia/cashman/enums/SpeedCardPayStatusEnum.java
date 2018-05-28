package com.xianjinxia.cashman.enums;


public enum SpeedCardPayStatusEnum {


    SPEED_CARD_PAYING("0", "支付中"),
    SPEED_CARD_PAY_SUCCESS("1","支付成功"),
    SPEED_CARD_PAY_FAIL("2","支付失败");

    private String code;
    private String value;

    SpeedCardPayStatusEnum(String code, String value) {
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
