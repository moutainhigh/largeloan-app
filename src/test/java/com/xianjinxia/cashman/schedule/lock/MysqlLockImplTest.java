package com.xianjinxia.cashman.schedule.lock;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MysqlLockImplTest {

    @Autowired
    private MysqlLock mysqlLock;

    @Test
    public void tryLockTest(){

        //@Transactional 在子方法中相当于新起了一个事务
        mysqlLock.tryLock("test-lock", 5l);

        try {
            Thread.sleep(1000000l);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

}