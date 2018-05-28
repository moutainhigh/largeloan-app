package com.xianjinxia.cashman.service.impl;

import com.alibaba.fastjson.JSON;
import com.xianjinxia.cashman.domain.RepaymentPlan;
import com.xianjinxia.cashman.enums.RepaymentMethodEnum;
import com.xianjinxia.cashman.request.PayCenterCallbackReq;
import com.xianjinxia.cashman.request.RepaymentReq;
import com.xianjinxia.cashman.service.IRepaymentService;
import com.xianjinxia.cashman.service.repay.IRepaymentPlanService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RepaymentServiceImplTest {

    @Autowired
    private IRepaymentPlanService repaymentPlanService;


    @Autowired
    private IRepaymentService repaymentService;

    @Test
    public void repayCommitTest() {
        RepaymentReq repaymentReq = new RepaymentReq();
        repaymentReq.setUserId(421l);
        repaymentReq.setAmount(177299);
        repaymentReq.setTrdLoanOrderId(1000l);
        repaymentService.repayCommit(repaymentReq);
    }

    @Test
    public void getRepaymentOrderByLoanOrderIdTest(){
        List<RepaymentPlan> repaymentOrderByLoanOrderId = repaymentService.getRepaymentPlanListByTrdLoanOrderId(1002l);

    }

    @Test
    public void repayCallbackTest() {
        PayCenterCallbackReq payCenterCallbackReq = JSON.parseObject("{\"bizType\": \"B6\",\"code\": \"success\",\"msg\": \"代扣成功\",\"orderDetailId\": \"93\",\"orderNo\": \"A5B62018011815162445531405M3EH\",\"orderTime\": \"2018-01-18\",\"payAmount\": 176765,\"payName\": \"测试-先锋\",\"payType\": \"ucfpayPaymentChanel\",\"requestSource\": \"A5\"}", PayCenterCallbackReq.class);
        repaymentService.repayCallback(payCenterCallbackReq);
    }
}

        