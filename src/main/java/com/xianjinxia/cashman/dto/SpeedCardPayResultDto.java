package com.xianjinxia.cashman.dto;

public class SpeedCardPayResultDto {

    private Long userId;//用户id

    private Long orderId;//订单id

    private String speedCardId;//提速卡id

    private String payResult;//支付结果，支付成功y，支付失败n


    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getSpeedCardId() {
        return speedCardId;
    }

    public void setSpeedCardId(String speedCardId) {
        this.speedCardId = speedCardId;
    }

    public String getPayResult() {
        return payResult;
    }

    public void setPayResult(String payResult) {
        this.payResult = payResult;
    }

    @Override
    public String toString() {
        return "SpeedCardPayResultDto{" +
                "userId=" + userId +
                ", orderId=" + orderId +
                ", speedCardId='" + speedCardId + '\'' +
                ", payResult='" + payResult + '\'' +
                '}';
    }
}
