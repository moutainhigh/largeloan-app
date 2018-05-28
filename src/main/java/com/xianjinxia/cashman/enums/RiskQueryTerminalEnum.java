package com.xianjinxia.cashman.enums;

/**
 * Created by liquan on 2018/1/9.
 * 风控查询终端类型枚举
 */
public enum RiskQueryTerminalEnum {

    ANDROID(1,"android","02"),
    IOS(2,"IOS","01"),
    H5(3,"H5","03"),
    APP(4,"APP","04");



    private String dbCode;
    private int riskCode ;
    private String value;

    RiskQueryTerminalEnum(int riskCode,String value,String dbCode){
        this.dbCode=dbCode;
        this.value=value;
        this.riskCode=riskCode;
    }

    public static Integer getRiskCode(String code){
        RiskQueryTerminalEnum[] values = RiskQueryTerminalEnum.values();
        for(RiskQueryTerminalEnum risk:values){
            if(code.equals(risk.getCode())){
                return risk.getRiskCode();
            }
        }
        // 默认返回android
        return ANDROID.riskCode;
    }


    public int getRiskCode() {
        return riskCode;
    }

    public String getCode() {
        return dbCode;
    }

    public void setCode(String code) {
        this.dbCode = code;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
