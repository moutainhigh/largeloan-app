package com.xianjinxia.cashman.enums;

/**
 * Created by liquan on 2017/11/24.
 */
public enum ContractTypeEnum {

    LOAN_AGREEMENT("借款协议","LOAN_AGREEMENT"),
    PLATFORM_SERVICE_AGREEMENT("平台服务协议","PLATFORM_SERVICE_AGREEMENT"),
    DEPOSITORY_AGREEMENT("存管协议","DEPOSITORY_AGREEMENT");

    private String name;
    private String type;


    ContractTypeEnum(String name, String type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
