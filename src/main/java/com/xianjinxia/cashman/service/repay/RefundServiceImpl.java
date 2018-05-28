package com.xianjinxia.cashman.service.repay;

import com.xianjinxia.cashman.constants.Constant;
import com.xianjinxia.cashman.domain.CashmanRefundLog;
import com.xianjinxia.cashman.domain.RepaymentPlan;
import com.xianjinxia.cashman.domain.RepaymentRecord;
import com.xianjinxia.cashman.exceptions.ServiceException;
import com.xianjinxia.cashman.mapper.CashmanRefundLogMapper;
import com.xianjinxia.cashman.mapper.RepaymentPlanMapper;
import com.xianjinxia.cashman.mapper.RepaymentRecordMapper;
import com.xianjinxia.cashman.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RefundServiceImpl implements IRefundService {

    @Autowired
    private IRepaymentRecordService repaymentRecordService;

    @Autowired
    private RepaymentRecordMapper repaymentRecordMapper;

    @Autowired
    private RepaymentPlanMapper repaymentPlanMapper;

    @Autowired
    private CashmanRefundLogMapper refundLogMapper;

    @Override
    @Transactional
    public void refund(String userPhone, Long repaymentRecordId, Integer refundAmt, String refundTime, String refundChannel, String refundOrderNo) {
        repaymentRecordService.substractRefundAmt(repaymentRecordId, refundAmt);
        RepaymentRecord repaymentRecord = repaymentRecordMapper.selectByPrimaryKey(repaymentRecordId);
        RepaymentPlan repaymentPlan = repaymentPlanMapper.selectByPrimaryKey(repaymentRecord.getRepaymentPlanId());

        CashmanRefundLog cashmanRefundLog = new CashmanRefundLog(
            userPhone,
            repaymentPlan.getLoanOrderId(),
            repaymentPlan.getId(),
            repaymentRecordId,
            refundAmt,
            refundChannel,
            refundOrderNo, DateUtil.yyyyMMddHHmmss2Date(refundTime)
        );
        int count = refundLogMapper.insert(cashmanRefundLog);
        if (count != 1){
            throw new ServiceException(Constant.DB_UPDATE_ERROR);
        }
    }
}
