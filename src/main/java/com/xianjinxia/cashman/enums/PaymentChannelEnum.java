package com.xianjinxia.cashman.enums;

/**
 * 支付渠道
 *
 * @author JaJian
 * @create 2018-05-02 18:26
 **/
public enum PaymentChannelEnum {

    OTHER_CHANNEL("otherChannel", "支付宝渠道"),

    RELIEF_CHANNEL("reliefChannel", "减免渠道");

    PaymentChannelEnum(String name, String description) {
        this.name = name;
        this.description = description;
    }

    private String name;

    private String description;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
