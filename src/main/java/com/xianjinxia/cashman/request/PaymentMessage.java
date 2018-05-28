package com.xianjinxia.cashman.request;


import com.xianjinxia.cashman.domain.LoanCapitalInfo;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * Created by MJH on 2017/9/11.
 */
public class PaymentMessage extends BaseRequest{

    @NotNull(message="trdLoanOrderId can't be null")
    private Long trdLoanOrderId;;

    @NotNull(message="userId can't be null")
    private Long userId;

    @NotNull(message="bizSeqNo can't be null")
    private String bizSeqNo;

    @NotNull(message="orderType can't be null")
    private String orderType;

    @NotNull(message="orderAmount can't be null")
    private Integer orderAmount;

    @NotNull(message="feeAmount can't be null")
    private Integer feeAmount;

    @NotNull(message="paymentAmount can't be null")
    private Integer paymentAmount;

    @NotNull(message="repaymentAmount can't be null")
    private Integer repaymentAmount;

    @NotNull(message="periods can't be null")
    private Integer periods;

    @NotNull(message="productId can't be null")
    private Long productId;

    @NotNull(message="productCategory can't be null")
    private Integer productCategory;

    @NotNull(message="bankName can't be null")
    private String bankName;

    @NotNull(message="lastFourbankCardNo can't be null")
    private String lastFourBankCardNo;

    @NotNull(message="userBankCardId can't be null")
    private Long userBankCardId;

    @NotNull(message="userType can't be null")
    private Boolean userType;

    @NotNull(message="userPhone can't be null")
    private String userPhone;

    @NotNull(message="userName can't be null")
    private String userName;

    //@NotNull(message="interestAmount can't be null")
    private Integer  interestAmount;

    //@NotNull(message="loanUsage can't be null")
    private String loanUsage;

    @NotNull(message="loanCaptionInfo can't be null")
    private LoanCapitalInfo loanCaptionInfo;

    private String remark;

    @NotNull(message="status can't be null")
    private String status;

    @NotNull(message="source can't be null")
    private String source;

    @NotNull(message="terminal can't be null")
    private String terminal;


    /** 实际支付时间 */
    @NotNull(message="paymentTime can't be null")
    private Date paymentTime;

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

    public LoanCapitalInfo getLoanCaptionInfo() {
        return loanCaptionInfo;
    }

    public void setLoanCaptionInfo(LoanCapitalInfo loanCaptionInfo) {
        this.loanCaptionInfo = loanCaptionInfo;
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
        this.bizSeqNo = bizSeqNo;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
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

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getLastFourBankCardNo() {
        return lastFourBankCardNo;
    }

    public void setLastFourBankCardNo(String lastFourBankCardNo) {
        this.lastFourBankCardNo = lastFourBankCardNo;
    }

    public Long getUserBankCardId() {
        return userBankCardId;
    }

    public void setUserBankCardId(Long userBankCardId) {
        this.userBankCardId = userBankCardId;
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
        this.userPhone = userPhone;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTerminal() {
        return terminal;
    }

    public void setTerminal(String terminal) {
        this.terminal = terminal;
    }

    public Date getPaymentTime() {
        return paymentTime;
    }

    public void setPaymentTime(Date paymentTime) {
        this.paymentTime = paymentTime;
    }
}
