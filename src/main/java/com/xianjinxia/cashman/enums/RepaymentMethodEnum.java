package com.xianjinxia.cashman.enums;

public enum RepaymentMethodEnum {

    SELECT_REPAY("select_repay","选择期数还款"),
    AMOUNT_REPAY("user_amount_repay","用户输入金额还款");

    private String code;
    private String text;

    RepaymentMethodEnum(String code, String text) {
        this.code = code;
        this.text = text;
    }

    public String getCode() {
        return this.code;
    }

    public String getText() {
        return this.text;
    }

}
