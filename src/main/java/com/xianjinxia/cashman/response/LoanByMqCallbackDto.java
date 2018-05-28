package com.xianjinxia.cashman.response;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by xuehan on 2017/9/7 0027.
 */
public class LoanByMqCallbackDto implements Serializable {
    // bizId
    private String paymentOrderSeqNo;
    /**
     * 交易类型
     */
    private String tradeType;

    /**
     * 来源ID
     */
    private String sourceId;

    /**
     * 支付中心的流水号
     */
    private String paymentOrderNo;

    /**
     * 响应状态码
     */
    private String code;


    /**
     * 响应的message
     */
    private String msg;

    /**
     * 银行交易时间
     */
    private Date bankPayTime;

    /**
     * 支付中心更新时间
     */
    private Date paymentUpdateTime;

    // 代扣透传字段
    private String exextData;



    public String getPaymentOrderSeqNo() {
        return paymentOrderSeqNo;
    }

    public void setPaymentOrderSeqNo(String paymentOrderSeqNo) {
        this.paymentOrderSeqNo = paymentOrderSeqNo;
    }

    public String getTradeType() {
        return tradeType;
    }

    public void setTradeType(String tradeType) {
        this.tradeType = tradeType;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public String getPaymentOrderNo() {
        return paymentOrderNo;
    }

    public void setPaymentOrderNo(String paymentOrderNo) {
        this.paymentOrderNo = paymentOrderNo;
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

    public Date getBankPayTime() {
        return bankPayTime;
    }

    public void setBankPayTime(Date bankPayTime) {
        this.bankPayTime = bankPayTime;
    }

    public Date getPaymentUpdateTime() {
        return paymentUpdateTime;
    }

    public void setPaymentUpdateTime(Date paymentUpdateTime) {
        this.paymentUpdateTime = paymentUpdateTime;
    }

    public String getExextData() {
        return exextData;
    }

    public void setExextData(String exextData) {
        this.exextData = exextData;
    }


}
