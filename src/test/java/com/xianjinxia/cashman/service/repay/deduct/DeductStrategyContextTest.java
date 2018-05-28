package com.xianjinxia.cashman.service.repay.deduct;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;



@RunWith(SpringRunner.class)
@SpringBootTest
public class DeductStrategyContextTest {


    @Autowired
    private DeductStrategyContext deductStrategyContext;

    @Test
    public void deductTest() {

//        RepaymentDeductDto repaymentDeductDto = new RepaymentDeductDto();
//        repaymentDeductDto.setDeductAmount(100007l);
//        repaymentDeductDto.setDeductType(DeductTypeEnum.AFTER.getType());
//        repaymentDeductDto.setRepaymentPlanId(23l);
//
//        deductStrategyContext.deduct(repaymentDeductDto);
    }

}