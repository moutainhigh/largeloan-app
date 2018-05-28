package com.xianjinxia.cashman.dto;

import com.xianjinxia.cashman.constants.Constant;

public class SmsDto {

    private String msgSource= Constant.SMS_MSG_SOURCE;

    private String msgBussinessId;

    private String telephone;

    private String msgTemplateCode;

    private String values;

    private String merchantNo;

    public SmsDto(String msgBusinessId,String telephone, String msgTemplateCode, String values,String merchantNo) {
        this.msgBussinessId=msgBusinessId;
        this.telephone = telephone;
        this.msgTemplateCode = msgTemplateCode;
        this.values = values;
        this.merchantNo = merchantNo;
    }

    public SmsDto(String msgBusinessId,String telephone, String msgTemplateCode, String values) {
        this.msgBussinessId=msgBusinessId;
        this.telephone = telephone;
        this.msgTemplateCode = msgTemplateCode;
        this.values = values;
    }

    public String getMsgSource() {
        return msgSource;
    }

    public String getMsgBussinessId() {
        return msgBussinessId;
    }

    public String getTelephone() {
        return telephone;
    }

    public String getMsgTemplateCode() {
        return msgTemplateCode;
    }

    public String getValues() {
        return values;
    }

    public String getMerchantNo() {
        return merchantNo;
    }

    public void setMerchantNo(String merchantNo) {
        this.merchantNo = merchantNo;
    }
}
