package com.xianjinxia.cashman.enums;

/**
 * Created by liquan on 2018/1/9.
 */
public enum RiskQueryRepaymentStatusEnum {

    BAD_LOAN(-4,"坏账"),
    OVERDUE(-3,"逾期"),
     REPAYMENT(4,"已还清"),
    INTEREST(0,"生息中")   ;

    private int code;
    private String value;

    RiskQueryRepaymentStatusEnum(int code,String value){
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
