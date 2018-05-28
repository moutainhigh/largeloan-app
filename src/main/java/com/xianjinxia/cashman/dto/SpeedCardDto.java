package com.xianjinxia.cashman.dto;

public class SpeedCardDto {

    private Long userId;//用户id

    private Long orderNo;//订单号

    private String speedCardId;//提速卡Id

    private String riskResult;//风控审核结果 y表示通过,n表示拒绝

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }


    public Long getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(Long orderNo) {
        this.orderNo = orderNo;
    }

    public String getSpeedCardId() {
        return speedCardId;
    }

    public void setSpeedCardId(String speedCardId) {
        this.speedCardId = speedCardId;
    }

    public String getRiskResult() {
        return riskResult;
    }

    public void setRiskResult(String riskResult) {
        this.riskResult = riskResult;
    }

    @Override
    public String toString() {
        return "SpeedCardDto{" +
                "userId=" + userId +
                ", orderNo='" + orderNo + '\'' +
                ", speedCardId='" + speedCardId + '\'' +
                ", riskResult='" + riskResult + '\'' +
                '}';
    }
}
