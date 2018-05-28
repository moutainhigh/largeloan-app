package com.xianjinxia.cashman.dto;

public class RiskDto {

    private Integer lastOverdueDayCount;
    private Integer historyOverdueRecordCount;
    private Integer lastRejectDayCount;
    private Integer currentApplyAmount;
    private Integer currentApplyPeriods;

    public Integer getLastOverdueDayCount() {
        return lastOverdueDayCount;
    }

    public void setLastOverdueDayCount(Integer lastOverdueDayCount) {
        this.lastOverdueDayCount = lastOverdueDayCount;
    }

    public Integer getHistoryOverdueRecordCount() {
        return historyOverdueRecordCount;
    }

    public void setHistoryOverdueRecordCount(Integer historyOverdueRecordCount) {
        this.historyOverdueRecordCount = historyOverdueRecordCount;
    }

    public Integer getLastRejectDayCount() {
        return lastRejectDayCount;
    }

    public void setLastRejectDayCount(Integer lastRejectDayCount) {
        this.lastRejectDayCount = lastRejectDayCount;
    }

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
}
