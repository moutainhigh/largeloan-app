package com.xianjinxia.cashman.service.repay;

import com.alibaba.fastjson.JSON;
import com.xianjinxia.cashman.domain.RepaymentPlan;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RepaymentPlanSpliterTest {

    @Autowired
    private RepaymentPlanSpliter repaymentPlanSpliter;


    @Test
    public void testSplitByRepayAmount(){
        Integer repayAmt = 1000;

        ArrayList<RepaymentPlan> repaymentPlans = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            RepaymentPlan repaymentPlan = new RepaymentPlan();
            repaymentPlan.setRepaymentTotalAmount(1000);
            repaymentPlan.setRepaymentPrincipalAmount(900);
            repaymentPlan.setRepaymentInterestAmount(100);
            repaymentPlan.setOverdueFeeAmount(0);
            repaymentPlan.setPeriod(i);

            repaymentPlans.add(repaymentPlan);
        }
        List<RepaymentPlan> repaymentPlansSplit = repaymentPlanSpliter.splitByRepayAmount(repayAmt, repaymentPlans);
        System.out.println(JSON.toJSONString(repaymentPlansSplit));
    }
}