package com.xianjinxia.cashman.request;

import javax.validation.constraints.NotNull;

public class RefundReq extends BaseRequest{

    @NotNull(message = "还款记录ID不可为空")
    private Long repaymentRecordId;

    @NotNull(message = "用户手机号码不可以为空")
    private String userPhone;

    @NotNull(message = "退款金额不可以为空")
    private Integer refundAmt;

    @NotNull(message = "退款时间不可为空，格式yyyy-MM-dd")
    private String refundTime;

    @NotNull(message = "退款渠道不可以为空")
    private String refundChannel;

    @NotNull(message = "退款订单号不可以为空")
    private String refundOrderNo;

    public Long getRepaymentRecordId() {
        return repaymentRecordId;
    }

    public void setRepaymentRecordId(Long repaymentRecordId) {
        this.repaymentRecordId = repaymentRecordId;
    }

    public Integer getRefundAmt() {
        return refundAmt;
    }

    public void setRefundAmt(Integer refundAmt) {
        this.refundAmt = refundAmt;
    }

    public String getRefundTime() {
        return refundTime;
    }

    public void setRefundTime(String refundTime) {
        this.refundTime = refundTime;
    }

    public String getRefundChannel() {
        return refundChannel;
    }

    public void setRefundChannel(String refundChannel) {
        this.refundChannel = refundChannel;
    }

    public String getRefundOrderNo() {
        return refundOrderNo;
    }

    public void setRefundOrderNo(String refundOrderNo) {
        this.refundOrderNo = refundOrderNo;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }
}
