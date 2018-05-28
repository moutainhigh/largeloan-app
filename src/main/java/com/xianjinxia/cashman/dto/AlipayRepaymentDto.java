package com.xianjinxia.cashman.dto;

import com.xianjinxia.cashman.idempotent.IdempotentKey;
import com.xianjinxia.cashman.request.BaseRequest;

import javax.validation.constraints.NotNull;

/**
 * 支付宝还款DTO
 *
 * @author JaJian
 * @create 2018-04-12 11:47
 **/
public class AlipayRepaymentDto extends BaseRequest {

    //支付宝账户名称
    private String alipayName;
    //支付宝流水号
    @NotNull(message="alipayOrderNo could not be null")
    private String alipayOrderNo;
    //支付宝账号
    private String alipayPhone;
    //支付宝备注
    private String alipayRemark;
    //支付金额
    private Double moneyAmount;
    //备注姓名
    private String remarkName;
    //备注手机
    private String remarkPhone;
    //支付时间
    private String repayTimeStr;
    //商户号
    @NotNull(message="merchantNum could not be null")
    private String merchantNum;

    public String getAlipayName() {
        return alipayName;
    }

    public void setAlipayName(String alipayName) {
        this.alipayName = alipayName;
    }

    public String getAlipayOrderNo() {
        return alipayOrderNo;
    }

    public void setAlipayOrderNo(String alipayOrderNo) {
        this.alipayOrderNo = alipayOrderNo;
    }

    public String getAlipayPhone() {
        return alipayPhone;
    }

    public void setAlipayPhone(String alipayPhone) {
        this.alipayPhone = alipayPhone;
    }

    public String getAlipayRemark() {
        return alipayRemark;
    }

    public void setAlipayRemark(String alipayRemark) {
        this.alipayRemark = alipayRemark;
    }

    public Double getMoneyAmount() {
        return moneyAmount;
    }

    public void setMoneyAmount(Double moneyAmount) {
        this.moneyAmount = moneyAmount;
    }

    public String getRemarkName() {
        return remarkName;
    }

    public void setRemarkName(String remarkName) {
        this.remarkName = remarkName;
    }

    public String getRemarkPhone() {
        return remarkPhone;
    }

    public void setRemarkPhone(String remarkPhone) {
        this.remarkPhone = remarkPhone;
    }

    public String getRepayTimeStr() {
        return repayTimeStr;
    }

    public void setRepayTimeStr(String repayTimeStr) {
        this.repayTimeStr = repayTimeStr;
    }

    public String getMerchantNum() {
        return merchantNum;
    }

    public void setMerchantNum(String merchantNum) {
        this.merchantNum = merchantNum;
    }
}
