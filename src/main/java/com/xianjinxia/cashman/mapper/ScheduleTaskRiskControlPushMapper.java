package com.xianjinxia.cashman.mapper;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.xianjinxia.cashman.domain.ScheduleTaskRiskControlPush;
/**
 * Created by chunliny on 2018/01/04
 * schedule风控推送Mapper
 */
public interface ScheduleTaskRiskControlPushMapper {   
    List<ScheduleTaskRiskControlPush> selectRiskControlPushList(@Param("createdTime") Date createdTime,  @Param("status") String status);
    void updateStatus(@Param("id") Long id,  @Param("newStatus") String newStatus, @Param("preStatus") String prStatus,  @Param("remark") String remark);
}
