package com.xianjinxia.cashman.strategy.money;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;


@RunWith(SpringRunner.class)
@SpringBootTest
public class MoneyContextTest {

    @Autowired
    private MoneyContext moneyContext;


    @Test
    public void getInterestTest(){
        moneyContext.getInterest(2l, 3000, 3);


    }


}