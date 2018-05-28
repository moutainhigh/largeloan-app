package com.xianjinxia.cashman.request;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * Created by liquan on 2018/1/2.
 */
public class NoticeOrdersReq {
    /**
     * 页码
     */
    @ApiModelProperty(name = "pageNum",value = "页码",example = "1",required = true,dataType = "Integer")
    @NotNull(message = "pageNum couldn't be null")
    private Integer pageNum;
    /**
     * 每页长度
     */
    @ApiModelProperty(name = "pageSize",value = "每页长度",example = "500",required = true,dataType = "Integer")
    @NotNull(message = "pageSize couldn't be null")
    private Integer pageSize;
    /**
     * 待解冻订单审核时间（审核失败时间）=系统时间-冷却时间
     */
    private Date useTime;
    /**
     * 产品表ID
     */
    private Long id;
    /**
     * 订单状态
     */
    private String status;
    private String repayStatus;
    private String merchantNo;

    private Integer categoryType;

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

    public Date getUseTime() {
        return useTime;
    }

    public void setUseTime(Date useTime) {
        this.useTime = useTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMerchantNo() {
        return merchantNo;
    }

    public void setMerchantNo(String merchantNo) {
        this.merchantNo = merchantNo;
    }

    public Integer getCategoryType() {
        return categoryType;
    }

    public void setCategoryType(Integer categoryType) {
        this.categoryType = categoryType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRepayStatus() {
        return repayStatus;
    }

    public void setRepayStatus(String repayStatus) {
        this.repayStatus = repayStatus;
    }

    public NoticeOrdersReq(Integer pageNum, Integer pageSize, Date useTime, Long id, String merchantNo) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.useTime = useTime;
        this.id = id;
        this.merchantNo = merchantNo;
    }

    public NoticeOrdersReq(Date useTime, Long id) {
        this.useTime = useTime;
        this.id = id;
    }

    public NoticeOrdersReq(Date useTime, Long id, String merchantNo,Integer categoryType) {
        this.useTime = useTime;
        this.id = id;
        this.merchantNo = merchantNo;
        this.categoryType = categoryType;
    }

    public NoticeOrdersReq(Long id, String merchantNo, Integer categoryType) {
        this.id = id;
        this.merchantNo = merchantNo;
        this.categoryType = categoryType;
    }

    public NoticeOrdersReq() {
    }

    @Override
    public String toString() {
        return "NoticeOrdersReq{" +
                "pageNum=" + pageNum +
                ", pageSize=" + pageSize +
                ", useTime=" + useTime +
                ", id=" + id +
                ", merchantNo='" + merchantNo + '\'' +
                '}';
    }
}
