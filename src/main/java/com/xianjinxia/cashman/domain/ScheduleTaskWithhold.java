package com.xianjinxia.cashman.domain;

import java.util.Date;

public class ScheduleTaskWithhold {

    private Long id;

    private Long repaymentOrderId;

    private Integer retryTimes;

    private Date withholdDate;

    private Integer status;

    private Date createdTime;

    private Date updatedTime;

    private Boolean dataValid;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRepaymentOrderId() {
        return repaymentOrderId;
    }

    public void setRepaymentOrderId(Long repaymentOrderId) {
        this.repaymentOrderId = repaymentOrderId;
    }

    public Date getWithholdDate() {
        return withholdDate;
    }

    public void setWithholdDate(Date withholdDate) {
        this.withholdDate = withholdDate;
    }

    public Integer getRetryTimes() {
        return retryTimes;
    }

    public void setRetryTimes(Integer retryTimes) {
        this.retryTimes = retryTimes;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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

    @Override
    public String toString() {
        return "ScheduleTaskWithhold{" +
                "id=" + id +
                ", repaymentOrderId=" + repaymentOrderId +
                ", retryTimes=" + retryTimes +
                ", withholdDate=" + withholdDate +
                ", status=" + status +
                ", createdTime=" + createdTime +
                ", updatedTime=" + updatedTime +
                ", dataValid=" + dataValid +
                '}';
    }
}