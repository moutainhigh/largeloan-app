package com.xianjinxia.cashman.domain;

import java.math.BigDecimal;
import java.util.Date;

public class ProductsFeeConfig {
    private Long id;

    private Boolean dataValid;

    private String createdUser;

    private Date createdTime;

    private String updatedUser;

    private Date updatedTime;

    private String feeName;

    private String feeType;

    private BigDecimal feeRate;

    private String description;

    private Long productId;

    private Integer periods;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getDataValid() {
        return dataValid;
    }

    public void setDataValid(Boolean dataValid) {
        this.dataValid = dataValid;
    }

    public String getCreatedUser() {
        return createdUser;
    }

    public void setCreatedUser(String createdUser) {
        this.createdUser = createdUser == null ? null : createdUser.trim();
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public String getUpdatedUser() {
        return updatedUser;
    }

    public void setUpdatedUser(String updatedUser) {
        this.updatedUser = updatedUser == null ? null : updatedUser.trim();
    }

    public Date getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }

    public String getFeeName() {
        return feeName;
    }

    public void setFeeName(String feeName) {
        this.feeName = feeName == null ? null : feeName.trim();
    }

    public String getFeeType() {
        return feeType;
    }

    public void setFeeType(String feeType) {
        this.feeType = feeType == null ? null : feeType.trim();
    }

    public BigDecimal getFeeRate() {
        return feeRate;
    }

    public void setFeeRate(BigDecimal feeRate) {
        this.feeRate = feeRate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Integer getPeriods() {
        return periods;
    }

    public void setPeriods(Integer periods) {
        this.periods = periods;
    }

    @Override
    public String toString() {
        return "ProductsFeeConfig{" + "id=" + id + ", dataValid=" + dataValid + ", createdUser='" + createdUser + '\'' + ", createdTime=" + createdTime + ", updatedUser='" + updatedUser + '\'' + ", updatedTime=" + updatedTime + ", feeName='" + feeName + '\'' + ", feeType='" + feeType + '\'' + ", feeRate=" + feeRate + ", description='" + description + '\'' + ", productId=" + productId + ", periods=" + periods + '}';
    }
}