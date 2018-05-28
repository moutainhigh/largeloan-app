package com.xianjinxia.cashman.schedule;

import com.github.pagehelper.Page;
import com.xianjinxia.cashman.domain.ScheduleTaskWithhold;
import com.xianjinxia.cashman.enums.ScheduleTaskStatusEnum;
import com.xianjinxia.cashman.mapper.ScheduleTaskWithholdMapper;
import com.xianjinxia.cashman.schedule.job.PagebleScanCollectionJob;
import com.xianjinxia.cashman.service.repay.jobs.WithholdJobService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class HandlerWithHoldExecuteJob extends PagebleScanCollectionJob<ScheduleTaskWithhold, List<ScheduleTaskWithhold>> {

    private final static  Logger logger = LoggerFactory.getLogger(HandlerWithHoldExecuteJob.class);

    @Autowired
    private WithholdJobService withholdService;

    @Autowired
    private ScheduleTaskWithholdMapper scheduleTaskWithholdMapper;

    @Override
    public void process(List<ScheduleTaskWithhold> item) {
        Date date = new Date();
        for (ScheduleTaskWithhold scheduleTaskWithhold : item) {
            try {
                withholdService.executeWithhold(scheduleTaskWithhold, date);
            }catch (Exception e){
                logger.error(scheduleTaskWithhold.getId()+"代扣执行失败",e);
                continue;
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
    public Page<ScheduleTaskWithhold> fetch() {
        Page<ScheduleTaskWithhold> pages = (Page<ScheduleTaskWithhold>) scheduleTaskWithholdMapper.selectWithholdOrderList(new Date(), 3, ScheduleTaskStatusEnum.WAITING.getCode());
        return pages;
    }
}