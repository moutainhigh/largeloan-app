package com.xianjinxia.cashman.enums;

/**
 * 支付宝还款入账方式
 *
 * @author JaJian
 * @create 2018-05-09 14:00
 **/
public enum AlipayRepayIncomeTypeEnum {

    AUTO_INCOME(0, "自动入账"),
    MANUAL_INCOME(1, "人工入账");

    private int code;

    private String desc;

    AlipayRepayIncomeTypeEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

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
