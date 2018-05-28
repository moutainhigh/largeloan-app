package com.xianjinxia.cashman.enums;

public enum RepaymentOverdueStatusEnum {
    NORMAL(0,"未逾期"),
    OVERDUE(1,"已逾期");

    private int code;
    private String text;

    RepaymentOverdueStatusEnum(int code, String text) {
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
