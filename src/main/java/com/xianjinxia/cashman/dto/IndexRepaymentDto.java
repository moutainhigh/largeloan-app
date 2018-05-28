package com.xianjinxia.cashman.dto;

public class IndexRepaymentDto {

    private String status;
    private Integer currentRepaymentAmount;
    private Integer lateDay;
    private String repaymentUrl;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getCurrentRepaymentAmount() {
        return currentRepaymentAmount;
    }

    public void setCurrentRepaymentAmount(Integer currentRepaymentAmount) {
        this.currentRepaymentAmount = currentRepaymentAmount;
    }

    public Integer getLateDay() {
        return lateDay;
    }

    public void setLateDay(Integer lateDay) {
        this.lateDay = lateDay;
    }

    public String getRepaymentUrl() {
        return repaymentUrl;
    }

    public void setRepaymentUrl(String repaymentUrl) {
        this.repaymentUrl = repaymentUrl;
    }
}
