package com.xianjinxia.cashman.enums;

/**
 * @author ganminghui
 * 催收系统常量
 */

public enum CollectWithholdEnum {
    /** ============催收请求状态常量============ */
    STATUS_NEW(0,"初始化入库"),
    STATUS_SUCCESS(1,"请求成功"),
    STATUS_FAITURE(2,"请求失败"),

    /** ============催收类型常量============ */
    TYPE_WITHHOLE(1,"催收代扣"),
    TYPE_DEDUCT(2,"催收减免"),
    ;

    CollectWithholdEnum(int type, String text) { this.type = type;this.text = text; }

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
