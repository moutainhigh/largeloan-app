package com.xianjinxia.cashman.enums;

public enum ScheduleTaskStatusEnum {


    WAITING(0,"NEW"),
    SUCCESS(2,"SUCCESS"),
    PROCESSING(1,"PROCESSING");


    private int code;
    private String text;

    ScheduleTaskStatusEnum(int code, String text) {
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
