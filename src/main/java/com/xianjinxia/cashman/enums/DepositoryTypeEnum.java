package com.xianjinxia.cashman.enums;

/**
 * Created by liquan on 2018/1/10.
 */
public enum DepositoryTypeEnum {
    Depository("1","接存管"),
    Not_Depository("0","不接存管");
    private String code;
    private String desc;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    DepositoryTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
