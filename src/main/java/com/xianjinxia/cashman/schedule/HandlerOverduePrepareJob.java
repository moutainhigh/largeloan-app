package com.xianjinxia.cashman.schedule;

import com.github.pagehelper.Page;
import com.xianjinxia.cashman.domain.RepaymentPlan;
import com.xianjinxia.cashman.enums.RepaymentPlanOperationFlagEnum;
import com.xianjinxia.cashman.enums.RepaymentPlanStatusEnum;
import com.xianjinxia.cashman.mapper.RepaymentPlanMapper;
import com.xianjinxia.cashman.schedule.job.PagebleScanCollectionJob;
import com.xianjinxia.cashman.service.repay.jobs.OverdueJobService;
import com.xianjinxia.cashman.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class HandlerOverduePrepareJob extends PagebleScanCollectionJob<RepaymentPlan, List<RepaymentPlan>> {


    @Autowired
    private OverdueJobService overdueJobService;

    @Autowired
    private RepaymentPlanMapper repaymentPlanMapper;

    @Override
    public void process(List<RepaymentPlan> item) {
        for (RepaymentPlan repaymentPlan : item) {
            overdueJobService.prepareOverdueData(repaymentPlan);
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
        Date date = DateUtil.daysBefore(new Date(), 1);
        Page<RepaymentPlan> pages = (Page<RepaymentPlan>) repaymentPlanMapper.selectByRepaymentPlanTimeAndOperationFlag(date, RepaymentPlanOperationFlagEnum.WITHHOLD.getCode(), RepaymentPlanStatusEnum.Waiting.getCode());
        return pages;
    }
}