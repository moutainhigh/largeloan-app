package com.xianjinxia.cashman.dto;

import com.xianjinxia.cashman.domain.ProductsFeeConfig;

import java.math.BigDecimal;
import java.util.Date;

public class ProductsDto {

    private Long id;

    private String name;

    private Boolean dataValid;

    private String slogan;

    private Integer productCategory;

    private Integer minAmount;

    private Integer maxAmount;

    private Integer term;

    private String termType;

    private Integer minPeriods;

    private Integer maxPeriods;

    private Integer quietPeriod;

    private BigDecimal feeRate;

    private String feeName;

    private String feeType;

    private String repayMethod;

    private Date updatedTime;

    private String createdUser;

    private Byte isPrepayment;

    private Byte isRenewal;

    private Date startValidDate;

    private Date createdTime;

    private Date endValidDate;

    private String isDepository;


    private String merchantNo;

    public void setProductsFeeConfig(ProductsFeeConfig productsFeeConfig){
        this.feeName= productsFeeConfig.getFeeName();
        this.feeType=productsFeeConfig.getFeeType();
        this.feeRate=productsFeeConfig.getFeeRate();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getDataValid() {
        return dataValid;
    }

    public void setDataValid(Boolean dataValid) {
        this.dataValid = dataValid;
    }

    public String getSlogan() {
        return slogan;
    }

    public void setSlogan(String slogan) {
        this.slogan = slogan;
    }

    public Integer getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(Integer productCategory) {
        this.productCategory = productCategory;
    }

    public Integer getMinAmount() {
        return minAmount;
    }

    public void setMinAmount(Integer minAmount) {
        this.minAmount = minAmount;
    }

    public Integer getMaxAmount() {
        return maxAmount;
    }

    public void setMaxAmount(Integer maxAmount) {
        this.maxAmount = maxAmount;
    }

    public Integer getTerm() {
        return term;
    }

    public void setTerm(Integer term) {
        this.term = term;
    }

    public String getTermType() {
        return termType;
    }

    public void setTermType(String termType) {
        this.termType = termType;
    }

    public Integer getMinPeriods() {
        return minPeriods;
    }

    public void setMinPeriods(Integer minPeriods) {
        this.minPeriods = minPeriods;
    }

    public Integer getMaxPeriods() {
        return maxPeriods;
    }

    public void setMaxPeriods(Integer maxPeriods) {
        this.maxPeriods = maxPeriods;
    }

    public Integer getQuietPeriod() {
        return quietPeriod;
    }

    public void setQuietPeriod(Integer quietPeriod) {
        this.quietPeriod = quietPeriod;
    }

    public BigDecimal getFeeRate() {
        return feeRate;
    }

    public void setFeeRate(BigDecimal feeRate) {
        this.feeRate = feeRate;
    }

    public String getFeeName() {
        return feeName;
    }

    public void setFeeName(String feeName) {
        this.feeName = feeName;
    }

    public String getFeeType() {
        return feeType;
    }

    public void setFeeType(String feeType) {
        this.feeType = feeType;
    }

    public String getRepayMethod() {
        return repayMethod;
    }

    public void setRepayMethod(String repayMethod) {
        this.repayMethod = repayMethod;
    }

    public Date getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }

    public String getCreatedUser() {
        return createdUser;
    }

    public void setCreatedUser(String createdUser) {
        this.createdUser = createdUser;
    }

    public Byte getIsPrepayment() {
        return isPrepayment;
    }

    public void setIsPrepayment(Byte isPrepayment) {
        this.isPrepayment = isPrepayment;
    }

    public Byte getIsRenewal() {
        return isRenewal;
    }

    public void setIsRenewal(Byte isRenewal) {
        this.isRenewal = isRenewal;
    }

    public Date getStartValidDate() {
        return startValidDate;
    }

    public void setStartValidDate(Date startValidDate) {
        this.startValidDate = startValidDate;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public Date getEndValidDate() {
        return endValidDate;
    }

    public void setEndValidDate(Date endValidDate) {
        this.endValidDate = endValidDate;
    }

    public String getMerchantNo() {
        return merchantNo;
    }

    public void setMerchantNo(String merchantNo) {
        this.merchantNo = merchantNo;
    }

    public String getIsDepository() {
        return isDepository;
    }

    public void setIsDepository(String isDepository) {
        this.isDepository = isDepository;
    }
}
