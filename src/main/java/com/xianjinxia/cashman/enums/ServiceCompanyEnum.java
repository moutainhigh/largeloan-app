package com.xianjinxia.cashman.enums;

import com.sun.org.apache.bcel.internal.generic.PUSH;

/**
 * Created by chunliny on 2018/01/05
 */
public enum ServiceCompanyEnum {

	CASHMAN_APP_PUSH_RISK("CASHMAN-APP-PUSH-RISK","风控发送审核"),

    CASHMAN_APP_PUSH_RISK_XEJD("CASHMAN-APP-PUSH-RISK-XEJD","小额借贷风控审核");
 
    private String code;
    private String text;

    ServiceCompanyEnum(String code, String text) {
        this.code = code;
        this.text = text;
    }

    public String getCode() {
        return code;
    }

    public String getText() {
        return text;
    }
}
