package com.xianjinxia.cashman.enums;


/**
 * 1 是正常还款的时候推过来 比如 1期的大额债权或者多期的 你想一期期还 不一定是到期的可能没到期的都可以通过这个方式还
 * 2 是提前还款,这个是针对不款计划总数大于1 的情况 钱是真放出去 然后提前还了
 * 3 是异常情况 比如提现失败 或者 单边账这种
 */
public enum RepayTypeEnum {

    NORMAL_REPAY(1, "正常还款"),
    PRE_REPAY(2, "提前还款"),
    ERROR_REPAY(3, "异常还款");

    private int code;
    private String value;
    private PayCenterRouteStrategyEnum payCenterRouteStrategyEnum;

    RepayTypeEnum(int code, String value) {
        this.code = code;
        this.value = value;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
