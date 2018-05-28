package com.xianjinxia.cashman.service.repay;

import com.xianjinxia.cashman.service.impl.RepaymentServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest
public class RepaymentRecordServiceImplTest {

    private static final Logger logger = LoggerFactory.getLogger(RepaymentServiceImpl.class);

    @Autowired
    private IRepaymentRecordService repaymentRecordService;


    @Test
    public void updateRepaymentRecordToSuccessTest() {
//        repaymentRecordService.updateRepaymentRecordToSuccess(9l, 6);
    }

}