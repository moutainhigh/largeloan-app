package com.xianjinxia.cashman.dto;

import java.util.Date;
import java.util.List;

public class PaymentParamDto {

    private String sign;
    private Long amount;//	 优惠券金额
    private String bizId;//  业务id/订单号
    private String bizType;//	业务类型
    private String couponId;//	优惠券ID
    private Long withholdingAmount;//	支付金额
    private String exextData;//	 扩展透传字段
    private Long expireDate;// 过期时间
    private String requestSource;//	来源ID

//    由前端获取并传递
//    private String deviceId;
//    private String mobile;
//    private String payPwd;
//    private String userId;
    private String txSerialNo;//放款订单序列号
    private List<PaymentInstallmentDto> installments;//还款计划明细
    private String routeStrategy;
    /**
     * 商户号
     */
    private String merchant;

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getBizId() {
        return bizId;
    }

    public void setBizId(String bizId) {
        this.bizId = bizId;
    }

    public Long getWithholdingAmount() {
        return withholdingAmount;
    }

    public void setWithholdingAmount(Long withholdingAmount) {
        this.withholdingAmount = withholdingAmount;
    }

    public String getCouponId() {
        return couponId;
    }

    public void setCouponId(String couponId) {
        this.couponId = couponId;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public String getExextData() {
        return exextData;
    }

    public void setExextData(String exextData) {
        this.exextData = exextData;
    }

    public String getBizType() {
        return bizType;
    }

    public void setBizType(String bizType) {
        this.bizType = bizType;
    }

    public String getRequestSource() {
        return requestSource;
    }

    public void setRequestSource(String requestSource) {
        this.requestSource = requestSource;
    }

    public Long getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(Date expireDate) {
        this.expireDate = expireDate.getTime();
    }

    public void setExpireDate(Long expireDate) {
        this.expireDate = expireDate;
    }

    public String getTxSerialNo() {
        return txSerialNo;
    }

    public void setTxSerialNo(String txSerialNo) {
        this.txSerialNo = txSerialNo;
    }

    public List<PaymentInstallmentDto> getInstallments() {
        return installments;
    }

    public void setInstallments(List<PaymentInstallmentDto> installments) {
        this.installments = installments;
    }

    public String getRouteStrategy() {
        return routeStrategy;
    }

    public void setRouteStrategy(String routeStrategy) {
        this.routeStrategy = routeStrategy;
    }

    public String getMerchant() {
        return merchant;
    }

    public void setMerchant(String merchant) {
        this.merchant = merchant;
    }
}