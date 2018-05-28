package com.xianjinxia.cashman.schedule;

import com.github.pagehelper.Page;
import com.xianjinxia.cashman.constants.ErrorCodeConstants;
import com.xianjinxia.cashman.domain.RepaymentPlan;
import com.xianjinxia.cashman.enums.RepaymentPlanOperationFlagEnum;
import com.xianjinxia.cashman.enums.RepaymentPlanStatusEnum;
import com.xianjinxia.cashman.exceptions.CashmanExceptionBuilder;
import com.xianjinxia.cashman.mapper.RepaymentPlanMapper;
import com.xianjinxia.cashman.schedule.job.PagebleScanCollectionJob;
import com.xianjinxia.cashman.service.repay.jobs.WithholdJobService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;


@Component
public class HandlerWithHoldPrepareJob extends PagebleScanCollectionJob<RepaymentPlan, List<RepaymentPlan>> {


    private static final Logger logger = LoggerFactory.getLogger(HandlerWithHoldPrepareJob.class);

    @Autowired
    private WithholdJobService withholdService;

    @Autowired
    private RepaymentPlanMapper repaymentPlanMapper;


    @Override
    public void process(List<RepaymentPlan> item) {
        Date date = new Date();
        for (RepaymentPlan repaymentPlan : item) {
            try {
                withholdService.prepareWithhold(repaymentPlan, date);
            }catch (Exception e){
                logger.error("准备代扣数据发生异常",e);
            }

        }
    }

    @Override
    public int pageSize() {
        return 500;
    }

    @Override
    public int threshold() {
        return 3000;
    }

    @Override
    public Page<RepaymentPlan> fetch() {
        Page<RepaymentPlan> pages = (Page<RepaymentPlan>) repaymentPlanMapper.selectByRepaymentPlanTimeAndOperationFlag(new Date(), RepaymentPlanOperationFlagEnum.INIT.getCode(), RepaymentPlanStatusEnum.Waiting.getCode());
        return pages;
    }
}