package com.xianjinxia.cashman.remote;

import com.xianjinxia.cashman.conf.MyRestTemplate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MyRestTemplateTest {

    @Autowired
    private MyRestTemplate myRestTemplate ;


    @Test
    public void testOldCashman() {
        /*JsonResult result =  myRestTemplate.httpGet("118.31.69.111:18080/refactor/repay/checkpwd?userId=99500&payPassword=111111", JsonResult.class);
    	//JsonResult result = restTemplate.getForObject("http://localhost:8080/cashman/refactor/repayment/userinfo?userId=99500", JsonResult.class);
    	System.out.println(JSON.toJSON(result.getData()));*/
    }

}