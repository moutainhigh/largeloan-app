package com.xianjinxia.cashman.schedule;

import com.github.pagehelper.Page;
import com.xianjinxia.cashman.domain.ScheduleTaskOverdue;
import com.xianjinxia.cashman.mapper.ScheduleTaskOverdueMapper;
import com.xianjinxia.cashman.schedule.job.PagebleScanCollectionJob;
import com.xianjinxia.cashman.service.repay.collection.CollectionNotifyService;
import com.xianjinxia.cashman.utils.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by liquan on 2017/10/18.
 */
@Service
public class HandlerCollectionNotifyJob extends PagebleScanCollectionJob<ScheduleTaskOverdue, List<ScheduleTaskOverdue>> {

    private static final Logger logger = LoggerFactory.getLogger(HandlerCollectionNotifyJob.class);
    @Autowired
    private CollectionNotifyService collectionNotifyService;

    @Autowired
    private ScheduleTaskOverdueMapper scheduleTaskOverdueMapper;


    @Override
    public void process(List<ScheduleTaskOverdue> item) {
        for (ScheduleTaskOverdue scheduleTaskOverdue : item) {
            try {
                collectionNotifyService.notify(scheduleTaskOverdue);
            }catch (Exception e){
                logger.info("推送给催收失败,repaymentPlanId={}",scheduleTaskOverdue.getRepaymentOrderId());
                continue;
            }
        }
    }

    @Override
    public int pageSize() {
        return 1000;
    }

    @Override
    public int threshold() {
        return 3000;
    }

    @Override
    public Page<ScheduleTaskOverdue> fetch() {
        // 逾期2天通知催收系统
//        Date date = DateUtil.daysBefore(new Date(), 2);
        String yyyyMMdd = DateUtil.yyyyMMdd(new Date());
        Page<ScheduleTaskOverdue> pages = (Page<ScheduleTaskOverdue>) scheduleTaskOverdueMapper.selectForNotifyCollection(yyyyMMdd, false, false);
        return pages;

    }
}
