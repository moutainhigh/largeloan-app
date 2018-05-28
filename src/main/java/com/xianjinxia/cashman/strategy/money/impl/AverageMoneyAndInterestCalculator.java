package com.xianjinxia.cashman.strategy.money.impl;

import com.xianjinxia.cashman.dto.ProductsDto;
import com.xianjinxia.cashman.dto.ServiceChargeFee;
import com.xianjinxia.cashman.strategy.money.MoneyCalculator;
import com.xianjinxia.cashman.utils.MoneyUtil;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component("averageMoneyAndInterestCalculator")
public class AverageMoneyAndInterestCalculator implements MoneyCalculator {

/*
    public static void main(String[] args) {
        AverageMoneyAndInterestCalculator calculator=new AverageMoneyAndInterestCalculator();
        ProductsDto dto=new ProductsDto();
        dto.setFeeRate(new BigDecimal("0.03"));
        for (Integer integer : calculator.getInterest(dto, 5000, 3)) {
            System.out.println(integer);
        }
        for (Integer integer : calculator.getPeriodMoney(dto, 5000, 3)) {
            System.out.println(integer);
        }
    }*/

    /**
     * 利息=总金额* 利率值
     * @param products
     * @param money
     * @param period
     * @return
     */
    @Override
    public List<Integer> getInterest(ProductsDto products, Integer money, Integer period) {
        List<Integer> list = new ArrayList<>();
        String temep=MoneyUtil.multiply(money.toString(),products.getFeeRate().toString());
        String interest=MoneyUtil.div(temep,"1");
        for(int i=0;i<period;i++){
            list.add(Integer.parseInt(interest));
        }
        return list;
    }

    /**
     * 每期本金=  总本金/期数
     * @param products
     * @param money
     * @param period
     * @return
     */
    @Override
    public List<Integer> getPeriodMoney(ProductsDto products, Integer money, Integer period) {
        List<Integer> list = new ArrayList<>();
        Integer averageMoney=Integer.parseInt(MoneyUtil.div(money,period));
        Integer amount = 0;
        for(int i=0;i<period-1;i++){
            list.add(averageMoney);
            amount += averageMoney;
        }
        //最后一期的金额= 借款总金额-（period-1)期的总金额
        list.add(money - amount);
        return list;
    }

    @Override
    public List<ServiceChargeFee> getServiceChargeFees(Long productId, Integer money) {
        throw new RuntimeException("method not support");
    }
}
