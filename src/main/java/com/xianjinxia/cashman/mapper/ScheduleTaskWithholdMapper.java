package com.xianjinxia.cashman.mapper;

import com.xianjinxia.cashman.domain.ScheduleTaskWithhold;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface ScheduleTaskWithholdMapper {

    int insert(ScheduleTaskWithhold record);

    int updateWithholdStatus(@Param("repaymentOrderId") Long repaymentOrderId, @Param("status") Integer status);

    int updateWithholdRetryTimesAndStatus(@Param("repaymentOrderId") Long repaymentOrderId, @Param("status") Integer status);

    List<ScheduleTaskWithhold> selectWithholdOrderList(@Param("withholdDate") Date withholdDate, @Param("maxRetryTimes") Integer maxRetryTimes, @Param("status") Integer status);
}