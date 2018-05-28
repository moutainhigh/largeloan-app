package com.xianjinxia.cashman.request;

/**
 * Created by liquan on 2018/3/16.
 *
 * @Author: liquan
 * @Description:
 * @Date: Created in 17:13 2018/3/16
 * @Modified By:
 */
public class LoanIndexReq {
    private Integer productCategory;
    private String merchantNo;

    public Integer getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(Integer productCategory) {
        this.productCategory = productCategory;
    }

    public String getMerchantNo() {
        return merchantNo;
    }

    public void setMerchantNo(String merchantNo) {
        this.merchantNo = merchantNo;
    }

    public LoanIndexReq(Integer productCategory, String merchantNo) {
        this.productCategory = productCategory;
        this.merchantNo = merchantNo;
    }

    public LoanIndexReq() {
    }

    @Override
    public String toString() {
        return "LoanIndexReq{" +
                "productCategory=" + productCategory +
                ", merchantNo='" + merchantNo + '\'' +
                '}';
    }
}
