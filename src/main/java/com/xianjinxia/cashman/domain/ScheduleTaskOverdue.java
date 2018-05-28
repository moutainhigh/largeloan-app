package com.xianjinxia.cashman.domain;

import java.util.Date;

public class ScheduleTaskOverdue {
    private Long id;

    private Long userId;

    private Long repaymentOrderId;

    private Boolean isRepaymented;

    private Boolean isCollection;

    private Date lastCalculateTime;

    private Date createdTime;

    private Date updatedTime;

    private Boolean dataValid;

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


    public Long getRepaymentOrderId() {
        return repaymentOrderId;
    }

    public void setRepaymentOrderId(Long repaymentOrderId) {
        this.repaymentOrderId = repaymentOrderId;
    }


    public Boolean getIsRepaymented() {
        return isRepaymented;
    }

    public void setIsRepaymented(Boolean isRepaymented) {
        this.isRepaymented = isRepaymented;
    }

    public Boolean getIsCollection() {
        return isCollection;
    }

    public void setIsCollection(Boolean isCollection) {
        this.isCollection = isCollection;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public Date getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }

    public Boolean getDataValid() {
        return dataValid;
    }

    public void setDataValid(Boolean dataValid) {
        this.dataValid = dataValid;
    }

    public Date getLastCalculateTime() {
        return lastCalculateTime;
    }

    public void setLastCalculateTime(Date lastCalculateTime) {
        this.lastCalculateTime = lastCalculateTime;
    }

    @Override
    public String toString() {
        return "ScheduleTaskOverdue{" +
                "id=" + id +
                ", userId=" + userId +
                ", repaymentOrderId=" + repaymentOrderId +
                ", isRepaymented=" + isRepaymented +
                ", isCollection=" + isCollection +
                ", lastCalculateTime=" + lastCalculateTime +
                ", createdTime=" + createdTime +
                ", updatedTime=" + updatedTime +
                ", dataValid=" + dataValid +
                '}';
    }
}