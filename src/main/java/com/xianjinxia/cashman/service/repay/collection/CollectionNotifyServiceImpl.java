package com.xianjinxia.cashman.service.repay.collection;

import com.alibaba.fastjson.JSON;
import com.xianjinxia.cashman.constants.QueueConstants;
import com.xianjinxia.cashman.domain.ScheduleTaskOverdue;
import com.xjx.mqclient.pojo.MqMessage;
import com.xjx.mqclient.service.MqClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * 催收系统通知:
 * 1. 全部还清之后，通知催收关闭此订单
 * 2. 逾期扫描的任务发现订单逾期后，通知催收
 */
@Component
public class CollectionNotifyServiceImpl implements CollectionNotifyService {

    private static final Logger logger = LoggerFactory.getLogger(CollectionNotifyServiceImpl.class);

    @Autowired
    private MqClient mqClient;

    @Autowired
    private CollectionNotifyDtoBuilder collectionNotifyDtoBuilder;


    public void notify(CollectionNotifyDto collectionNotifyDto) {
        MqMessage message = new MqMessage();
        message.setMessage(JSON.toJSONString(collectionNotifyDto));
        message.setQueueName(QueueConstants.COLLECTION_REPAYMENT_PLAN_OVERDUE_NOTIFY);
        mqClient.sendMessage(message);
        logger.info("推送逾期订单到催收系统:" + collectionNotifyDto);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void notify(ScheduleTaskOverdue scheduleTaskOverdue) {
        logger.info("封装推送逾期订单到催收系统的数据：RepaymentOrderId = " + scheduleTaskOverdue.getRepaymentOrderId());
        CollectionNotifyDto collectionNotifyDto = collectionNotifyDtoBuilder.build(scheduleTaskOverdue.getRepaymentOrderId(), false);
        if (collectionNotifyDto == null) {
            logger.info("推送逾期订单到催收系统时未获取到{}的repaymentPlan:", scheduleTaskOverdue.getRepaymentOrderId());
            return;
        }
        collectionNotifyDto.setScheduleTaskOverdueId(scheduleTaskOverdue.getId());
        this.notify(collectionNotifyDto);
    }

    @Transactional
    public void cancel(CollectionNotifyDto collectionNotifyDto) {
        MqMessage message = new MqMessage();
        message.setMessage(JSON.toJSONString(collectionNotifyDto));
        message.setQueueName(QueueConstants.COLLECTION_REPAYMENT_PLAN_OVERDUE_REPAY);
        mqClient.sendMessage(message);
        logger.info("推送逾期订单已还款到催收系统:" + collectionNotifyDto);
    }

}
