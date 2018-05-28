package com.xianjinxia.cashman.enums;

/**
 * Created by liquan on 2018/1/10.
 */
public enum  RiskQueryOrderTypeEnum {

    LQD(1,"零钱贷"),
    XJFQ(2,"现金分期"),
    FQSC(3,"分期商城");

    private int code;
    private String value;

    RiskQueryOrderTypeEnum(int code,String value){
        this.code=code;
        this.value=value;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
