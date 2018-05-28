package com.xianjinxia.cashman.domain;

import java.math.BigDecimal;
import java.util.Date;

public class LoanOrder {
    private Long id;

    private Long trdLoanOrderId;

    private Long userId;

    private String bizSeqNo;

    private String orderType;

    private Integer orderAmount;

    private Integer feeAmount;

    private Integer paymentAmount;

    private Integer repaymentAmount;

    private Integer periods;

    private Long productId;

    private Integer productCategory;

    private Long userBankCardId;

    private String bankName;

    private String lastFourBankCardNo;

    private String remark;

    private Boolean userType;

    private String userPhone;

    private String userName;

    private String status;

    private String source;

    private String terminal;

    private Date createdTime;

    private String createdUser;

    private Date updatedTime;

    private Boolean dataValid;

    private String traceNo;

    private Integer  interestAmount;

    private String loanUsage;

    private Date loanTime;

    private Date reviewFailTime;

    private String isDepository;//是否存管  "1"表示是 "0"表示否

    private String termUnit;//期数单位

    private BigDecimal termRate;//期利率

    private String merchantNo;//商户号

    private String speedCardId;//提速卡id

    private String speedCardPayStatus;//提速卡支付状态

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTrdLoanOrderId() {
        return trdLoanOrderId;
    }

    public void setTrdLoanOrderId(Long trdLoanOrderId) {
        this.trdLoanOrderId = trdLoanOrderId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getBizSeqNo() {
        return bizSeqNo;
    }

    public void setBizSeqNo(String bizSeqNo) {
        this.bizSeqNo = bizSeqNo == null ? null : bizSeqNo.trim();
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType == null ? null : orderType.trim();
    }

    public Integer getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(Integer orderAmount) {
        this.orderAmount = orderAmount;
    }

    public Integer getFeeAmount() {
        return feeAmount;
    }

    public void setFeeAmount(Integer feeAmount) {
        this.feeAmount = feeAmount;
    }

    public Integer getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(Integer paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public Integer getRepaymentAmount() {
        return repaymentAmount;
    }

    public void setRepaymentAmount(Integer repaymentAmount) {
        this.repaymentAmount = repaymentAmount;
    }

    public Integer getPeriods() {
        return periods;
    }

    public void setPeriods(Integer periods) {
        this.periods = periods;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Integer getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(Integer productCategory) {
        this.productCategory = productCategory;
    }

    public Long getUserBankCardId() {
        return userBankCardId;
    }

    public void setUserBankCardId(Long userBankCardId) {
        this.userBankCardId = userBankCardId;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName == null ? null : bankName.trim();
    }

    public String getLastFourBankCardNo() {
        return lastFourBankCardNo;
    }

    public void setLastFourBankCardNo(String lastFourBankCardNo) {
        this.lastFourBankCardNo = lastFourBankCardNo == null ? null : lastFourBankCardNo.trim();
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public Boolean getUserType() {
        return userType;
    }

    public void setUserType(Boolean userType) {
        this.userType = userType;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone == null ? null : userPhone.trim();
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName == null ? null : userName.trim();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source == null ? null : source.trim();
    }

    public String getTerminal() {
        return terminal;
    }

    public void setTerminal(String terminal) {
        this.terminal = terminal == null ? null : terminal.trim();
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public String getCreatedUser() {
        return createdUser;
    }

    public void setCreatedUser(String createdUser) {
        this.createdUser = createdUser == null ? null : createdUser.trim();
    }

    public Date getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }

    public Boolean getDataValid() {
        return dataValid;
    }

    public void setDataValid(Boolean dataValid) {
        this.dataValid = dataValid;
    }

    public String getTraceNo() {
        return traceNo;
    }

    public void setTraceNo(String traceNo) {
        this.traceNo = traceNo == null ? null : traceNo.trim();
    }

    public Integer getInterestAmount() {
        return interestAmount;
    }

    public void setInterestAmount(Integer interestAmount) {
        this.interestAmount = interestAmount;
    }

    public String getLoanUsage() {
        return loanUsage;
    }

    public void setLoanUsage(String loanUsage) {
        this.loanUsage = loanUsage;
    }

    public String getIsDepository() {
        return isDepository;
    }

    public void setIsDepository(String isDepository) {
        this.isDepository = isDepository;
    }

    public String getTermUnit() {
        return termUnit;
    }

    public void setTermUnit(String termUnit) {
        this.termUnit = termUnit;
    }

    public BigDecimal getTermRate() {
        return termRate;
    }

    public void setTermRate(BigDecimal termRate) {
        this.termRate = termRate;
    }

    public Date getLoanTime() {
        return loanTime;
    }

    public void setLoanTime(Date loanTime) {
        this.loanTime = loanTime;
    }

    public Date getReviewFailTime() {
        return reviewFailTime;
    }

    public void setReviewFailTime(Date reviewFailTime) {
        this.reviewFailTime = reviewFailTime;
    }

    public String getMerchantNo() {
        return merchantNo;
    }

    public void setMerchantNo(String merchantNo) {
        this.merchantNo = merchantNo;
    }

    public String getSpeedCardId() {
        return speedCardId;
    }

    public void setSpeedCardId(String speedCardId) {
        this.speedCardId = speedCardId;
    }

    public LoanOrder() {
    }

    public LoanOrder(String bizSeqNo, String merchantNo) {
        this.bizSeqNo = bizSeqNo;
        this.merchantNo = merchantNo;
    }

    public String getSpeedCardPayStatus() {
        return speedCardPayStatus;
    }

    public void setSpeedCardPayStatus(String speedCardPayStatus) {
        this.speedCardPayStatus = speedCardPayStatus;
    }

    @Override
    public String toString() {
        return "LoanOrder{" +
                "id=" + id +
                ", trdLoanOrderId=" + trdLoanOrderId +
                ", userId=" + userId +
                ", bizSeqNo='" + bizSeqNo + '\'' +
                ", orderType='" + orderType + '\'' +
                ", orderAmount=" + orderAmount +
                ", feeAmount=" + feeAmount +
                ", paymentAmount=" + paymentAmount +
                ", repaymentAmount=" + repaymentAmount +
                ", periods=" + periods +
                ", productId=" + productId +
                ", productCategory=" + productCategory +
                ", userBankCardId=" + userBankCardId +
                ", bankName='" + bankName + '\'' +
                ", lastFourBankCardNo='" + lastFourBankCardNo + '\'' +
                ", remark='" + remark + '\'' +
                ", userType=" + userType +
                ", userPhone='" + userPhone + '\'' +
                ", userName='" + userName + '\'' +
                ", status='" + status + '\'' +
                ", source='" + source + '\'' +
                ", terminal='" + terminal + '\'' +
                ", createdTime=" + createdTime +
                ", createdUser='" + createdUser + '\'' +
                ", updatedTime=" + updatedTime +
                ", dataValid=" + dataValid +
                ", traceNo='" + traceNo + '\'' +
                ", interestAmount=" + interestAmount +
                ", loanUsage='" + loanUsage + '\'' +
                ", loanTime=" + loanTime +
                ", reviewFailTime=" + reviewFailTime +
                ", isDepository='" + isDepository + '\'' +
                ", termUnit='" + termUnit + '\'' +
                ", termRate=" + termRate +
                ", merchantNo='" + merchantNo + '\'' +
                ", speedCardId='" + speedCardId + '\'' +
                ", speedCardPayStatus='" + speedCardPayStatus + '\'' +
                '}';
    }
}