package com.xianjinxia.cashman.service.repay.collection;

import com.xianjinxia.cashman.domain.ScheduleTaskOverdue;

/**
 * 催收系统通知:
 * 1. 全部还清之后，通知催收关闭此订单
 * 2. 逾期扫描的任务发现订单逾期后，通知催收
 */
public interface CollectionNotifyService {
    void notify(CollectionNotifyDto collectionNotifyDto);
    void notify(ScheduleTaskOverdue scheduleTaskOverdue);
    void cancel(CollectionNotifyDto collectionNotifyDto);
}