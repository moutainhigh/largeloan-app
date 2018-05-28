package com.xianjinxia.cashman.service.repay.confirm;

import com.xianjinxia.cashman.domain.PaymentRequest;
import com.xianjinxia.cashman.enums.PaymentBizTypeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * 单个还款、批量(多笔)还款
 *
 * @author zhangyongjia zyj@xianjinxia.com
 */
@Service("PaymentCallbackProcessorImplByRepay")
public class RepaymentConfirmProcessorImplByRepay extends RepaymentConfirmProcessorAbstract {

    private static final Logger logger = LoggerFactory.getLogger(RepaymentConfirmProcessorImplByRepay.class);

    @Override
    public RepaymentConfirmProcessResult process(RepaymentConfirmProcessParam repaymentConfirmProcessParam) {
        PaymentRequest paymentRequest = repaymentConfirmProcessParam.getPaymentRequest();

        if (!(paymentRequest.getPaymentType().equals(PaymentBizTypeEnum.REPAYMENT.getCode())||paymentRequest.getPaymentType().equals(PaymentBizTypeEnum.BEFOREHAND_PAY.getCode()))) {
            return new RepaymentConfirmProcessResult(false);
        }

        // 执行还款的逻辑
        super.processRepayment(repaymentConfirmProcessParam);

        return new RepaymentConfirmProcessResult(true);
    }
}
