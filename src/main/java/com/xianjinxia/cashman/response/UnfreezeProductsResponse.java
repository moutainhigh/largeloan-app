package com.xianjinxia.cashman.response;

/**
 * Created by liquan on 2018/1/2.
 */
public class UnfreezeProductsResponse {

    /**
     * 冷却时间
     */
    private Integer quietPeriod;
    /**
     * 产品表ID
     */
    private Long id;

    private String merchantNo;

    private Integer productCategory ;

    public Integer getQuietPeriod() {
        return quietPeriod;
    }

    public void setQuietPeriod(Integer quietPeriod) {
        this.quietPeriod = quietPeriod;
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

    public Integer getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(Integer productCategory) {
        this.productCategory = productCategory;
    }
}
