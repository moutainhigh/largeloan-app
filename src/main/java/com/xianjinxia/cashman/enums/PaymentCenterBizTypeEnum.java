package com.xianjinxia.cashman.enums;

public enum PaymentCenterBizTypeEnum {

    BIG_AMT_TIMER_WITHHOLD("C5","正常代扣"),
    BIG_AMT_OVERDUE_WITHHOLD("C6","逾期代扣"),
    BIG_AMT_USER_REPAY("C7","用户主动还款"),
    BIG_AMT_UNILATERAL("D2","单边账冲账");

    private String code;
    private String text;

    PaymentCenterBizTypeEnum(String code, String text) {
        this.code = code;
        this.text = text;
    }

    public String getCode() {
        return code;
    }

    public String getText() {
        return text;
    }
}
