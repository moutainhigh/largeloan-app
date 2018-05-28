package com.xianjinxia.cashman.domain;

import java.util.Date;

public class LoanCapitalInfo {
    private Long id;

    private Long trdLoanOrderId;

    private String capitalPayers;

    private String capitalCompany;

    private String capitalCity;

    private Date createdAt;

    private Date updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTrdLoanOrderId() {
        return trdLoanOrderId;
    }

    public void setTrdLoanOrderId(Long trdLoanOrderId) {
        this.trdLoanOrderId = trdLoanOrderId;
    }

    public String getCapitalPayers() {
        return capitalPayers;
    }

    public void setCapitalPayers(String capitalPayers) {
        this.capitalPayers = capitalPayers;
    }

    public String getCapitalCompany() {
        return capitalCompany;
    }

    public void setCapitalCompany(String capitalCompany) {
        this.capitalCompany = capitalCompany;
    }

    public String getCapitalCity() {
        return capitalCity;
    }

    public void setCapitalCity(String capitalCity) {
        this.capitalCity = capitalCity;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}