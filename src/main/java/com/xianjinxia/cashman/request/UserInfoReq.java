package com.xianjinxia.cashman.request;

public class UserInfoReq extends BaseRequest {
	private Long userId; // 用户id

	public UserInfoReq(Long userId) {
		super();
		this.userId = userId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}
}
