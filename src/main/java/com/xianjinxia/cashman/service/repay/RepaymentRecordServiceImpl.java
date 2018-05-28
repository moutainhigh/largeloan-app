package com.xianjinxia.cashman.service.repay;

import com.xianjinxia.cashman.enums.PaymentRequestStatusEnum;
import com.xianjinxia.cashman.exceptions.ServiceException;
import com.xianjinxia.cashman.constants.Constant;
import com.xianjinxia.cashman.domain.RepaymentPlan;
import com.xianjinxia.cashman.domain.RepaymentRecord;
import com.xianjinxia.cashman.enums.PaymentBizTypeEnum;
import com.xianjinxia.cashman.enums.PaymentOrderTypeEnum;
import com.xianjinxia.cashman.mapper.RepaymentRecordMapper;
import com.xianjinxia.cashman.service.impl.RepaymentServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

@Service
public class RepaymentRecordServiceImpl implements IRepaymentRecordService {

    private static final Logger logger = LoggerFactory.getLogger(RepaymentServiceImpl.class);

    @Autowired
    private RepaymentRecordMapper repaymentRecordMapper;

    @Autowired
    private IRepaymentPlanService repaymentPlanService;


    @Override
    @Transactional
    public void updateRepaymentRecordToSuccess(Long paymentRequestId, List<RepaymentRecord> repaymentRecordList) {
        this.updateReapymentRecordStatus(paymentRequestId, repaymentRecordList, PaymentRequestStatusEnum.SUCCESS);
    }

    @Override
    @Transactional
    public void updateRepaymentRecordToFailure(Long paymentRequestId, List<RepaymentRecord> repaymentRecordList) {
        this.updateReapymentRecordStatus(paymentRequestId, repaymentRecordList, PaymentRequestStatusEnum.FAILURE);
    }

    @Override
    @Transactional
    public void updateRepaymentRecordToCancel(Long paymentRequestId, List<RepaymentRecord> repaymentRecordList) {
        this.updateReapymentRecordStatus(paymentRequestId, repaymentRecordList, PaymentRequestStatusEnum.CANCEL);
    }

//    @Override
//    @Transactional
//    public void updateRepaymentRecordToFreeze(Long paymentRequestId, List<RepaymentRecord> repaymentRecordList) {
//        this.updateReapymentRecordStatus(paymentRequestId, repaymentRecordList, PaymentRequestStatusEnum.FREEZE);
//    }

    private void updateReapymentRecordStatus(Long paymentRequestId, List<RepaymentRecord> repaymentRecordList, PaymentRequestStatusEnum updateToStatus ) {
        int count = repaymentRecordList.size();
        int updateCount = repaymentRecordMapper.updatePaymentOrderStatus(paymentRequestId, updateToStatus.getPreConditionStatus().getCode(), updateToStatus.getCode());

        if (count != updateCount) {
            throw new ServiceException(Constant.DB_UPDATE_ERROR);
        }
    }

    @Override
    @Transactional
    public void addRepaymentRecord(RepaymentRecord repaymentRecord) {
        if (ObjectUtils.isEmpty(repaymentRecord)) {
            throw new ServiceException("新增还款记录不可以为空");
        }
        int count = repaymentRecordMapper.insert(repaymentRecord);
        if (count != 1) {
            throw new ServiceException(Constant.DB_INSERT_ERROR);
        }
    }

    @Override
    @Transactional
    public void addRepaymentRecord(List<RepaymentRecord> repaymentRecordList) {
        if (CollectionUtils.isEmpty(repaymentRecordList)) {
            throw new ServiceException("新增还款记录不可以为空");
        }
        for (Iterator<RepaymentRecord> iterator = repaymentRecordList.iterator(); iterator.hasNext(); ) {
            RepaymentRecord repaymentRecord = iterator.next();
            int count = repaymentRecordMapper.insert(repaymentRecord);
            if (count != 1) {
                throw new ServiceException(Constant.DB_INSERT_ERROR);
            }
        }
    }

    @Override
    @Transactional
    public void substractRefundAmt(Long id, Integer substractRefundAmt) {
        int count = repaymentRecordMapper.updateRefundAmt(id, substractRefundAmt);
        if (count != 1) {
            throw new ServiceException(Constant.DB_UPDATE_ERROR);
        }
    }

