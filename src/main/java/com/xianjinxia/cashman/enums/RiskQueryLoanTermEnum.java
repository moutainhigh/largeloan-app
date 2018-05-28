package com.xianjinxia.cashman.enums;

/**
 * Created by liquan on 2018/1/9.
 */
public enum RiskQueryLoanTermEnum {

    YEAR(2,"年","03"),
    MONTH(1,"月","02"),
    DAY(0,"日","01");


    private String dbCode;
    private int riskCode ;
    private String value;

    RiskQueryLoanTermEnum(int riskCode,String value,String dbCode){
        this.dbCode=dbCode;
        this.value=value;
        this.riskCode=riskCode;
    }

    public static Integer getRiskStatus(String code){
        RiskQueryLoanTermEnum[] values = RiskQueryLoanTermEnum.values();
        for(RiskQueryLoanTermEnum source:values){
            if(code.equals(source.getDbCode())){
                return source.getRiskCode();
            }
        }
        return DAY.getRiskCode();
    }

    public String getDbCode() {
        return dbCode;
    }

    public void setDbCode(String dbCode) {
        this.dbCode = dbCode;
    }

    public int getRiskCode() {
        return riskCode;
    }

    public void setRiskCode(int riskCode) {
        this.riskCode = riskCode;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
