package com.xianjinxia.cashman.dto;


import java.math.BigDecimal;
import java.util.List;

/**
 * @author whb
 * @date 2017/12/22.
 */
public class CustodyRequestParam {
    private String bizId;
    private String bizType;
    private String requestSource;
    private String routeStrategy;
    private Integer loanAmount;
    private String extData;
    private CustodyLoanUserInfo userInfo;
    private Integer loanMethod;
    private Integer loanTerm;
    private Integer loanInterest;
    private BigDecimal perAnnumRate;
    private Long orderTime;
    private CustodyRepayScheduleInfo repaymentSchedule;
    private List<CustodyRepayScheduleDtailInfo> repaymentScheduleDetail;
    private String merchant;//商户号

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

    public String getRouteStrategy() {
        return routeStrategy;
    }

    public void setRouteStrategy(String routeStrategy) {
        this.routeStrategy = routeStrategy;
    }

    public Integer getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(Integer loanAmount) {
        this.loanAmount = loanAmount;
    }

    public String getExtData() {
        return extData;
    }

    public void setExtData(String extData) {
        this.extData = extData;
    }

    public CustodyLoanUserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(CustodyLoanUserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public Integer getLoanTerm() {
        return loanTerm;
    }

    public void setLoanTerm(Integer loanTerm) {
        this.loanTerm = loanTerm;
    }

    public Integer getLoanInterest() {
        return loanInterest;
    }

    public void setLoanInterest(Integer loanInterest) {
        this.loanInterest = loanInterest;
    }

    public BigDecimal getPerAnnumRate() {
        return perAnnumRate;
    }

    public void setPerAnnumRate(BigDecimal perAnnumRate) {
        this.perAnnumRate = perAnnumRate;
    }

    public Long getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(Long orderTime) {
        if (orderTime == null) {
            this.orderTime = System.currentTimeMillis();
        } else {
            this.orderTime = orderTime;
        }
    }


    public CustodyRepayScheduleInfo getRepaymentSchedule() {
        return repaymentSchedule;
    }

    public void setRepaymentSchedule(CustodyRepayScheduleInfo repaymentSchedule) {
        this.repaymentSchedule = repaymentSchedule;
    }

    public List<CustodyRepayScheduleDtailInfo> getRepaymentScheduleDetail() {
        return repaymentScheduleDetail;
    }

    public void setRepaymentScheduleDetail(List<CustodyRepayScheduleDtailInfo> repaymentScheduleDetail) {
        this.repaymentScheduleDetail = repaymentScheduleDetail;
    }

    public Integer getLoanMethod() {
        return loanMethod;
    }

    public void setLoanMethod(Integer loanMethod) {
        this.loanMethod = loanMethod;
    }

    public String getMerchant() {
        return merchant;
    }

    public void setMerchant(String merchant) {
        this.merchant = merchant;
    }

    @Override
    public String toString() {
        return "CustodyRequestParam{" +
                "bizId='" + bizId + '\'' +
                ", bizType='" + bizType + '\'' +
                ", requestSource='" + requestSource + '\'' +
                ", routeStrategy='" + routeStrategy + '\'' +
                ", loanAmount=" + loanAmount +
                ", extData='" + extData + '\'' +
                ", userInfo=" + userInfo +
                ", loanMethod=" + loanMethod +
                ", loanTerm=" + loanTerm +
                ", loanInterest=" + loanInterest +
                ", perAnnumRate=" + perAnnumRate +
                ", orderTime=" + orderTime +
                ", repaymentSchedule=" + repaymentSchedule +
                ", repaymentScheduleDetail=" + repaymentScheduleDetail +
                ", merchant='" + merchant + '\'' +
                '}';
    }
}
