package com.xianjinxia.cashman.enums;

/**
 * 通知催收的操作类型
 *
 * @author JaJian
 * @create 2018-05-10 10:30
 **/
public enum NotifyCollectionTypeEnum {

    NOTIFY(0, "通知催收逾期信息"),

    CANCEL(1, "通知催收关闭订单");

    NotifyCollectionTypeEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    private int code;

    private String desc;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
