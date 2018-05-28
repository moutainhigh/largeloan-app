package com.xianjinxia.cashman.service;

import com.xianjinxia.cashman.domain.RepaymentPlan;
import com.xianjinxia.cashman.request.PaymentMessage;

import java.util.Date;
import java.util.List;

/**
 * Created by MJH on 2017/9/1.
 */
public interface IPaymentService {

    void processLoanSuccess(PaymentMessage paymentMessage);

}
