package com.xianjinxia.cashman.domain;

/**
 * Created by chunliny on 2018/01/04. 风控推送schedule domain
 */
public class ScheduleTaskRiskControlPush {
	
	private Long id;
	private Long trdLoanOrderId;
	private Long userId;
	private String status;
	private String userName;
	private String userPhone;
	private Boolean userType;
	private String bizSeqNo;
	private Integer productCategory;
	private Long userBankCardId;


	private String merchantNo;


	public String getMerchantNo() {
		return merchantNo;
	}

	public void setMerchantNo(String merchantNo) {
		this.merchantNo = merchantNo;
	}



	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserPhone() {
		return userPhone;
	}
	public void setUserPhone(String userPhone) {
		this.userPhone = userPhone;
	}
	public Boolean getUserType() {
		return userType;
	}
	public void setUserType(Boolean userType) {
		this.userType = userType;
	}
	public String getBizSeqNo() {
		return bizSeqNo;
	}
	public void setBizSeqNo(String bizSeqNo) {
		this.bizSeqNo = bizSeqNo;
	}
	
	public Long getTrdLoanOrderId() {
		return trdLoanOrderId;
	}
	public void setTrdLoanOrderId(Long trdLoanOrderId) {
		this.trdLoanOrderId = trdLoanOrderId;
	}
	
    public Integer getProductCategory() {
		return productCategory;
	}
	public void setProductCategory(Integer productCategory) {
		this.productCategory = productCategory;
	}

	public Long getUserBankCardId() {
		return userBankCardId;
	}

	public void setUserBankCardId(Long userBankCardId) {
		this.userBankCardId = userBankCardId;
	}
}
