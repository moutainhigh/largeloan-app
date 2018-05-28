package com.xianjinxia.cashman.response;

/**
 * Created by liquan on 2018/4/11.
 *
 * @Author: liquan
 * @Description:
 * @Date: Created in 18:00 2018/4/11
 * @Modified By:
 */
public class SpeedCardRepayRsp {
    /**
     * 是否需要重新支付
     */
    private Boolean isNeedRePay;
    /**
     * cashman-app对应数据库表主键
     */
    private Long orderId;

    public Boolean getNeedRePay() {
        return isNeedRePay;
    }

    public void setNeedRePay(Boolean needRePay) {
        isNeedRePay = needRePay;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    @Override
    public String toString() {
        return "SpeedCardRepayRsp{" +
                "isNeedRePay=" + isNeedRePay +
                ", orderId=" + orderId +
                '}';
    }

    public SpeedCardRepayRsp() {
    }

    public SpeedCardRepayRsp(Boolean isNeedRePay, Long orderId) {
        this.isNeedRePay = isNeedRePay;
        this.orderId = orderId;
    }
}
