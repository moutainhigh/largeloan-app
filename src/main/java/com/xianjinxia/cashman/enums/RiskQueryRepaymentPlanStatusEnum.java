package com.xianjinxia.cashman.enums;

/**
 * Created by liquan on 2018/1/9.
 */
public enum  RiskQueryRepaymentPlanStatusEnum {

    AWAIT("1","待还款"),
    REPAYMENT("2","已还款"),
    OVERDUE("3","已逾期"),
    BAD("4","已坏账"),
    PART("5","部分还款");

    private String code;
    private String value;

    RiskQueryRepaymentPlanStatusEnum(String code,String value){
        this.code=code;
        this.value=value;
    }


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
