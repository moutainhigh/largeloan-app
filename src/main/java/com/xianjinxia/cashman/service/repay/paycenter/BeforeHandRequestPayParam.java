package com.xianjinxia.cashman.service.repay.paycenter;

import com.xianjinxia.cashman.dto.PaymentInstallmentDto;

import java.util.Date;
import java.util.List;

public class BeforeHandRequestPayParam {

    private String bizId;

    private String bizType;

    private String requestSource;

    private String routeStrategy;

    private Long amount;

    private String idCardNo;

    private String cardHolder;

    private String phoneNo;

    private Date orderStartTime;

    private String extData;

    private String txSerialNo;

    private Long loanInterest;

    private String repaymentUser;

    private String detailData;

    private  String merchant;

    public String getBizId() {
        return bizId;
    }

    public void setBizId(String bizId) {
        this.bizId = bizId;
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

    public String getRouteStrategy() {
        return routeStrategy;
    }

    public void setRouteStrategy(String routeStrategy) {
        this.routeStrategy = routeStrategy;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public String getIdCardNo() {
        return idCardNo;
    }

    public void setIdCardNo(String idCardNo) {
        this.idCardNo = idCardNo;
    }

    public String getCardHolder() {
        return cardHolder;
    }

    public void setCardHolder(String cardHolder) {
        this.cardHolder = cardHolder;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public Date getOrderStartTime() {
        return orderStartTime;
    }

    public void setOrderStartTime(Date orderStartTime) {
        this.orderStartTime = orderStartTime;
    }

    public String getExtData() {
        return extData;
    }

    public void setExtData(String extData) {
        this.extData = extData;
    }

    public String getTxSerialNo() {
        return txSerialNo;
    }

    public void setTxSerialNo(String txSerialNo) {
        this.txSerialNo = txSerialNo;
    }

    public Long getLoanInterest() {
        return loanInterest;
    }

    public void setLoanInterest(Long loanInterest) {
        this.loanInterest = loanInterest;
    }

    public String getRepaymentUser() {
        return repaymentUser;
    }

    public void setRepaymentUser(String repaymentUser) {
        this.repaymentUser = repaymentUser;
    }

    public String getDetailData() {
        return detailData;
    }

    public void setDetailData(String detailData) {
        this.detailData = detailData;
    }

    public String getMerchant() {
        return merchant;
    }

    public void setMerchant(String merchant) {
        this.merchant = merchant;
    }

    public BeforeHandRequestPayParam() {
    }

    public BeforeHandRequestPayParam(String bizId, String bizType, String requestSource, String routeStrategy, Long amount, String idCardNo, String cardHolder, String phoneNo, Date orderStartTime, String extData, String txSerialNo, Long loanInterest, String repaymentUser, String detailData, String merchant) {
        this.bizId = bizId;
        this.bizType = bizType;
        this.requestSource = requestSource;
        this.routeStrategy = routeStrategy;
        this.amount = amount;
        this.idCardNo = idCardNo;
        this.cardHolder = cardHolder;
        this.phoneNo = phoneNo;
        this.orderStartTime = orderStartTime;
        this.extData = extData;
        this.txSerialNo = txSerialNo;
        this.loanInterest = loanInterest;
        this.repaymentUser = repaymentUser;
        this.detailData = detailData;
        this.merchant = merchant;
    }

    @Override
    public String toString() {
        return "BeforeHandRequestPayParam{" +
                "bizId='" + bizId + '\'' +
                ", bizType='" + bizType + '\'' +
                ", requestSource='" + requestSource + '\'' +
                ", routeStrategy='" + routeStrategy + '\'' +
                ", amount=" + amount +
                ", idCardNo='" + idCardNo + '\'' +
                ", cardHolder='" + cardHolder + '\'' +
                ", phoneNo='" + phoneNo + '\'' +
                ", orderStartTime=" + orderStartTime +
                ", extData='" + extData + '\'' +
                ", txSerialNo='" + txSerialNo + '\'' +
                ", loanInterest=" + loanInterest +
                ", repaymentUser='" + repaymentUser + '\'' +
                ", detailData='" + detailData + '\'' +
                ", merchant='" + merchant + '\'' +
                '}';
    }
}
