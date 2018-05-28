package com.xianjinxia.cashman.dto;

import java.math.BigDecimal;

/**
 * Created by liquan on 2018/1/5.
 */
public class RepaymentNoticeDto {
    private String bizSeqNo;
    private String userPhone;
    private Long trdLoanOrderId;
    private Integer repaymentTotalAmount;
    private Integer isOverdue;
    private String merchantNo;
    //银行卡后四位 last_four_bank_card_no
    private String lastFourBankCardNo;

    public String getBizSeqNo() {
        return bizSeqNo;
    }

    public void setBizSeqNo(String bizSeqNo) {
        this.bizSeqNo = bizSeqNo;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public Long getTrdLoanOrderId() {
        return trdLoanOrderId;
    }

    public void setTrdLoanOrderId(Long trdLoanOrderId) {
        this.trdLoanOrderId = trdLoanOrderId;
    }

    public Integer getRepaymentTotalAmount() {
        return repaymentTotalAmount;
    }

    public void setRepaymentTotalAmount(Integer repaymentTotalAmount) {
        this.repaymentTotalAmount = repaymentTotalAmount;
    }

    public Integer getIsOverdue() {
        return isOverdue;
    }

    public void setIsOverdue(Integer isOverdue) {
        this.isOverdue = isOverdue;
    }

    public String getMerchantNo() {
        return merchantNo;
    }

    public void setMerchantNo(String merchantNo) {
        this.merchantNo = merchantNo;
    }

    public String getLastFourBankCardNo() {
        return lastFourBankCardNo;
    }

    public void setLastFourBankCardNo(String lastFourBankCardNo) {
        this.lastFourBankCardNo = lastFourBankCardNo;
    }

    @Override
    public String toString() {
        return "RepaymentNoticeDto{" +
                "bizSeqNo='" + bizSeqNo + '\'' +
                ", userPhone='" + userPhone + '\'' +
                ", trdLoanOrderId=" + trdLoanOrderId +
                ", repaymentTotalAmount=" + repaymentTotalAmount +
                ", isOverdue=" + isOverdue +
                ", merchantNo='" + merchantNo + '\'' +
                ", lastFourBankCardNo='" + lastFourBankCardNo + '\'' +
                '}';
    }
}
