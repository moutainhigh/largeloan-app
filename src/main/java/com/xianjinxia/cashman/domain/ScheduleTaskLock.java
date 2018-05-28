package com.xianjinxia.cashman.domain;

import java.util.Date;

public class ScheduleTaskLock {
    private Long id;

    private String lockKey;

    private Long timeOut;

    private Date createTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLockKey() {
        return lockKey;
    }

    public void setLockKey(String lockKey) {
        this.lockKey = lockKey == null ? null : lockKey.trim();
    }

    public Long getTimeOut() {
        return timeOut;
    }

    public void setTimeOut(Long timeOut) {
        this.timeOut = timeOut;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "ScheduleTaskLock{" +
                "id=" + id +
                ", lockKey='" + lockKey + '\'' +
                ", timeOut=" + timeOut +
                ", createTime=" + createTime +
                '}';
    }
}