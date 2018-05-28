package com.xianjinxia.cashman.enums;

/**
 * Created by liquan on 2017/12/28.
 */
public enum TrdLoanSourceEnum {
    JSXJX("01","jsxjx","极速现金侠"),
    R360("02","r360","融360",RiskQueryOrderSourceEnum.RONG360),
    JQY("03","jqy","借钱易");
    private String code;
    private String name;
    private String nameCn;
    private RiskQueryOrderSourceEnum riskSource;

    TrdLoanSourceEnum(String code, String name,String nameCn) {
        this.code = code;
        this.name = name;
        this.nameCn = nameCn;
    }

    TrdLoanSourceEnum(String code, String name,String nameCn,RiskQueryOrderSourceEnum riskSource) {
        this.code = code;
        this.name = name;
        this.nameCn = nameCn;
        this.riskSource = riskSource;
    }

    public static Integer getRiskSouce(String code){
        TrdLoanSourceEnum[] values = TrdLoanSourceEnum.values();
        for (int i = 0; i < values.length; i++) {
            TrdLoanSourceEnum source = values[i];
            if(code.equals(source.getCode())){
                if(null!=source.getRiskSource()){
                    return source.getRiskSource().getCode();
                }
            }
        }
        // 默认来自app
        return RiskQueryOrderSourceEnum.APP.getCode();
    }


    public RiskQueryOrderSourceEnum getRiskSource() {
        return riskSource;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameCn() {
        return nameCn;
    }

    public void setNameCn(String nameCn) {
        this.nameCn = nameCn;
    }
}
