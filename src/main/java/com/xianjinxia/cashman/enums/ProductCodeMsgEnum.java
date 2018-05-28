package com.xianjinxia.cashman.enums;

/**
 * @author Administrator
 * @描述 这里是描述……
 * @create 2018/1/15  18:16
 **/
public enum ProductCodeMsgEnum {

    /**
     * 成功
     */
    SUCCESS("00", "成功"),

    CHECK_CATEGORY_REPETITION("01","当前产品类型已存在，请确认"),
    CHECK_CATEGORY_ERROR("01","产品类型有误"),
    CHECK_PERIODS_REPETITION("01","产品期数重复，请确认"),


    ;

    /**
     * @描述 code
     **/
    private final String code;

    /**
     * @描述 描述
     **/
    private final String msg;

    ProductCodeMsgEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    /**
     * @描述 获取code
     **/
    public String getCode() {
        return code;
    }

    /**
     * @描述 获取描述
     **/
    public String getMsg() {

        return msg;
    }

}