package com.xianjinxia.cashman.enums;


public enum GroupRiskResultEnum {

    APPROVED(1, "审核通过", TrdLoanOrderStatusEnum.APPROVED),
    MANUAL_REVIEW(2, "待人工审核", TrdLoanOrderStatusEnum.MANUAL_REVIEWING),
    REFUSED(3, "审核拒绝", TrdLoanOrderStatusEnum.REFUSED);


    private int code;
    private String desc;
    private TrdLoanOrderStatusEnum orderStatus;

    GroupRiskResultEnum(int code, String desc, TrdLoanOrderStatusEnum orderStatus) {
        this.code = code;
        this.desc = desc;
        this.orderStatus = orderStatus;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public TrdLoanOrderStatusEnum getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(TrdLoanOrderStatusEnum orderStatus) {
        this.orderStatus = orderStatus;
    }


    public static GroupRiskResultEnum getByCode(int code) {
        for (GroupRiskResultEnum groupRiskResultEnum : GroupRiskResultEnum.values()) {
            if (groupRiskResultEnum.getCode() == code){
                return groupRiskResultEnum;
            }
        }
        return null;
    }

    public static TrdLoanOrderStatusEnum getOrderStatusByResultCode(int code) {
        for (GroupRiskResultEnum e : GroupRiskResultEnum.values()) {
            if (e.getCode() == code) {
                return e.getOrderStatus();
            }
        }
        return null;
    }
}
