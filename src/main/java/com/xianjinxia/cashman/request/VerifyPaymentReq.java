package com.xianjinxia.cashman.request;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;

public class VerifyPaymentReq extends BaseRequest {

    @NotNull(message="userId couldn't be null")
    @ApiModelProperty(name = "userId",value = "用户ID",example = "5000",required = true,dataType = "Integer")
    private Integer userId;

    @NotNull(message="bizId couldn't be null")
    @ApiModelProperty(name = "bizId",value = "业务ID",example = "5",required = true,dataType = "Integer")
    private Long bizId;

    @NotNull(message="withholdingAmount couldn't be null")
    @ApiModelProperty(name = "withholdingAmount",value = "支付金额",example = "10000",required = true,dataType = "Integer")
    private Integer withholdingAmount;

    @NotNull(message="userId couldn't be null")
    @ApiModelProperty(name = "bizType",value = "请求类型",example = "428",required = true,dataType = "String")
    private String bizType;

    @NotNull(message="requestSource couldn't be null")
    @ApiModelProperty(name = "requestSource",value = "请求来源",example = "428",required = true,dataType = "String")
    private String requestSource;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Long getBizId() {
        return bizId;
    }

    public void setBizId(Long bizId) {
        this.bizId = bizId;
    }

    public Integer getWithholdingAmount() {
        return withholdingAmount;
    }

    public void setWithholdingAmount(Integer withholdingAmount) {
        this.withholdingAmount = withholdingAmount;
    }

    public String getBizType() {
        return bizType;
    }

    public void setBizType(String bizType) {
        this.bizType = bizType;
    }

    public String getRequestSource() {
        return requestSource;
    }

    public void setRequestSource(String requestSource) {
        this.requestSource = requestSource;
    }

    @Override
    public String toString() {
        return "VerifyPaymentReq{" +
                "userId=" + userId +
                ", bizId=" + bizId +
                ", withholdingAmount=" + withholdingAmount +
                ", bizType='" + bizType + '\'' +
                ", requestSource='" + requestSource + '\'' +
                '}';
    }
}
