package com.xianjinxia.cashman.dto;

public class UserDetailDto {
	private String userName= "";
	private String realname= "";
	private String idNumber= "";
	private String userSex= "";
	private String userAge= "";
	private String userPhone= "";
	private String education= "";
	private String maritalStatus= "";
	private String presentAddress= "";
	private String presentAddressDistinct= "";
	private String presentLatitude= "";
	private String presentLongitude= "";
	private String presentPeriod= "";
	private String idcardImgZ= "";
	private String idcardImgF= "";
	private String headPortrait= "";
	private String firstContactName= "";
	private String firstContactPhone= "";
	private String fristContactRelation= "";
	private String secondContactName= "";
	private String secondContactPhone= "";
	private String bankCard="";

	@Override
	public String toString() {
		return "UserDetailDto [userName=" + userName + ", realname=" + realname
				+ ", idNumber=" + idNumber + ", userSex=" + userSex
				+ ", userAge=" + userAge + ", userPhone=" + userPhone
				+ ", education=" + education + ", maritalStatus="
				+ maritalStatus + ", presentAddress=" + presentAddress
				+ ", presentAddressDistinct=" + presentAddressDistinct
				+ ", presentLatitude=" + presentLatitude
				+ ", presentLongitude=" + presentLongitude + ", presentPeriod="
				+ presentPeriod + ", idcardImgZ=" + idcardImgZ
				+ ", idcardImgF=" + idcardImgF + ", headPortrait="
				+ headPortrait + ", firstContactName=" + firstContactName
				+ ", firstContactPhone=" + firstContactPhone
				+ ", fristContactRelation=" + fristContactRelation
				+ ", secondContactName=" + secondContactName
				+ ", secondContactPhone=" + secondContactPhone
				+ ", secondContactRelation=" + secondContactRelation
				+ ", userContactSize=" + userContactSize + ", getUserName()="
				+ getUserName() + ", getRealname()=" + getRealname()
				+ ", getIdNumber()=" + getIdNumber() + ", getUserSex()="
				+ getUserSex() + ", getUserAge()=" + getUserAge()
				+ ", getUserPhone()=" + getUserPhone() + ", getEducation()="
				+ getEducation() + ", getMaritalStatus()=" + getMaritalStatus()
				+ ", getPresentAddress()=" + getPresentAddress()
				+ ", getPresentAddressDistinct()="
				+ getPresentAddressDistinct() + ", getPresentLatitude()="
				+ getPresentLatitude() + ", getPresentLongitude()="
				+ getPresentLongitude() + ", getPresentPeriod()="
				+ getPresentPeriod() + ", getIdcardImgZ()=" + getIdcardImgZ()
				+ ", getIdcardImgF()=" + getIdcardImgF()
				+ ", getHeadPortrait()=" + getHeadPortrait()
				+ ", getFirstContactName()=" + getFirstContactName()
				+ ", getFirstContactPhone()=" + getFirstContactPhone()
				+ ", getFristContactRelation()=" + getFristContactRelation()
				+ ", getSecondContactName()=" + getSecondContactName()
				+ ", getSecondContactPhone()=" + getSecondContactPhone()
				+ ", getSecondContactRelation()=" + getSecondContactRelation()
				+ ", getUserContactSize()=" + getUserContactSize()
				+ ", getClass()=" + getClass() + ", hashCode()=" + hashCode()
				+ ", toString()=" + super.toString() + "]";
	}

	private String secondContactRelation;
	private String userContactSize;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getRealname() {
		return realname;
	}

	public void setRealname(String realname) {
		this.realname = realname;
	}

	public String getIdNumber() {
		return idNumber;
	}

	public void setIdNumber(String idNumber) {
		this.idNumber = idNumber;
	}

	public String getUserSex() {
		return userSex;
	}

	public void setUserSex(String userSex) {
		this.userSex = userSex;
	}

	public String getUserAge() {
		return userAge;
	}

	public void setUserAge(String userAge) {
		this.userAge = userAge;
	}

	public String getUserPhone() {
		return userPhone;
	}

	public void setUserPhone(String userPhone) {
		this.userPhone = userPhone;
	}

	public String getEducation() {
		return education;
	}

	public void setEducation(String education) {
		this.education = education;
	}

	public String getMaritalStatus() {
		return maritalStatus;
	}

	public void setMaritalStatus(String maritalStatus) {
		this.maritalStatus = maritalStatus;
	}

	public String getPresentAddress() {
		return presentAddress;
	}

	public void setPresentAddress(String presentAddress) {
		this.presentAddress = presentAddress;
	}

	public String getPresentAddressDistinct() {
		return presentAddressDistinct;
	}

	public void setPresentAddressDistinct(String presentAddressDistinct) {
		this.presentAddressDistinct = presentAddressDistinct;
	}

	public String getPresentLatitude() {
		return presentLatitude;
	}

	public void setPresentLatitude(String presentLatitude) {
		this.presentLatitude = presentLatitude;
	}

	public String getPresentLongitude() {
		return presentLongitude;
	}

	public void setPresentLongitude(String presentLongitude) {
		this.presentLongitude = presentLongitude;
	}

	public String getPresentPeriod() {
		return presentPeriod;
	}

	public void setPresentPeriod(String presentPeriod) {
		this.presentPeriod = presentPeriod;
	}

	public String getIdcardImgZ() {
		return idcardImgZ;
	}

	public void setIdcardImgZ(String idcardImgZ) {
		this.idcardImgZ = idcardImgZ;
	}

	public String getIdcardImgF() {
		return idcardImgF;
	}

	public void setIdcardImgF(String idcardImgF) {
		this.idcardImgF = idcardImgF;
	}

	public String getHeadPortrait() {
		return headPortrait;
	}

	public void setHeadPortrait(String headPortrait) {
		this.headPortrait = headPortrait;
	}

	public String getFirstContactName() {
		return firstContactName;
	}

	public void setFirstContactName(String firstContactName) {
		this.firstContactName = firstContactName;
	}

	public String getFirstContactPhone() {
		return firstContactPhone;
	}

	public void setFirstContactPhone(String firstContactPhone) {
		this.firstContactPhone = firstContactPhone;
	}

	public String getFristContactRelation() {
		return fristContactRelation;
	}

	public void setFristContactRelation(String fristContactRelation) {
		this.fristContactRelation = fristContactRelation;
	}

	public String getSecondContactName() {
		return secondContactName;
	}

	public void setSecondContactName(String secondContactName) {
		this.secondContactName = secondContactName;
	}

	public String getSecondContactPhone() {
		return secondContactPhone;
	}

	public void setSecondContactPhone(String secondContactPhone) {
		this.secondContactPhone = secondContactPhone;
	}

	public String getSecondContactRelation() {
		return secondContactRelation;
	}

	public void setSecondContactRelation(String secondContactRelation) {
		this.secondContactRelation = secondContactRelation;
	}

	public String getUserContactSize() {
		return userContactSize;
	}

	public void setUserContactSize(String userContactSize) {
		this.userContactSize = userContactSize;
	}

	public String getBankCard() {
		return bankCard;
	}

	public void setBankCard(String bankCard) {
		this.bankCard = bankCard;
	}
}
