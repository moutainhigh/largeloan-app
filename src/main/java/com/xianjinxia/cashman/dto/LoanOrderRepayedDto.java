package com.xianjinxia.cashman.dto;

public class LoanOrderRepayedDto {

    private Long loanOrderId;

    public Long getLoanOrderId() {
        return loanOrderId;
    }

    public void setLoanOrderId(Long loanOrderId) {
        this.loanOrderId = loanOrderId;
    }

    @Override
    public String toString() {
        return "LoanOrderRepayedDto{" +
                "loanOrderId=" + loanOrderId +
                '}';
    }
}
