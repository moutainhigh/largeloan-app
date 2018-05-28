package com.xianjinxia.cashman.loan;

import com.xianjinxia.cashman.request.LoanCheckRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.xianjingxia.paymentclient.utils.JsonUtils;
import com.xianjinxia.cashman.CashmanApplication;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = CashmanApplication.class)
public class LoanTest {


    @Test
    public void testLoan() throws Exception {
        LoanCheckRequest loanCheckRequest = new LoanCheckRequest();
        //loanCheckRequest.setOrderAmount("6000");
        loanCheckRequest.setPeriods("1");
        loanCheckRequest.setProductId("0");

        String json = JsonUtils.toJSONString(loanCheckRequest);
        System.out.println(json);
    }

    @Test
    public void testRestTemplate() {

    }

}