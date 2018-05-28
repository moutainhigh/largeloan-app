package com.xianjinxia.cashman.domain;

import java.util.Date;

/**
 * @author ganminghui
 *
 * 催收系统请求
 */
public class CollectRequest {
    /** 主键、借款计划编号、用户编号、金额(代扣金额/减免金额) */
    private Long id,repaymentPlanId,userId,amount;

    /** 请求状态、催收类型(催收代扣、催收减免)、数据有效性标志、版本标志 */
    private Integer status,collectType,dataValid,version;

    /** 催收流水号、备注信息 */
    private String uuid,remark;

    /** 创建时间、更新时间 */
    private Date createdTime,updatedTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRepaymentPlanId() {
        return repaymentPlanId;
    }

    public void setRepaymentPlanId(Long repaymentPlanId) {
        this.repaymentPlanId = repaymentPlanId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getDataValid() {
        return dataValid;
    }

    public void setDataValid(Integer dataValid) {
        this.dataValid = dataValid;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Integer getCollectType() {
        return collectType;
    }

    public void setCollectType(Integer collectType) {
        this.collectType = collectType;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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
}