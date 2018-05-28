package com.xianjinxia.cashman.request;

/**
 * Created by liujun on 2017/9/30.
 */
public class TestDemoReq extends BaseRequest{

    private Long id;

    private  String userName;

    private Long certifactCard;

    private  Integer certifType;

    private Long phoneNo;

    private String address;

    private Integer pageNum;

    private Integer pageSize;

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

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
}
