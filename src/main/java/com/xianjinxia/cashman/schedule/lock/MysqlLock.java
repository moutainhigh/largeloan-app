package com.xianjinxia.cashman.schedule.lock;

/**
 * 互斥锁
 * @author zhangyongjia
 */
public interface MysqlLock {

    boolean tryLock(String key, Long timeOut);
    void unLock(String key);
    void watchAndDeleteTimeOut();
}
