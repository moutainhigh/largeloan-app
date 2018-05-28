package com.xianjinxia.cashman.dto;

/**
 * Created by liquan on 2018/4/11.
 *
 * @Author: liquan
 * @Description:
 * @Date: Created in 18:09 2018/4/11
 * @Modified By:
 */
public class SpeedCardRepayDto {
    private Long orderId;
    private String speedCardPayStatus;

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getSpeedCardPayStatus() {
        return speedCardPayStatus;
    }

    public void setSpeedCardPayStatus(String speedCardPayStatus) {
        this.speedCardPayStatus = speedCardPayStatus;
    }
}
