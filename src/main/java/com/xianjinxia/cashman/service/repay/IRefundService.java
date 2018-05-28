package com.xianjinxia.cashman.service.repay;


public interface IRefundService {
    void refund(String userPhone, Long repaymentRecordId, Integer refundAmt, String refundTime, String refundChannel, String refundOrderNo);
}
