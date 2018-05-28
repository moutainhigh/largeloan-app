package com.xianjinxia.cashman.schedule;

import com.github.pagehelper.Page;
import com.xianjinxia.cashman.CashmanApplication;
import com.xianjinxia.cashman.domain.ScheduleTaskOverdue;
import com.xianjinxia.cashman.mapper.ScheduleTaskOverdueMapper;
import com.xianjinxia.cashman.service.repay.jobs.OverdueJobService;
import com.xianjinxia.cashman.utils.DateUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest(
        classes = {CashmanApplication.class},
        properties = {"application.yml"},
        webEnvironment = SpringBootTest.WebEnvironment.MOCK
)
public class HandlerOverdueExecuteJobTest {

    private static final Logger logger = LoggerFactory.getLogger(HandlerOverdueExecuteJobTest.class);

    @Autowired
    private HandlerOverdueExecuteJob handlerOverdueExecuteJob;

    @Autowired
    private ScheduleTaskOverdueMapper scheduleTaskOverdueMapper;

    @Autowired
    private OverdueJobService overdueJobService;

    @Test
    public void fetchTest(){
        Date date = DateUtil.daysBefore(new Date(), 1);
        Page<ScheduleTaskOverdue> pages = (Page<ScheduleTaskOverdue>)scheduleTaskOverdueMapper.selectForCalculate(date);

        logger.info("逾期数据：{}",pages.getResult().size());
    }

    @Test
    public void processTest(){
        overdueJobService.calculateOverdueFee(null, new Date());
    }

    @Test
    public void jobTest(){
        handlerOverdueExecuteJob.execute();
    }

    @Test
    public void decimalHalfUpTest(){
        System.out.println(new BigDecimal(101.212111).setScale(0, BigDecimal.ROUND_HALF_UP).intValue());
        System.out.println(new BigDecimal(101.512111).setScale(0, BigDecimal.ROUND_HALF_UP).intValue());
    }
}