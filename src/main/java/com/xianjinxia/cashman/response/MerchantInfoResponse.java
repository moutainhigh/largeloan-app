package com.xianjinxia.cashman.response;

/**
 * Created by liquan on 2018/3/14.
 *
 * @Author: liquan
 * @Description:
 * @Date: Created in 11:38 2018/3/14
 * @Modified By:
 */
public class MerchantInfoResponse {
    private Integer id;

    /**
     * 商户描述
     */
    private String merchantDesc;

    /**
     * 公司简称
     */
    private String companyShortName;
    /**
     * 公司城市
     */
    private String companyCity;
    /**
     * 商户名称
     */
    private String merchantName;

    /**
     * 商户描述
     */
    private String desc;

    /**
     * 组织机构编号
     */
    private String organCode;

    /**
     * 主体公司名称
     */
    private String companyName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getOrganCode() {
        return organCode;
    }

    public void setOrganCode(String organCode) {
        this.organCode = organCode;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getMerchantDesc() {
        return merchantDesc;
    }

    public void setMerchantDesc(String merchantDesc) {
        this.merchantDesc = merchantDesc;
    }

    public String getCompanyShortName() {
        return companyShortName;
    }

    public void setCompanyShortName(String companyShortName) {
        this.companyShortName = companyShortName;
    }

    public String getCompanyCity() {
        return companyCity;
    }

    public void setCompanyCity(String companyCity) {
        this.companyCity = companyCity;
    }

    @Override
    public String toString() {
        return "MerchantInfoResponse{" +
                "id=" + id +
                ", merchantDesc='" + merchantDesc + '\'' +
                ", companyShortName='" + companyShortName + '\'' +
                ", companyCity='" + companyCity + '\'' +
                ", merchantName='" + merchantName + '\'' +
                ", desc='" + desc + '\'' +
                ", organCode='" + organCode + '\'' +
                ", companyName='" + companyName + '\'' +
                '}';
    }
}
