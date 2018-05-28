package com.xianjinxia.cashman.schedule;

import com.github.pagehelper.Page;
import com.xianjinxia.cashman.domain.ScheduleTaskOverdue;
import com.xianjinxia.cashman.mapper.ScheduleTaskOverdueMapper;
import com.xianjinxia.cashman.schedule.job.PagebleScanCollectionJob;
import com.xianjinxia.cashman.service.repay.jobs.OverdueJobService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class HandlerOverdueExecuteJob extends PagebleScanCollectionJob<ScheduleTaskOverdue, List<ScheduleTaskOverdue>> {

    private static final Logger logger = LoggerFactory.getLogger(HandlerOverdueExecuteJob.class);


    @Autowired
    private OverdueJobService overdueJobService;
    
    @Autowired
    private ScheduleTaskOverdueMapper scheduleTaskOverdueMapper;

    @Override
    public void process(List<ScheduleTaskOverdue> item) {
        Date date = new Date();
        for (ScheduleTaskOverdue scheduleTaskOverdue : item) {
            try {
                overdueJobService.calculateOverdueFee(scheduleTaskOverdue, date);
            }catch (Exception e){
                logger.error("计算逾期费用异常：", e);
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
    public Page<ScheduleTaskOverdue> fetch() {
    	Date date = new Date();
        Page<ScheduleTaskOverdue> pages = (Page<ScheduleTaskOverdue>)scheduleTaskOverdueMapper.selectForCalculate(date);
        return pages;
    }
}