package com.xianjinxia.cashman.service.repay.paycenter;

import com.xianjingxia.paymentclient.paycenter.params.ComposeData;
import com.xianjingxia.paymentclient.paycenter.params.OrderInfo;
import com.xianjingxia.paymentclient.paycenter.params.UserInfo;
import com.xianjinxia.cashman.dto.PaymentInstallmentDto;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * Created by xuehan on 2017/6/30.
 */
public class RequestPayParam {
    @ApiModelProperty(value = "业务id")
    private String bizId;
    @ApiModelProperty(value = "业务类型")
    private String bizType;
    @ApiModelProperty(value = "支付来源")
    private String requestSource;
    @ApiModelProperty(value = "支付方式")
    private String paymentMethod;
    @ApiModelProperty(value = "组合支付集合")
    private List<ComposeData> composeList;
    @ApiModelProperty(value = " 代扣金额")
    private Long withholdingAmount;
    @ApiModelProperty(value = "收费名称")
    private String executeName;
    @ApiModelProperty(value = "路由策略")
    private String routeStrategy;
    @ApiModelProperty(value = "用户信息")
    private UserInfo userInfo;
    @ApiModelProperty(value = "扩展透传字段")
    private String exextData;
    private OrderInfo oerderInfo;

    @ApiModelProperty(value = "放款订单序列号")
    private String txSerialNo;
    @ApiModelProperty(value = "商户号")
    private String merchant;

    private List<PaymentInstallmentDto> installments;


    public String getBizId() {
        return bizId;
    }

    public void setBizId(String bizId) {
        this.bizId = bizId;
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

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public List<ComposeData> getComposeList() {
        return composeList;
    }

    public void setComposeList(List<ComposeData> composeList) {
        this.composeList = composeList;
    }

    public Long getWithholdingAmount() {
        return withholdingAmount;
    }

    public void setWithholdingAmount(Long withholdingAmount) {
        this.withholdingAmount = withholdingAmount;
    }

    public String getExecuteName() {
        return executeName;
    }

    public void setExecuteName(String executeName) {
        this.executeName = executeName;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public String getRouteStrategy() {
        return routeStrategy;
    }

    public void setRouteStrategy(String routeStrategy) {
        this.routeStrategy = routeStrategy;
    }

    public String getExextData() {
        return exextData;
    }

    public void setExextData(String exextData) {
        this.exextData = exextData;
    }

    public OrderInfo getOerderInfo() {
        return oerderInfo;
    }

    public void setOerderInfo(OrderInfo oerderInfo) {
        this.oerderInfo = oerderInfo;
    }

    public String getTxSerialNo() {
        return txSerialNo;
    }

    public void setTxSerialNo(String txSerialNo) {
        this.txSerialNo = txSerialNo;
    }

    public List<PaymentInstallmentDto> getInstallments() {
        return installments;
    }

    public void setInstallments(List<PaymentInstallmentDto> installments) {
        this.installments = installments;
    }

    public String getMerchant() {
        return merchant;
    }

    public void setMerchant(String merchant) {
        this.merchant = merchant;
    }

    @Override
    public String toString() {
        return "RequestPayParam{" + "bizId='" + bizId + '\'' + ", bizType='" + bizType + '\'' + ", requestSource='" + requestSource + '\'' + ", paymentMethod='" + paymentMethod + '\'' + ", composeList=" + composeList + ", withholdingAmount=" + withholdingAmount + ", executeName='" + executeName + '\'' + ", routeStrategy='" + routeStrategy + '\'' + ", userInfo=" + userInfo + ", exextData='" + exextData + '\'' + ", oerderInfo=" + oerderInfo + ", txSerialNo='" + txSerialNo + '\'' + ", installments=" + installments +", merchant = "+merchant +'}';
    }
}
