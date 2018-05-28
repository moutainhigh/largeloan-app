package com.xianjinxia.cashman.service.repay.deduct;

class DeductResult {

    private boolean isMatched;

    public DeductResult(boolean isMatched) {
        this.isMatched = isMatched;
    }

    public boolean isMatched() {
        return isMatched;
    }

    public void setMatched(boolean matched) {
        isMatched = matched;
    }
}