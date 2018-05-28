package com.xianjinxia.cashman.response;

/**
 * 支付宝人工入账返回信息
 *
 * @author JaJian
 * @create 2018-05-14 11:01
 **/
public class AlipayManualIncomeResponse {

    private String repayAccountTime;

    public String getRepayAccountTime() {
        return repayAccountTime;
    }

    public void setRepayAccountTime(String repayAccountTime) {
        this.repayAccountTime = repayAccountTime;
    }

    public AlipayManualIncomeResponse(String repayAccountTime) {
        this.repayAccountTime = repayAccountTime;
    }
}
