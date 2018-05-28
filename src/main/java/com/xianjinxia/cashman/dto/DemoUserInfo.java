package com.xianjinxia.cashman.dto;

import java.util.Date;

/**
 * Created by liquan on 2017/9/30.
 */
public class DemoUserInfo {

    private Long id;

    private  String userName;

    private Long certifactCard;

    private  Integer certifType;

    private Long phoneNo;

    private String address;

    private Date updateTime;

    private Date addTime;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Long getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(Long phoneNo) {
        this.phoneNo = phoneNo;
    }

    public Integer getCertifType() {
        return certifType;
    }

    public void setCertifType(Integer certifType) {
        this.certifType = certifType;
    }

    public Long getCertifactCard() {
        return certifactCard;
    }

    public void setCertifactCard(Long certifactCard) {
        this.certifactCard = certifactCard;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Date getAddTime() {
        return addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }

    @Override
    public String toString(){
        return "DemoUserInfo{" +
                "id = " + id
                +",userName = "+userName
                +",certifactCard = "+certifactCard
                +",certifType = "+certifType
                +",phoneNo = "+phoneNo
                +",address = "+address
                +",updateTime = "+updateTime
                +", addTime = " + addTime +
                '}';
    }
}
