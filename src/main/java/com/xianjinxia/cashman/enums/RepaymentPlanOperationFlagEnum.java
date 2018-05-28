package com.xianjinxia.cashman.enums;

public enum RepaymentPlanOperationFlagEnum {

    INIT("init", "代扣任务"),
    WITHHOLD("withhold", "代扣任务"),
    OVERDUE("overdue", "逾期任务");


    private String code;
    private String text;

    RepaymentPlanOperationFlagEnum(String code, String text) {
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