    @Override
    public RepaymentRecord buildRepaymentRecord(Long paymentRequestId, RepaymentPlan repaymentPlan, PaymentBizTypeEnum paymentBizTypeEnum) {
        // 计算出还款金额
        Integer repaymentAmount = repaymentPlan.getRepaymentTotalAmount();

        // 生成RepaymentRecord对象，但是不插入db
        RepaymentRecord repaymentRecord = new RepaymentRecord();
        repaymentRecord.setUserId(repaymentPlan.getUserId());
        repaymentRecord.setRepaymentPlanId(repaymentPlan.getId());
        repaymentRecord.setPaymentRequestId(paymentRequestId);
        repaymentRecord.setStatus(PaymentRequestStatusEnum.NEW.getCode());
        repaymentRecord.setAmount(repaymentAmount);
        repaymentRecord.setPaymentType(paymentBizTypeEnum.getCode());
        Date now = new Date();
        repaymentRecord.setCreatedAt(now);
        repaymentRecord.setUpdatedAt(now);
        repaymentRecord.setLoanOrderId(repaymentPlan.getLoanOrderId().toString());
        repaymentRecord.setRemark(paymentBizTypeEnum.getText());
        // 金额的计算，目前只支持每期全额还款的情况
        repaymentRecord.setRepayPrincipalAmt(repaymentPlan.getRepaymentPrincipalAmount());
        repaymentRecord.setRepayOverdueAmt(repaymentPlan.getOverdueFeeAmount());
        repaymentRecord.setRepayInterestAmt(repaymentPlan.getRepaymentInterestAmount());
        logger.info("创建支付订单:{}", repaymentRecord);
        return repaymentRecord;
    }

    @Override
    public RepaymentRecord buildRepaymentRecordByIncomeCms(Long paymentRequestId, RepaymentPlan repaymentPlan, PaymentBizTypeEnum paymentBizTypeEnum) {
        // 计算出还款金额
        Integer repaymentAmount = repaymentPlan.getRepaymentPrincipalAmount() + repaymentPlan.getRepaymentInterestAmount() + repaymentPlan.getOverdueFeeAmount();

        // 生成RepaymentRecord对象，但是不插入db
        RepaymentRecord repaymentRecord = new RepaymentRecord();
        repaymentRecord.setUserId(repaymentPlan.getUserId());
        repaymentRecord.setRepaymentPlanId(repaymentPlan.getId());
        repaymentRecord.setPaymentRequestId(paymentRequestId);
        repaymentRecord.setStatus(PaymentRequestStatusEnum.SUCCESS.getCode());

        repaymentRecord.setPaymentType(PaymentOrderTypeEnum.OFF_LINE.getCode());
        Date now = new Date();
        repaymentRecord.setCreatedAt(now);
        repaymentRecord.setUpdatedAt(now);
        repaymentRecord.setLoanOrderId(repaymentPlan.getLoanOrderId().toString());
        repaymentRecord.setRemark(paymentBizTypeEnum.getText());
        // 金额的计算，目前只支持每期全额还款的情况
        repaymentRecord.setAmount(repaymentAmount);
        repaymentRecord.setRepayPrincipalAmt(repaymentPlan.getRepaymentPrincipalAmount());
        repaymentRecord.setRepayOverdueAmt(repaymentPlan.getOverdueFeeAmount());
        repaymentRecord.setRepayInterestAmt(repaymentPlan.getRepaymentInterestAmount());
        logger.info("创建支付订单:{}", repaymentRecord);
        return repaymentRecord;
    }

    @Override
    public List<RepaymentRecord> buildRepaymentRecord(Long paymentRequestId, List<RepaymentPlan> repaymentPlans, PaymentBizTypeEnum paymentBizTypeEnum) {
        List<RepaymentRecord> repaymentRecords = new ArrayList<>();
        for (Iterator<RepaymentPlan> iterator = repaymentPlans.iterator(); iterator.hasNext(); ) {
            RepaymentPlan repaymentPlan = iterator.next();
            RepaymentRecord repaymentRecord = this.buildRepaymentRecord(paymentRequestId, repaymentPlan, paymentBizTypeEnum);
            repaymentRecords.add(repaymentRecord);
        }
        return repaymentRecords;
    }


    @Override
    public List<RepaymentRecord> getRepaymentRecordsByPaymentRequestId(Long paymentRequestId) {
        List<RepaymentRecord> repaymentRecords = repaymentRecordMapper.selectByPaymentRequestId(paymentRequestId);
        return repaymentRecords;
    }

    @Override
    public List<RepaymentRecord> getRepaymentRecordsByRepaymentPlanId(Long repaymentPlanId) {
        List<RepaymentRecord> repaymentRecords = repaymentRecordMapper.selectByRepaymentPlanId(repaymentPlanId);
        return repaymentRecords;
    }
}
