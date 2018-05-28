package com.xianjinxia.cashman.enums;

/**
 * Created by liquan on 2018/1/9.
 * 风控查询 订单枚举
 */
public enum  RiskQueryOrderEnum {

    ORDER_LOAN_REJECT("-9","放款驳回"),
    ORDER_REVIEW_REJECT("-4","复审驳回"),
    ORDER_REVIEW("7","待复审"),
    ORDER_FIRSTTRIAL_REJECT("-3","初审驳回"),
    ORDER_FIRSTTRIAL("0","待初审"),
    ORDER_LOAN_ING("2","打款中"),
    ORDER_REPAYMENT_BEGIN("3","还款中"),
    ORDER_REPAYMENT_END("6","已还款"),
    ORDER_MANUAL_REJECT("10000","人工拒绝");


    private String code;
    private String value;

    RiskQueryOrderEnum(String code,String value){
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
