package com.xianjinxia.cashman.service.repay.jobs;

import com.alibaba.fastjson.JSON;
import com.xianjinxia.cashman.constants.ErrorCodeConstants;
import com.xianjinxia.cashman.domain.LoanOrder;
import com.xianjinxia.cashman.domain.PaymentRequest;
import com.xianjinxia.cashman.domain.PaymentRequestConfig;
import com.xianjinxia.cashman.domain.RepaymentPlan;
import com.xianjinxia.cashman.domain.RepaymentRecord;
import com.xianjinxia.cashman.domain.ScheduleTaskWithhold;
import com.xianjinxia.cashman.dto.PaymentInstallmentDto;
import com.xianjinxia.cashman.enums.PaymentBizTypeEnum;
import com.xianjinxia.cashman.enums.PaymentCenterBizTypeEnum;
import com.xianjinxia.cashman.enums.PaymentRequestStatusEnum;
import com.xianjinxia.cashman.enums.RepaymentPlanOperationFlagEnum;
import com.xianjinxia.cashman.enums.RepaymentPlanStatusEnum;
import com.xianjinxia.cashman.enums.ScheduleTaskStatusEnum;
import com.xianjinxia.cashman.enums.TrdLoanOrderStatusEnum;
import com.xianjinxia.cashman.exceptions.CashmanExceptionBuilder;
import com.xianjinxia.cashman.mapper.LoanOrderMapper;
import com.xianjinxia.cashman.mapper.PaymentRequestConfigMapper;
import com.xianjinxia.cashman.mapper.RepaymentPlanMapper;
import com.xianjinxia.cashman.mapper.ScheduleTaskWithholdMapper;
import com.xianjinxia.cashman.service.repay.IPaymentRequestService;
import com.xianjinxia.cashman.service.repay.IRepaymentPlanService;
import com.xianjinxia.cashman.service.repay.IRepaymentRecordService;
import com.xianjinxia.cashman.service.repay.paycenter.IPayCenterService;
import com.xianjinxia.cashman.utils.DateUtil;
import org.apache.commons.lang.BooleanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

//import com.xianjinxia.cashman.constants.ExceptionMsgConstants;

@Service
public class WithholdJobService {


    private static final Logger logger = LoggerFactory.getLogger(WithholdJobService.class);


    @Autowired
    private RepaymentPlanMapper repaymentPlanMapper;

    @Autowired
    private IRepaymentPlanService repaymentPlanService;

    @Autowired
    private IRepaymentRecordService repaymentRecordService;

    @Autowired
    private ScheduleTaskWithholdMapper scheduleTaskWithholdMapper;

    @Autowired
    private IPaymentRequestService paymentRequestService;

    @Autowired
    private IPayCenterService payCenterService;

    @Autowired
    private PaymentRequestConfigMapper paymentRequestConfigMapper;

    @Autowired
    private LoanOrderMapper loanOrderMapper;


    @Transactional( propagation = Propagation.REQUIRES_NEW)
    public void prepareWithhold(RepaymentPlan repaymentPlan, Date date) {
        // 1.验证待还金额
        Integer withholdAmount = repaymentPlan.getRepaymentTotalAmount();

        if (withholdAmount == 0) {
            logger.warn("还款订单[{}]待还金额为0，定时代扣任务忽略此订单", repaymentPlan.getId());
            return;
        }

        // 2.保存代扣数据
        ScheduleTaskWithhold withholdOrder = new ScheduleTaskWithhold();
        withholdOrder.setStatus(ScheduleTaskStatusEnum.WAITING.getCode());
        withholdOrder.setRepaymentOrderId(repaymentPlan.getId());
        withholdOrder.setDataValid(true);
        withholdOrder.setRetryTimes(0);
        withholdOrder.setWithholdDate(date);
        withholdOrder.setCreatedTime(new Date());
        withholdOrder.setUpdatedTime(new Date());

        try {
            // 取消掉还款计划订单的状态
            LoanOrder loanOrder = loanOrderMapper.selectByTrdOrderId(repaymentPlan.getLoanOrderId());
            if (loanOrder.getStatus().equals(TrdLoanOrderStatusEnum.PUSH_FAIL.getCode())||
                    loanOrder.getStatus().equals(TrdLoanOrderStatusEnum.NEW_PUSH_SUCCESS.getCode())||
                    loanOrder.getStatus().equals(TrdLoanOrderStatusEnum.LOANING.getCode())||
                    loanOrder.getStatus().equals(TrdLoanOrderStatusEnum.MANUAL_REVIEWING.getCode())||
                    loanOrder.getStatus().equals(TrdLoanOrderStatusEnum.NEW.getCode())){
                logger.info("此状态不能代扣,订单loanOrderId为{},订单状态为:{}",loanOrder.getTrdLoanOrderId(),loanOrder.getStatus());
                repaymentPlanMapper.updateStatus(loanOrder.getTrdLoanOrderId(), RepaymentPlanStatusEnum.Canceled.getCode());
                return;
            }


            int withholdTaskInsertCount = scheduleTaskWithholdMapper.insert(withholdOrder);
            if (withholdTaskInsertCount != 1) {
                throw CashmanExceptionBuilder.build(ErrorCodeConstants.DB_UPDATE_ERROR);
            }

            // 3.修改还款计划的OperationFlag字段，避免下个任务重新读取相同的数据
            int repaymentPlanUpdateCount = repaymentPlanMapper.updateWithholdOperationFlagById(repaymentPlan.getId(), RepaymentPlanOperationFlagEnum.WITHHOLD.getCode(), repaymentPlan.getVersion());
            if (repaymentPlanUpdateCount != 1) {
                throw CashmanExceptionBuilder.build(ErrorCodeConstants.DB_UPDATE_ERROR);
            }
        }catch (Exception e){
            logger.error("准备代扣数据发生异常",e);
            throw CashmanExceptionBuilder.build(ErrorCodeConstants.REPAY_WITHHOLD_PREPARE_DATA_ERR);
        }
    }



