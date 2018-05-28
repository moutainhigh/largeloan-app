package com.xianjinxia.cashman.mapper;

import com.xianjinxia.cashman.domain.ScheduleTaskOverdue;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface ScheduleTaskOverdueMapper {

    int insert(ScheduleTaskOverdue record);

    ScheduleTaskOverdue selectByPrimaryKey(Long id);

    int updateIsCollectionById(@Param("id") Long id, @Param("isCollection") Boolean isCollection);

    int updateIsRepaymentedByOrderId(@Param("repaymentOrderId") Long repaymentOrderId, @Param("isRepaymented") Boolean isRepaymented);

    int updateLastCalculateTimeById(@Param("id") Long id, @Param("lastCalculateTime") Date lastCalculateTime);

    List<ScheduleTaskOverdue> selectForNotifyCollection(@Param("createdTime") String createdTime, @Param("isCollection") Boolean isCollection, @Param("isRepaymented") Boolean isRepaymented);

    List<ScheduleTaskOverdue> selectForCalculate(@Param("lastCalculateTime") Date lastCalculateTime);


}