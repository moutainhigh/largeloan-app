package com.xianjinxia.cashman.dto;

public class TradeAppRiskDto {
    private Integer lastRejectDayCount;
    private Integer currentApplyAmount;
    private Integer currentApplyPeriods;

    public Integer getCurrentApplyAmount() {
        return currentApplyAmount;
    }

    public void setCurrentApplyAmount(Integer currentApplyAmount) {
        this.currentApplyAmount = currentApplyAmount;
    }

    public Integer getCurrentApplyPeriods() {
        return currentApplyPeriods;
    }

    public void setCurrentApplyPeriods(Integer currentApplyPeriods) {
        this.currentApplyPeriods = currentApplyPeriods;
    }

    public Integer getLastRejectDayCount() {
        return lastRejectDayCount;
    }

    public void setLastRejectDayCount(Integer lastRejectDayCount) {
        this.lastRejectDayCount = lastRejectDayCount;
    }
}