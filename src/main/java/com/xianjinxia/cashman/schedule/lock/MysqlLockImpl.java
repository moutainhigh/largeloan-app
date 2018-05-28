package com.xianjinxia.cashman.schedule.lock;

import com.xianjinxia.cashman.exceptions.ServiceException;
import com.xianjinxia.cashman.domain.ScheduleTaskLock;
import com.xianjinxia.cashman.mapper.ScheduleTaskLockMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

@Component
public class MysqlLockImpl implements MysqlLock {

    private static final Logger logger = LoggerFactory.getLogger(MysqlLockImpl.class);

    @Autowired
    private ScheduleTaskLockMapper scheduleTaskLockMapper;

    @Override
    @Transactional
    public boolean tryLock(String lockKey, Long timeOut) {
        ScheduleTaskLock scheduleTaskLock = new ScheduleTaskLock();
        scheduleTaskLock.setLockKey(lockKey);
        scheduleTaskLock.setTimeOut(timeOut);
        scheduleTaskLock.setCreateTime(new Date());

        try {
            int count = scheduleTaskLockMapper.insert(scheduleTaskLock);
            if (count == 1){
                return true;
            }
        } catch (DuplicateKeyException duplicateKeyException) {
            logger.error("锁已存在:", duplicateKeyException);
            return false;
        } catch (Exception e){
            logger.error("加锁异常:", e);
            return false;
        }
        return false;
    }

    @Override
    @Transactional
    public void unLock(String lockKey) {
        scheduleTaskLockMapper.deleteByLockKey(lockKey);
    }


    @Override
    @Transactional
    public void watchAndDeleteTimeOut() {
        Date currentTime = new Date();
        List<ScheduleTaskLock> scheduleTaskLocks = scheduleTaskLockMapper.selectAll();
        for (Iterator<ScheduleTaskLock> iterator = scheduleTaskLocks.iterator(); iterator.hasNext(); ) {
            ScheduleTaskLock lock = iterator.next();
            // timeout时间单位：minute
            if(currentTime.getTime() - lock.getCreateTime().getTime() > lock.getTimeOut() * 1000 * 60){
                logger.error("锁[key:{}]超时未释放, 锁创建时间：{}， 预设超时时间：{}分钟", lock.getLockKey(), lock.getCreateTime(), lock.getTimeOut(), new ServiceException("锁"+lock.getLockKey()+"超时"));
            }
        }
    }

}
