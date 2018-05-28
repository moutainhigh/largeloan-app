package com.xianjinxia.cashman.service;

import com.xianjinxia.cashman.domain.Products;
import com.xianjinxia.cashman.dto.ServiceChargeFee;
import com.xianjinxia.cashman.enums.ProductTermTypeEnum;
import com.xianjinxia.cashman.mapper.FeeDetailMapper;
import com.xianjinxia.cashman.mapper.LoanOrderMapper;
import com.xianjinxia.cashman.mapper.ProductsMapper;
import com.xianjinxia.cashman.mapper.RepaymentPlanMapper;
import com.xianjinxia.cashman.request.PaymentMessage;
import com.xianjinxia.cashman.strategy.money.MoneyContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.mockito.BDDMockito.*;





@RunWith(SpringRunner.class)
@SpringBootTest
public class IPaymentServiceTest {


    @Autowired
    private IPaymentService paymentService;

    @MockBean
    private LoanOrderMapper loanOrderMapper;

    @MockBean
    private ILoanService loanService;

    @MockBean
    private FeeDetailMapper feeDetailMapper;

    @MockBean
    private RepaymentPlanMapper repaymentPlanMapper;

    @MockBean
    private ISmsService smsService;

    @MockBean
    private ProductsMapper productsMapper;

    @MockBean
    private MoneyContext moneyContext;

    @Test
    public void processLoanSuccess() throws Exception {



        
        
        PaymentMessage message=new PaymentMessage();
        message.setTrdLoanOrderId(123L);
        message.setProductId(1L);
        message.setOrderAmount(50000);
        message.setPeriods(4);
        message.setPaymentTime(new Date());

        Products product=new Products();
        product.setId(1L);
        product.setTermType(ProductTermTypeEnum.DAY.getCode());
        product.setTerm(10);

        given(loanOrderMapper.insert(anyObject())).willReturn(1);

        List<ServiceChargeFee> fees=new ArrayList<>();
        ServiceChargeFee feeOne=new ServiceChargeFee();
        feeOne.setFeeType("1");
        feeOne.setFeeMoney(new BigDecimal("123"));
        ServiceChargeFee feeTwo=new ServiceChargeFee();
        feeTwo.setFeeType("2");
        feeTwo.setFeeMoney(new BigDecimal("456"));
        fees.add(feeOne);
        fees.add(feeTwo);
        given(loanService.getServiceChargeFees(anyDouble(),anyLong())).willReturn(fees);
        given(feeDetailMapper.insert(anyObject())).willReturn(1);

        List<Integer> list=new ArrayList<>();
        list.add(3000);
        list.add(3000);
        list.add(3000);
        list.add(3000);
        given(moneyContext.getInterest(anyLong(),anyInt(),anyInt())).willReturn(list);
        given(moneyContext.getPeriodMoney(anyLong(),anyInt(),anyInt())).willReturn(list);
        given(productsMapper.getTermTypeAndTermById(anyLong())).willReturn(product);
        given( repaymentPlanMapper.insert(anyObject())).willReturn(1);
        doNothing().when(smsService).sendSms(anyObject());
        paymentService.processLoanSuccess(message);
    }

}