package com.xianjinxia.cashman.enums;


public enum LoanContractStatusEnum {

    NOT_HANDLE("not_handle"),
    UPLOAN_SUCCESS("uploan_success"),
    UPLOAN_FAIL("uploan_fail");

    private String code;

    LoanContractStatusEnum(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
