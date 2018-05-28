package com.xianjinxia.cashman.dto;

public class SpeedCardNotifyDto {
    private Long userId;

    private Integer speedCardId;

    private Long orderId;

    private String loanResult;


    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getSpeedCardId() {
        return speedCardId;
    }

    public void setSpeedCardId(Integer speedCardId) {
        this.speedCardId = speedCardId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getLoanResult() {
        return loanResult;
    }

    public void setLoanResult(String loanResult) {
        this.loanResult = loanResult;
    }

    @Override
    public String toString() {
        return "SpeedCardNotifyDto{" +
                "userId=" + userId +
                ", speedCardId=" + speedCardId +
                ", orderId=" + orderId +
                ", loanResult='" + loanResult + '\'' +
                '}';
    }
}
