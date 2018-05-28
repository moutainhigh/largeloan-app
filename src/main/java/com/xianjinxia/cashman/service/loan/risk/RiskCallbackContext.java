package com.xianjinxia.cashman.service.loan.risk;

import com.xianjinxia.cashman.domain.LoanOrder;
import com.xianjinxia.cashman.enums.GroupRiskResultEnum;
import com.xianjinxia.cashman.exceptions.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;

@Service
public class RiskCallbackContext {

    @Autowired
    private List<RiskCallback> riskCallbackList;

    public void process(LoanOrder loanOrder, GroupRiskResultEnum groupRiskResultEnum){
        for (Iterator<RiskCallback> iterator = riskCallbackList.iterator(); iterator.hasNext(); ) {
            RiskCallback riskCallback = iterator.next();
            RiskCallbackResult result = riskCallback.callback(loanOrder, groupRiskResultEnum);
            if (result.isProcessed()){
                return;
            }
        }
        throw new ServiceException("集团风控回调请求未处理成功，没找到匹配的处理类");
    }

}
