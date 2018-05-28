package com.xianjinxia.cashman.domain.contract;

import java.util.Map;

/**
 * Created by liquan on 2017/11/24.
 */
public abstract class BaseContract {
    /**
     * 订单号
     */
    private String loanOrderId;
    /**
     * 用户真实名称
     */
    private String realName;
    /**
     * 身份证掩码
     */
    private String maskIdCardNum;
    /**
     * 丙方公司title
     */
    private String companyTitle;
    /**
     * 借款金额
     */
    private String moneyAmount;
    /**
     * 借款金额中文大写
     */
    private String cnMoneyAmount;
    /**
     * 放款时间
     */
    private String loanTime;
    /**
     * 需要还款时间
     */
    private String loanEndTime;
    /**
     * 借款期数
     */
    private String period;
    /**
     * 借款息费
     */
    private String interest;

    private String source;

    private String merchantNo;

    public String getLoanOrderId() {
        return loanOrderId;
    }

    public void setLoanOrderId(String loanOrderId) {
        this.loanOrderId = loanOrderId;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getMaskIdCardNum() {
        return maskIdCardNum;
    }

    public void setMaskIdCardNum(String maskIdCardNum) {
        this.maskIdCardNum = maskIdCardNum;
    }

    public String getCompanyTitle() {
        return companyTitle;
    }

    public void setCompanyTitle(String companyTitle) {
        this.companyTitle = companyTitle;
    }

    public String getMoneyAmount() {
        return moneyAmount;
    }

    public void setMoneyAmount(String moneyAmount) {
        this.moneyAmount = moneyAmount;
    }

    public String getCnMoneyAmount() {
        return cnMoneyAmount;
    }

    public void setCnMoneyAmount(String cnMoneyAmount) {
        this.cnMoneyAmount = cnMoneyAmount;
    }

    public String getLoanTime() {
        return loanTime;
    }

    public void setLoanTime(String loanTime) {
        this.loanTime = loanTime;
    }

    public String getLoanEndTime() {
        return loanEndTime;
    }

    public void setLoanEndTime(String loanEndTime) {
        this.loanEndTime = loanEndTime;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getInterest() {
        return interest;
    }

    public void setInterest(String interest) {
        this.interest = interest;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getMerchantNo() {
        return merchantNo;
    }

    public void setMerchantNo(String merchantNo) {
        this.merchantNo = merchantNo;
    }

    public abstract Map<String,String> getMapResult();

}
