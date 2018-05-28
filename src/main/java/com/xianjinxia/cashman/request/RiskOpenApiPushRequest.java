package com.xianjinxia.cashman.request;

import com.xianjinxia.cashman.domain.RiskControlPushData;

public class RiskOpenApiPushRequest {

	private RiskControlPushData taskParams;

	public RiskOpenApiPushRequest(RiskControlPushData taskParams) {
		super();
		this.taskParams = taskParams;
	}

	public RiskControlPushData getTaskParams() {
		return taskParams;
	}

	public void setTaskParams(RiskControlPushData taskParams) {
		this.taskParams = taskParams;
	}
}
