package com.xianjinxia.cashman.enums;

/**
 * Created by liquan on 2017/11/24.
 */
public enum DepositoryAgreementContractParmEnum {
    PARM_REAL_NAME("###realName###","realName"),
    PARM_PARTB_NAME("###partBName###","partBName"),
    PARM_PARTC_NAME("###partCName###","partCName");

    private String code;
    private String name;

    DepositoryAgreementContractParmEnum(String code, String name) {
        this.code = code;
        this.name = name;
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
}
