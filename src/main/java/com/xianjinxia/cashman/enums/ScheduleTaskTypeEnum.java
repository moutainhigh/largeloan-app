package com.xianjinxia.cashman.enums;

public enum ScheduleTaskTypeEnum {


    WITHHOLD_TIMER("WITHHOLD","定时代扣"),
    COLLECTION_NOTIFY("COLLECTION_NOTIFY","催收通知"),
    OVERDUE_SCANNER("OVERDUE_SCANNER","逾期扫描"),
    OVERDUE_CALCULATE("OVERDUE_CALCULATE","逾期计算");


    private String code;
    private String text;

    ScheduleTaskTypeEnum(String code, String text) {
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
