package com.xianjinxia.cashman.enums;

/**
 * Created by liquan on 2018/1/10.
 */
public enum RiskQueryAutoRiskCheckStatusEnum {


    NOTPUSH(0,"尚未推单"),
    MANUAL(1,"人工初审"),
    PUSHOK(2,"推单成功");

    private int code;
    private String value;

    RiskQueryAutoRiskCheckStatusEnum(int code,String value){
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
