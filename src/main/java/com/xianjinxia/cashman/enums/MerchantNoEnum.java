package com.xianjinxia.cashman.enums;

/**
 * Created by liquan on 2018/4/3.
 *
 * @Author: liquan
 * @Description:
 * @Date: Created in 10:54 2018/4/3
 * @Modified By:
 */
public enum MerchantNoEnum {
    MERCHANT_KJQB("kjqb","kjqbde-");
    private String merchantNo;
    private String merchantNoticePrex;

    MerchantNoEnum(String merchantNo, String merchantNoticePrex) {
        this.merchantNo = merchantNo;
        this.merchantNoticePrex = merchantNoticePrex;
    }

    public String getMerchantNo() {
        return merchantNo;
    }

    public void setMerchantNo(String merchantNo) {
        this.merchantNo = merchantNo;
    }

    public String getMerchantNoticePrex() {
        return merchantNoticePrex;
    }

    public void setMerchantNoticePrex(String merchantNoticePrex) {
        this.merchantNoticePrex = merchantNoticePrex;
    }
}
