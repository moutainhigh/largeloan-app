package com.xianjinxia.cashman.service.repay;

import com.xianjinxia.cashman.domain.LoanOrder;
import com.xianjinxia.cashman.dto.PaymentParamDto;
import com.xianjinxia.cashman.enums.PaymentCenterBizTypeEnum;
import com.xianjinxia.cashman.service.repay.paycenter.IPayCenterService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest
public class IPayCenterServiceTest {


    @Autowired
    private IPayCenterService payCenterService;

//
//    POST http://120.55.44.90:1002/api/custody/payment
//
//    POST data:
//
//    {
//        "cardRecordId":5680412,
//            "sign": "72e5f519ec4a86502727fe76c34404f6",
//            "amount":"",
//            "bizId": "",
//            "bizType":"B6",
//            "couponId":"" ,
//            "deviceId": "wweewdttttttt",
//            "exextData":,
//        "payPwd": "111111",
//            "expireDate": 1516185197198,
//            "mobile": "13916320566",
//            "payPwd": "111111",
//            "installments":[{"period":1,"installmentAmount":176765,"installmentPrincipal":161765,"installInterest":15000,"repayType":1}],
//        "requestSource":"A5",
//            "routeStrategy": "custody_initiative",
//            "txSerialNo": "",
//            "userId": "8774693",
//            "withholdingAmount": ""
//
//    }
//
//[no cookies]
//
//    Request Headers:
//    Connection: keep-alive
//    Content-Type: application/json
//    sessionid: 9D9E99B279389700BC8F8BF195C6FBFB
//    Content-Length: 826
//    Host: 120.55.44.90:1002
//    User-Agent: Apache-HttpClient/4.5.3 (Java/1.8.0_151)

    @Test
    public void createPaymentParamTest() {
//        PaymentParamDto paymentParamDto = payCenterService.payWithH5(
//                "KG2V960UWLBU37KO",
//                8774693,
//                73l,
//                176765,
//                null,
//                null,
//                "{\"amount\":176765,\"createdAt\":1516183397198,\"expiredAt\":1516185197198,\"id\":73,\"paymentType\":\"1\",\"remark\":\"还款\",\"respMsg\":\"-1\",\"respOrderId\":\"-1\",\"respTime\":1516183397208,\"status\":0,\"updatedAt\":1516183397198,\"userId\":8774693}",
//                PaymentCenterBizTypeEnum.BIG_AMT_USER_REPAY,
//                "A5",
//                new Date(1516185197198l),
//                new LoanOrder("lo20180116161432852lh","jsxjx"),
//                null,
//                false,
//                false,
//                false
//        );
//        System.out.println(paymentParamDto.getSign());
    }
}

