package com.xianjinxia.cashman.dto;

import java.math.BigDecimal;

public class IndexProductsDto {

    private Long id;

    private String name;

    private String slogan;

    private Integer productCategory;

    private BigDecimal minAmount;

    private BigDecimal maxAmount;

    private Integer term;

    private String termType;

    private Integer minPeriods;

    private Integer maxPeriods;

    private Integer quietPeriod;

    private BigDecimal feeRate;

	private String isDepository;


    public IndexProductsDto() {
    }

    public IndexProductsDto(Long id, String name, String slogan, Integer productCategory,
                            BigDecimal minAmount, BigDecimal maxAmount, Integer term,
                            String termType, Integer minPeriods, Integer maxPeriods,
                            Integer quietPeriod, BigDecimal feeRate, String isDepository) {
        this.id = id;
        this.name = name;
        this.slogan = slogan;
        this.productCategory = productCategory;
        this.minAmount = minAmount;
        this.maxAmount = maxAmount;
        this.term = term;
        this.termType = termType;
        this.minPeriods = minPeriods;
        this.maxPeriods = maxPeriods;
        this.quietPeriod = quietPeriod;
        this.feeRate = feeRate;
        this.isDepository = isDepository;
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

    public BigDecimal getMinAmount() {
        return minAmount;
    }

    public void setMinAmount(BigDecimal minAmount) {
        this.minAmount = minAmount;
    }

    public BigDecimal getMaxAmount() {
        return maxAmount;
    }

    public void setMaxAmount(BigDecimal maxAmount) {
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
    
    public String getIsDepository() {
		return isDepository;
	}

	public void setIsDepository(String isDepository) {
		this.isDepository = isDepository;
	}


}
