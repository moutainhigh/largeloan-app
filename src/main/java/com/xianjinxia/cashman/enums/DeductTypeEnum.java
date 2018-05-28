package com.xianjinxia.cashman.enums;

public enum DeductTypeEnum {

    BEFORE(1,"前置减免"),
    AFTER(2,"后置减免");

    DeductTypeEnum(int type, String text) {
        this.type = type;
        this.text = text;
    }

    private int type;
    private String text;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }


}
