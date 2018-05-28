package com.xianjinxia.cashman.enums;

public enum RepaymentBadLevelEnum {
    ZERO(0,"初始化"),
    ONE(1,"级别1");

    private int code;
    private String text;

    RepaymentBadLevelEnum(int code, String text) {
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