    @Transactional( propagation = Propagation.REQUIRES_NEW)
    public void executeWithhold(ScheduleTaskWithhold scheduleTaskWithhold, Date date) {
        // 1.验证待还金额
        RepaymentPlan repaymentPlan = repaymentPlanMapper.selectByPrimaryKey(scheduleTaskWithhold.getRepaymentOrderId());
        Integer withholdAmount = repaymentPlan.getRepaymentTotalAmount();
        if (withholdAmount == 0) {
            logger.warn("还款订单[{}]待还金额为0，定时代扣任务忽略此订单", repaymentPlan.getId());
            return;
        }

        // 2.创建支付请求
        Long userId = repaymentPlan.getUserId();
        String extData = JSON.toJSONString(scheduleTaskWithhold);

        PaymentRequestConfig paymentRequestConfig = paymentRequestConfigMapper.selectOne();
        Date currentTime = new Date();
        Date expiredTime = DateUtil.minutesAfter(currentTime, paymentRequestConfig.getExpireMinutes());
        PaymentRequest paymentRequest = paymentRequestService.createPaymentRequest(userId, withholdAmount, PaymentRequestStatusEnum.NEW, PaymentBizTypeEnum.WITHHOLD);


        // 3.创建还款记录
        RepaymentRecord repaymentRecord = repaymentRecordService.buildRepaymentRecord(paymentRequest.getId(), repaymentPlan, PaymentBizTypeEnum.WITHHOLD);
        repaymentRecordService.addRepaymentRecord(repaymentRecord);
        // 4.修改还款计划的在途金额(增加), 续期的不需要增加
        repaymentPlanService.increaseWaitingAmount(repaymentPlan.getId(), repaymentRecord.getAmount(), repaymentRecord.getRepayPrincipalAmt(), repaymentRecord.getRepayInterestAmt(), repaymentRecord.getRepayOverdueAmt(), repaymentPlan.getVersion());


        // 5 MQ的方式提交支付中心
        List<RepaymentPlan> allRepaymentPlansBySingleId = repaymentPlanService.getAllRepaymentPlansBySingleId(scheduleTaskWithhold.getRepaymentOrderId());
        boolean isHistoryOverdue = this.isHistoryOverdue(allRepaymentPlansBySingleId);

        List<PaymentInstallmentDto> paymentInstallmentDtos = new ArrayList<>();
        PaymentInstallmentDto paymentInstallmentDto = new PaymentInstallmentDto();
        // 这里的费用按照匹配到的repayment plan进行封装，
        paymentInstallmentDto.setPeriod(repaymentPlan.getPeriod());
        paymentInstallmentDto.setInstallmentAmount(repaymentRecord.getAmount());
        paymentInstallmentDto.setInstallmentPrincipal(repaymentRecord.getRepayPrincipalAmt());
        paymentInstallmentDto.setInstallInterest(repaymentRecord.getRepayInterestAmt());
        paymentInstallmentDtos.add(paymentInstallmentDto);
        logger.info("提交给支付中心代扣的还款计划列表{}", paymentInstallmentDtos);

        PaymentCenterBizTypeEnum paymentCenterBizTypeEnum = PaymentCenterBizTypeEnum.BIG_AMT_TIMER_WITHHOLD;
        if(isHistoryOverdue){
            paymentCenterBizTypeEnum = PaymentCenterBizTypeEnum.BIG_AMT_OVERDUE_WITHHOLD;
        }

        LoanOrder loanOrder = loanOrderMapper.selectByTrdOrderId(repaymentPlan.getLoanOrderId());
        logger.info("loanOrderId={},bizSeqNo={}",loanOrder.getTrdLoanOrderId(),loanOrder.getBizSeqNo());
        payCenterService.payWithMQ(
                userId.toString(),
                paymentRequest.getId(),
                withholdAmount,
                paymentCenterBizTypeEnum,
                extData,
                isHistoryOverdue,
                false,
                false,
                true,
                paymentInstallmentDtos,
                loanOrder,
                null);
        logger.info("提交代扣还款订单到支付中心[{}], 金额[{}]", repaymentPlan.getId(), withholdAmount);

        // 4 增加重试次数和此代扣任务的状态
        int count = scheduleTaskWithholdMapper.updateWithholdRetryTimesAndStatus(repaymentPlan.getId(), ScheduleTaskStatusEnum.PROCESSING.getCode());
        if (count!=1){
            throw CashmanExceptionBuilder.build(ErrorCodeConstants.REPAY_WITHHOLD_UPDATE_STATUS_ERR);
        }
    }


    private boolean isHistoryOverdue(List<RepaymentPlan> allRepaymentPlanList){
        //1.先判断历史是否有逾期的还款计划
        boolean isHistoryOverdue = false;
        for (Iterator<RepaymentPlan> iterator = allRepaymentPlanList.iterator(); iterator.hasNext(); ) {
            RepaymentPlan repaymentPlan = iterator.next();
            if (BooleanUtils.isFalse(repaymentPlan.getIsOverdue())){
                continue;
            }
            isHistoryOverdue = true;
        }

        return isHistoryOverdue;
    }
}
