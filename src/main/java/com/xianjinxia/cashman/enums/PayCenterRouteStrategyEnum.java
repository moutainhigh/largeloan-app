package com.xianjinxia.cashman.enums;

/**
 * 历史有逾期：全部都走Overdue
 * 历史没逾期：
 *
 *
 */
public enum PayCenterRouteStrategyEnum {

    //提前还款（全部期数一次性2），正常还款，1
    CUSTODY_INITIATIVE("custody_initiative", "存管,正常还款", true),
    //定时代扣
    CUSTODY_TITMING("custody_timing", "存管,定时代扣", true),
    //大额逾期
    CUSTODY_OVERDUE("custody_overdue", "不走存管,逾期还款", false),

    //当天借款，当天还款
    PETTYLOAN_CUSTODY_INITIATIVE("pettyloan_custody_initiative", "不走存管,当天借款,当天还款；分期商城订单", false),

    INITIATIVE("initiative", "不走存管,正常还款", false),
    TIMING("timing", "不走存管,定时代扣", false),
    OVERDUE("overdue", "不走存管,逾期还款", false);

    private String code;
    private String text;
    private boolean isCustody;

    PayCenterRouteStrategyEnum(String code, String text, boolean isCustody) {
        this.code = code;
        this.text = text;
        this.isCustody = isCustody;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isCustody() {
        return isCustody;
    }

    public void setCustody(boolean custody) {
        isCustody = custody;
    }
}
