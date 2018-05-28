package com.xianjinxia.cashman.response;

import java.util.Date;

/**
 * @auth whb
 * @date 2017-1-5
 **/

public class TradMqCallback {
    //trad app 放款订单号
    private String bizSeqNo;
    //200 成功 1000 放款中 4000 放款失败
    private String code;

    private String msg;
    //放款时间（只有在放款成功或失败是才有值）
    private Date loanTime;

    public String getBizSeqNo() {
        return bizSeqNo;
    }

    public void setBizSeqNo(String bizSeqNo) {
        this.bizSeqNo = bizSeqNo;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Date getLoanTime() {
        return loanTime;
    }

    public void setLoanTime(Date loanTime) {
        this.loanTime = loanTime;
    }
}
