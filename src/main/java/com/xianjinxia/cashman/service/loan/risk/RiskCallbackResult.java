package com.xianjinxia.cashman.service.loan.risk;

class RiskCallbackResult {

    private boolean isProcessed;

    public boolean isProcessed() {
        return isProcessed;
    }

    public void setProcessed(boolean processed) {
        isProcessed = processed;
    }

    public RiskCallbackResult(boolean isProcessed) {
        this.isProcessed = isProcessed;
    }
}
