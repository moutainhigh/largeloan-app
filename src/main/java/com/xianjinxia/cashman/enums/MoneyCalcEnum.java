package com.xianjinxia.cashman.enums;

/**
 * 本金、利息计算使用的方法枚举
 */
public enum MoneyCalcEnum {
    ECAI("ECAI","等额本息"),
    EMAI("EMAI","等本等息");

    private String calcMethod;
    private String descrption;

    MoneyCalcEnum(String calcMethod,String description){
        this.calcMethod=calcMethod;
        this.descrption=description;
    }

    public String getDescrption() {
        return descrption;
    }

    public void setDescrption(String descrption) {
        this.descrption = descrption;
    }

    public String getCalcMethod() {
        return calcMethod;
    }

    public void setCalcMethod(String calcMethod) {
        this.calcMethod = calcMethod;
    }
}
