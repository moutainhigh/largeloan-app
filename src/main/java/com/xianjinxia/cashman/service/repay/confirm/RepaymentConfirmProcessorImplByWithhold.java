package com.xianjinxia.cashman.service.repay.confirm;

import com.xianjinxia.cashman.exceptions.SqlUpdateException;
import com.xianjinxia.cashman.domain.PaymentRequest;
import com.xianjinxia.cashman.domain.RepaymentRecord;
import com.xianjinxia.cashman.domain.ScheduleTaskWithhold;
import com.xianjinxia.cashman.enums.PaymentBizTypeEnum;
import com.xianjinxia.cashman.enums.ScheduleTaskStatusEnum;
import com.xianjinxia.cashman.mapper.ScheduleTaskWithholdMapper;
import com.xianjinxia.cashman.request.PayCenterCallbackReq;
import com.xianjinxia.cashman.request.PaymentCenterCallbackRequestCode;
import com.xianjinxia.cashman.utils.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;

@Service("PaymentCallbackProcessorImplByWithhold")
public class RepaymentConfirmProcessorImplByWithhold extends RepaymentConfirmProcessorAbstract {

    private static final Logger logger = LoggerFactory.getLogger(RepaymentConfirmProcessorImplByWithhold.class);

    @Autowired
    private ScheduleTaskWithholdMapper scheduleTaskWithholdMapper;

    @Override
    public RepaymentConfirmProcessResult process(RepaymentConfirmProcessParam repaymentConfirmProcessParam) {
        PaymentRequest paymentRequest = repaymentConfirmProcessParam.getPaymentRequest();

        if (!paymentRequest.getPaymentType().equals(PaymentBizTypeEnum.WITHHOLD.getCode())) {
            return new RepaymentConfirmProcessResult(false);
        }

        // 执行还款的逻辑
        super.processRepayment(repaymentConfirmProcessParam);

        // 执行定时任务的逻辑（追加对schedule_task_withhold的状态更新操作）
        this.processScheduleWithholdTask(repaymentConfirmProcessParam);

        return new RepaymentConfirmProcessResult(true);
    }

    private void processScheduleWithholdTask(RepaymentConfirmProcessParam repaymentConfirmProcessParam){
        PayCenterCallbackReq payCenterCallbackReq = repaymentConfirmProcessParam.getPayCenterCallbackReq();
        List<RepaymentRecord> repaymentRecordList = repaymentConfirmProcessParam.getRepaymentRecordList();
        String payCenterRespCode = payCenterCallbackReq.getCode();

        //当成功的时候，把定时代扣的还款计划订单修改为还款成功状态
        //当失败的时候，把定时代扣的还款计划订单修改为待处理状态
        ScheduleTaskStatusEnum updateScheduleTaskStatus;

        if (PaymentCenterCallbackRequestCode.SUCCESS.equals(payCenterRespCode)) {
            updateScheduleTaskStatus = ScheduleTaskStatusEnum.SUCCESS;
        } else {
            updateScheduleTaskStatus = ScheduleTaskStatusEnum.WAITING;
        }

        for (Iterator<RepaymentRecord> iterator = repaymentRecordList.iterator(); iterator.hasNext(); ) {
            RepaymentRecord next = iterator.next();
            updateWithholdStatus(next.getRepaymentPlanId(), DateUtil.yyyyMMdd(), updateScheduleTaskStatus);
        }
    }

    private void updateWithholdStatus(Long repaymentOrderId, String withholdDate, ScheduleTaskStatusEnum scheduleTaskStatusEnum) {
        ScheduleTaskWithhold scheduleTaskWithhold = new ScheduleTaskWithhold();
        scheduleTaskWithhold.setRepaymentOrderId(repaymentOrderId);
        scheduleTaskWithhold.setWithholdDate(DateUtil.yyyyMMdd2Date(withholdDate));
        scheduleTaskWithhold.setStatus(scheduleTaskStatusEnum.getCode());

        int count = scheduleTaskWithholdMapper.updateWithholdStatus(repaymentOrderId, scheduleTaskStatusEnum.getCode());
        if (count != 1) {
            throw new SqlUpdateException("修改withhold状态失败, repaymentOrderId:" + repaymentOrderId);
        }
    }
}
