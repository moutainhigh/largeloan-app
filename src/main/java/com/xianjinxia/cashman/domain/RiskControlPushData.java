package com.xianjinxia.cashman.domain;

public class RiskControlPushData {
	
	private Long userId;
	private Long orderId;
	private String userName;
	private String phoneNumber;
	private String idCard;
	private String userType;
	private String faceImg;
	private String idCardBackImg;
	private String idCardFrontImg;
	private String bankCard;
	
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public Long getOrderId() {
		return orderId;
	}
	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getIdCard() {
		return idCard;
	}
	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}
	public String getUserType() {
		return userType;
	}
	public void setUserType(String userType) {
		this.userType = userType;
	}
	public String getFaceImg() {
		return faceImg;
	}
	public void setFaceImg(String faceImg) {
		this.faceImg = faceImg;
	}
	public String getIdCardBackImg() {
		return idCardBackImg;
	}
	public void setIdCardBackImg(String idCardBackImg) {
		this.idCardBackImg = idCardBackImg;
	}
	public String getIdCardFrontImg() {
		return idCardFrontImg;
	}
	public void setIdCardFrontImg(String idCardFrontImg) {
		this.idCardFrontImg = idCardFrontImg;
	}

	public String getBankCard() {
		return bankCard;
	}

	public void setBankCard(String bankCard) {
		this.bankCard = bankCard;
	}

	@Override
	public String toString() {
		return "RiskControlPushData{" +
				"userId=" + userId +
				", orderId=" + orderId +
				", userName='" + userName + '\'' +
				", phoneNumber='" + phoneNumber + '\'' +
				", idCard='" + idCard + '\'' +
				", userType='" + userType + '\'' +
				", faceImg='" + faceImg + '\'' +
				", idCardBackImg='" + idCardBackImg + '\'' +
				", idCardFrontImg='" + idCardFrontImg + '\'' +
				", bankCard='" + bankCard + '\'' +
				'}';
	}
}