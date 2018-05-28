package com.xianjinxia.cashman.enums;

import org.codehaus.groovy.runtime.dgmimpl.arrays.IntegerArrayGetAtMetaMethod;

import java.util.ArrayList;
import java.util.List;

public enum ProductCategoryEnum {

    PRODUCT_CATEGORY_SMALL(1, "小额","xe"),
    PRODUCT_CATEGORY_BIG(2, "大额","de"),
    PRODUCT_CATEGORY_SHOPPING(3, "商城订单");

    private Integer code;
    private String value;
    private String statisticType;

    ProductCategoryEnum(Integer code, String value) {
        this.code = code;
        this.value = value;
    }

    ProductCategoryEnum(Integer code, String value, String statisticType) {
        this.code = code;
        this.value = value;
        this.statisticType = statisticType;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getStatisticType() {
        return statisticType;
    }

    public void setStatisticType(String statisticType) {
        this.statisticType = statisticType;
    }

    public static String getValue(Integer code){
        ProductCategoryEnum[] values = ProductCategoryEnum.values();
        for (int i = 0; i < values.length; i++) {
            ProductCategoryEnum productCategoryEnum = values[i];
            if (productCategoryEnum.getCode().equals(code)){
                return productCategoryEnum.getValue();
            }
        }

        return null;
    }

    public static String getStatisticType(Integer code){
        ProductCategoryEnum[] values = ProductCategoryEnum.values();
        for (int i = 0; i < values.length; i++) {
            ProductCategoryEnum productCategoryEnum = values[i];
            if (productCategoryEnum.getCode().equals(code)){
                return productCategoryEnum.getStatisticType();
            }
        }

        return null;
    }
}
