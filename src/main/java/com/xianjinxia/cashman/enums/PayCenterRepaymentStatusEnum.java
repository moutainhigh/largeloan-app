package com.xianjinxia.cashman.enums;

public enum PayCenterRepaymentStatusEnum {
    REQUEST_HANDLING("request_handling", "支付处理中"), SUCCESS("success", "支付成功"), FAILED("failed", "支付失败"), REQUEST_UNFIND("request_unfind", "未查询到该订单");


    private String code;
    private String value;

    PayCenterRepaymentStatusEnum(String code, String value) {
        this.code = code;
        this.value = value;
    }

    public String getCode() {
        return this.code;
    }

    public String getValue() {
        return this.value;
    }

}
