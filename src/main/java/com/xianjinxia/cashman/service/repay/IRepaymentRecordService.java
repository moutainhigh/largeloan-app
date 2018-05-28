package com.xianjinxia.cashman.service.repay;

import com.xianjinxia.cashman.domain.RepaymentPlan;
import com.xianjinxia.cashman.domain.RepaymentRecord;
import com.xianjinxia.cashman.enums.PaymentBizTypeEnum;

import java.util.List;

public interface IRepaymentRecordService {

    void updateRepaymentRecordToSuccess(Long paymentRequestId, List<RepaymentRecord> repaymentRecordList);
    void updateRepaymentRecordToFailure(Long paymentRequestId, List<RepaymentRecord> repaymentRecordList);
    void updateRepaymentRecordToCancel(Long paymentRequestId, List<RepaymentRecord> repaymentRecordList);
//    void updateRepaymentRecordToFreeze(Long paymentRequestId, List<RepaymentRecord> repaymentRecordList);

    void addRepaymentRecord(RepaymentRecord repaymentRecord);
    void addRepaymentRecord(List<RepaymentRecord> repaymentRecordList);

    void substractRefundAmt(Long id, Integer substractRefundAmt);
    RepaymentRecord buildRepaymentRecord(Long paymentRequestId, RepaymentPlan repaymentPlan, PaymentBizTypeEnum paymentBizTypeEnum);
    RepaymentRecord buildRepaymentRecordByIncomeCms(Long paymentRequestId, RepaymentPlan repaymentPlan, PaymentBizTypeEnum paymentBizTypeEnum);
    List<RepaymentRecord> buildRepaymentRecord(Long paymentRequestId, List<RepaymentPlan> repaymentPlans, PaymentBizTypeEnum paymentBizTypeEnum);


    List<RepaymentRecord> getRepaymentRecordsByPaymentRequestId(Long paymentRequestId);
    List<RepaymentRecord> getRepaymentRecordsByRepaymentPlanId(Long repaymentPlanId);
}
