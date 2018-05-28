package com.xianjinxia.cashman.enums;

/**
 * Created by liquan on 2018/2/11.
 */
public enum RiskQueryCustomerTypeEnum {


    NEW_USER("0","新用户"),
    OLD_USER("1","老用户");


    RiskQueryCustomerTypeEnum(String code,String value){
        this.code=code;
        this.value=value;
    }


    private String code;
    private String value;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
