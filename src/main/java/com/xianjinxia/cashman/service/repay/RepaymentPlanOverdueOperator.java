package com.xianjinxia.cashman.service.repay;

import com.xianjinxia.cashman.domain.RepaymentPlan;

public interface RepaymentPlanOverdueOperator {
    RepaymentPlan checkAndUpdateOverdue(RepaymentPlan repaymentPlan, boolean isUpdateOperationFlag);
}