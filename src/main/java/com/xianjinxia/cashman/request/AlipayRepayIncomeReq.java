package com.xianjinxia.cashman.request;

import com.xianjinxia.cashman.idempotent.IdempotentKey;

import javax.validation.constraints.NotNull;

public class AlipayRepayIncomeReq extends BaseRequest {

    @NotNull(message = "借款订单ID")
    private Long trdLoanOrderId;

    @NotNull(message = "用户不可以为空")
    private Long userId;

    @NotNull(message = "用户不可以为空")
    private Integer amount;

    @NotNull(message = "入账账号")
    private String incomeAccount;

    @NotNull(message = "第三方支付订单号")
    @IdempotentKey(order=1)
    private String thirdOrderNo;

    @NotNull(message = "支付渠道")
    private String paymentChannel;

    @NotNull(message = "还款入账时间")
    private String repaymentTime;

    @NotNull(message = "还款备注")
    private String remark;

    public Long getTrdLoanOrderId() {
        return trdLoanOrderId;
    }

    public void setTrdLoanOrderId(Long trdLoanOrderId) {
        this.trdLoanOrderId = trdLoanOrderId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public String getIncomeAccount() {
        return incomeAccount;
    }

    public void setIncomeAccount(String incomeAccount) {
        this.incomeAccount = incomeAccount;
    }

    public String getThirdOrderNo() {
        return thirdOrderNo;
    }

    public void setThirdOrderNo(String thirdOrderNo) {
        this.thirdOrderNo = thirdOrderNo;
    }

    public String getPaymentChannel() {
        return paymentChannel;
    }

    public void setPaymentChannel(String paymentChannel) {
        this.paymentChannel = paymentChannel;
    }

    public String getRepaymentTime() {
        return repaymentTime;
    }

    public void setRepaymentTime(String repaymentTime) {
        this.repaymentTime = repaymentTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
