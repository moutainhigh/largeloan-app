package com.xianjinxia.cashman.enums;

/**
 * 费用类型枚举
 * 
 * @author liuzhifang
 *
 *         2017年10月16日
 */
public enum ProductsFeeConfigEnum {


    MANAGE_FEE_BIG("manage_fee", "管理费"),
    CONSULTATION_FEE_BIG("consultation_fee", "咨询费"),
    INTEREST_FEE("interest_fee","利息费"),
    OVERDUE_FEE("overdue_fee","逾期费"),

//    PERIOD_RATE("period_rate","产品期利率"),
//    DAILY_OVERDUE_RATE("daily_overdue_rate","滞纳金日利率"),
;
    private String code;
    private String value;

    ProductsFeeConfigEnum(String code, String value) {
        this.code = code;
        this.value = value;
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
