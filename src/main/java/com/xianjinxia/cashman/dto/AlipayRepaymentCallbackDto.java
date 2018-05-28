package com.xianjinxia.cashman.dto;

/**
 * 支付宝还款入账回调
 *
 * @author JaJian
 * @create 2018-04-17 11:50
 **/
public class AlipayRepaymentCallbackDto {

    private String alipayOrderNo;

    private String status;

    private String remark;

    private int type;

    private String loanOrderNo;

    private String repayAccountTime;

    public String getAlipayOrderNo() {
        return alipayOrderNo;
    }

    public void setAlipayOrderNo(String alipayOrderNo) {
        this.alipayOrderNo = alipayOrderNo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getLoanOrderNo() {
        return loanOrderNo;
    }

    public void setLoanOrderNo(String loanOrderNo) {
        this.loanOrderNo = loanOrderNo;
    }

    public String getRepayAccountTime() {
        return repayAccountTime;
    }

    public void setRepayAccountTime(String repayAccountTime) {
        this.repayAccountTime = repayAccountTime;
    }
}
