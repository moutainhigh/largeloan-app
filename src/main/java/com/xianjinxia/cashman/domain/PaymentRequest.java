package com.xianjinxia.cashman.domain;

import java.util.Date;

public class PaymentRequest {
    private Long id;

    private Long userId;

    private Integer status;

    private Integer amount;

    private String remark;

    private Date expiredAt;

    private Date createdAt;

    private Date updatedAt;

    private String respOrderId;

    private Date respTime;

    private String respMsg;

    private String paymentType;

    private String paymentChannel;//支付渠道

    private String thirdOrderNo;//第三方订单号

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getRespOrderId() {
        return respOrderId;
    }

    public void setRespOrderId(String respOrderId) {
        this.respOrderId = respOrderId == null ? null : respOrderId.trim();
    }

    public Date getRespTime() {
        return respTime;
    }

    public void setRespTime(Date respTime) {
        this.respTime = respTime;
    }

    public String getRespMsg() {
        return respMsg;
    }

    public void setRespMsg(String respMsg) {
        this.respMsg = respMsg == null ? null : respMsg.trim();
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public Date getExpiredAt() {
        return expiredAt;
    }

    public void setExpiredAt(Date expiredAt) {
        this.expiredAt = expiredAt;
    }

    public String getPaymentChannel() {
        return paymentChannel;
    }

    public void setPaymentChannel(String paymentChannel) {
        this.paymentChannel = paymentChannel;
    }

    public String getThirdOrderNo() {
        return thirdOrderNo;
    }

    public void setThirdOrderNo(String thirdOrderNo) {
        this.thirdOrderNo = thirdOrderNo;
    }

    @Override
    public String toString() {
        return "PaymentRequest{" +
                "id=" + id +
                ", userId=" + userId +
                ", status=" + status +
                ", amount=" + amount +
                ", remark='" + remark + '\'' +
                ", expiredAt=" + expiredAt +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", respOrderId='" + respOrderId + '\'' +
                ", respTime=" + respTime +
                ", respMsg='" + respMsg + '\'' +
                ", paymentType='" + paymentType + '\'' +
                ", paymentChannel='" + paymentChannel + '\'' +
                ", thirdOrderNo='" + thirdOrderNo + '\'' +
                '}';
    }
}