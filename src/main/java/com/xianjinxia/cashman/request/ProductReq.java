package com.xianjinxia.cashman.request;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author Kevin.Tse
 * @描述 be 产品
 * @create 2017-12-25 16:15
 */
public class ProductReq implements Serializable {

    private static final long serialVersionUID = 1L;
    //产品编号
    private Long productId;
    //产品名称
    private String name;
    //数据有效性 0 无效  1 有效
    private Boolean dataValid;
    //描述
    private String slogan;
    //产品类型 (1:小额   2:大额)
    private Integer productCategory;
    //最小金额
    private Integer minAmount;
    //最大金额
    private Integer maxAmount;
    //借款期限
    private Integer term;
    //期数类型(天/月)
    private String termType;
    //最小期数
    private Integer minPeriods;
    //最大期数
    private Integer maxPeriods;
    //期数
    private Integer periods;
    //冷却时间，单位：天
    private Integer quietPeriod;
    //还款方式
    private String repayMethod;
    //更新时间
    private Date updatedTime;
    //创建人
    private String createdUser;
    //是否支持提前还款
    private Byte isPrepayment;
    //是否支持续期(1表示支持续期 0表示不支持续期)
    private Byte isRenewal;
    //有效开始时间
    private Date startValidDate;
    //创建时间
    private Date createdTime;
    //有效结束时间
    private Date endValidDate;
    //产品期利率
    private BigDecimal periodRate;
    //滞纳金日利率
    private BigDecimal dailyOverdueRate;
    //是否存管(1表示是 2表示否)
    private String isDepository;
    //单日放款最大值（单位是分）
    private Long dayPaymentMaxAmount;

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
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

    public Integer getPeriods() {
        return periods;
    }

    public void setPeriods(Integer periods) {
        this.periods = periods;
    }

    public Integer getQuietPeriod() {
        return quietPeriod;
    }

    public void setQuietPeriod(Integer quietPeriod) {
        this.quietPeriod = quietPeriod;
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

    public BigDecimal getPeriodRate() {
        return periodRate;
    }

    public void setPeriodRate(BigDecimal periodRate) {
        this.periodRate = periodRate;
    }

    public BigDecimal getDailyOverdueRate() {
        return dailyOverdueRate;
    }

    public void setDailyOverdueRate(BigDecimal dailyOverdueRate) {
        this.dailyOverdueRate = dailyOverdueRate;
    }

    public String getIsDepository() {
        return isDepository;
    }

    public void setIsDepository(String isDepository) {
        this.isDepository = isDepository;
    }

    public Long getDayPaymentMaxAmount() {
        return dayPaymentMaxAmount;
    }

    public void setDayPaymentMaxAmount(Long dayPaymentMaxAmount) {
        this.dayPaymentMaxAmount = dayPaymentMaxAmount;
    }

    @Override
    public String toString() {
        return "ProductReq{" +
                "productId=" + productId +
                ", name='" + name + '\'' +
                ", dataValid=" + dataValid +
                ", slogan='" + slogan + '\'' +
                ", productCategory=" + productCategory +
                ", minAmount=" + minAmount +
                ", maxAmount=" + maxAmount +
                ", term=" + term +
                ", termType='" + termType + '\'' +
                ", minPeriods=" + minPeriods +
                ", maxPeriods=" + maxPeriods +
                ", periods=" + periods +
                ", quietPeriod=" + quietPeriod +
                ", repayMethod='" + repayMethod + '\'' +
                ", updatedTime=" + updatedTime +
                ", createdUser='" + createdUser + '\'' +
                ", isPrepayment=" + isPrepayment +
                ", isRenewal=" + isRenewal +
                ", startValidDate=" + startValidDate +
                ", createdTime=" + createdTime +
                ", endValidDate=" + endValidDate +
                ", periodRate=" + periodRate +
                ", dailyOverdueRate=" + dailyOverdueRate +
                ", isDepository='" + isDepository + '\'' +
                ", dayPaymentMaxAmount=" + dayPaymentMaxAmount +
                '}';
    }
}
