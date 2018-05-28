package com.xianjinxia.cashman.service.repay.confirm;

public class RepaymentConfirmProcessResult {

	private boolean isProcessed = false;

	public boolean isProcessed() {
		return isProcessed;
	}

	public void setProcessed(boolean processed) {
		isProcessed = processed;
	}

	public RepaymentConfirmProcessResult(boolean isProcessed) {
		this.isProcessed = isProcessed;
	}
}
