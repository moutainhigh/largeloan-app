package com.xianjinxia.cashman.dto;

import java.math.BigDecimal;

/**
 * Created by liquan on 2017/11/29.
 */
public class LoanOrderFeeTotalDto {
    /**
     * 费用总和
     */
    private BigDecimal feeAmount;
    /**
     * 利息总和
     */
    private BigDecimal interestAmount;
    /**
     * 借款息费 = 费用总和+利息总和
     */
    private BigDecimal feeAndInterest;

    public LoanOrderFeeTotalDto() {
    }

    public LoanOrderFeeTotalDto(BigDecimal feeAmount, BigDecimal interestAmount, BigDecimal feeAndInterest) {
        this.feeAmount = feeAmount;
        this.interestAmount = interestAmount;
        this.feeAndInterest = feeAndInterest;
    }

    public BigDecimal getFeeAmount() {
        return feeAmount;
    }

    public void setFeeAmount(BigDecimal feeAmount) {
        this.feeAmount = feeAmount;
    }

    public BigDecimal getInterestAmount() {
        return interestAmount;
    }

    public void setInterestAmount(BigDecimal interestAmount) {
        this.interestAmount = interestAmount;
    }

    public BigDecimal getFeeAndInterest() {
        return feeAndInterest;
    }

    public void setFeeAndInterest(BigDecimal feeAndInterest) {
        this.feeAndInterest = feeAndInterest;
    }

    @Override
    public String toString(){
        return "LoanOrderFeeTotalDto{" +
                "feeAmount = " + feeAmount
                +",interestAmount = "+interestAmount
                +",feeAndInterest = "+feeAndInterest+
                '}';
    }
}
