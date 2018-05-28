package com.xianjinxia.cashman.service.loan.risk;

import com.xianjinxia.cashman.enums.RepaymentPlanStatusEnum;
import com.xianjinxia.cashman.enums.TrdLoanOrderStatusEnum;
import com.xianjinxia.cashman.exceptions.ServiceException;
import com.xianjinxia.cashman.mapper.LoanOrderMapper;
import com.xianjinxia.cashman.mapper.RepaymentPlanMapper;
import com.xianjinxia.cashman.response.OpenApiBaseResponse;
import com.xjx.mqclient.service.MqClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

public abstract class RiskCallbackAbstract implements RiskCallback {

    private static final Logger logger = LoggerFactory.getLogger(RiskCallbackAbstract.class);

    @Autowired
    private LoanOrderMapper loanOrderMapper;

    @Autowired
    private RepaymentPlanMapper repaymentPlanMapper;

    @Autowired
    private MqClient mqClient;

    protected void updateLoanOrderStatus(Long loanOrderId, Long trdLoanOrderId, String originStatus, String updatedToStatus) {
        // 判断订单前置状态是否符合条件
        if (!TrdLoanOrderStatusEnum.canUpdate(updatedToStatus, originStatus)) {
            logger.info("订单号:{},订单前置状态为:{},要修改成状态为:{}", loanOrderId, originStatus, updatedToStatus);
            throw new ServiceException(OpenApiBaseResponse.ResponseCode.BIZ_CHECK_FAIL.getValue(), "订单前置状态有误");
        }
        int updateNum;
        if (TrdLoanOrderStatusEnum.REFUSED.getCode().equals(updatedToStatus)) {//审核失败，设置审核失败时间
            updateNum = loanOrderMapper.updateStatus(loanOrderId, updatedToStatus, originStatus, new Date());//乐观锁,防并发更新
            int repaymentNum = repaymentPlanMapper.updateStatus(trdLoanOrderId, RepaymentPlanStatusEnum.Canceled.getCode());
            if (repaymentNum == 0) {
                logger.error("repaymentNum=0,loanOrderId={}", trdLoanOrderId);
            }
        } else {
            updateNum = loanOrderMapper.updateStatus(loanOrderId, updatedToStatus, originStatus, null);//乐观锁,防并发更新
        }

        if (updateNum != 1) {
            throw new ServiceException(OpenApiBaseResponse.ResponseCode.BIZ_CHECK_FAIL.getValue(), "更新订单状态失败");
        }
    }
}
