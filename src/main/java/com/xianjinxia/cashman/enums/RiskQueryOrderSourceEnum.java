package com.xianjinxia.cashman.enums;

/**
 * Created by liquan on 2018/1/9.
 */
public enum RiskQueryOrderSourceEnum {
        APP(1,"APP"),
        H5(0,"H5"),
        RONG360(2,"rong360");



    private int code;
    private String value;

    RiskQueryOrderSourceEnum(int code,String value){
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
