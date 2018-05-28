package com.xianjinxia.cashman.service.loan.risk;

import com.xianjinxia.cashman.domain.LoanOrder;
import com.xianjinxia.cashman.enums.GroupRiskResultEnum;

public interface RiskCallback {

    RiskCallbackResult callback(LoanOrder loanOrder, GroupRiskResultEnum groupRiskResultEnum);
}
