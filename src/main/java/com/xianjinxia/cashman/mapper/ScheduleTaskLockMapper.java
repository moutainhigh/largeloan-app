package com.xianjinxia.cashman.mapper;

import com.xianjinxia.cashman.domain.ScheduleTaskLock;

import java.util.List;

public interface ScheduleTaskLockMapper {
    int deleteByPrimaryKey(Long id);

    int deleteByLockKey(String lockKey);

    int insert(ScheduleTaskLock record);

    int insertSelective(ScheduleTaskLock record);

    ScheduleTaskLock selectByPrimaryKey(Long id);

    ScheduleTaskLock selectByLockKey(String lockKey);

    List<ScheduleTaskLock> selectAll();

    int updateByPrimaryKeySelective(ScheduleTaskLock record);

    int updateByPrimaryKey(ScheduleTaskLock record);
}