package com.xianjinxia.cashman.strategy.money.impl;

import com.xianjinxia.cashman.constants.Constant;
import com.xianjinxia.cashman.dto.ProductsDto;
import com.xianjinxia.cashman.dto.ServiceChargeFee;
import com.xianjinxia.cashman.strategy.money.MoneyCalculator;
import com.xianjinxia.cashman.utils.MoneyUtil;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component("bigAmountMoneyCalculator")
public class BigAmountMoneyCalculator implements MoneyCalculator {

    @Override
    public List<Integer> getInterest(ProductsDto products, Integer money, Integer period) {
        //大额采用等额本息计算法，  每期利息=每期本息-每期本金
        List<Integer> list = new ArrayList<>();
        //获取本息
        Integer principalAndInterest = getPrincipalAndInterestByECAI(money, products.getFeeRate(), period);
        List<Integer> principals = getPeriodMoney(products, money, period);
        for (int i = 0; i < period; i++) {
            list.add(principalAndInterest - principals.get(i));
        }
        return list;
    }

    @Override
    public List<Integer> getPeriodMoney(ProductsDto products, Integer money, Integer period) {
        List<Integer> list = new ArrayList<>();
        Integer amount = 0;
        for (int i = 0; i < period - 1; i++) {
            Integer periodMoney = getPrincipalByECAI(money, products.getFeeRate(), period, i + 1);
            amount += periodMoney;
            list.add(periodMoney);
        }
        //最后一期的金额= 借款总金额-（period-1)期的总金额
        list.add(money - amount);
        return list;
    }

    /**
     * 获取费用明细列表--（由于大额小额计算费用明细的方法一致，大额的计算方法不允许调用）
     *
     * @param productId
     * @param money
     *
     * @return
     */
    @Override
    public List<ServiceChargeFee> getServiceChargeFees(Long productId, Integer money) {
        throw new RuntimeException("getServiceChargeFees method can't support");
    }

    /*public static void main(String[] args) {
        BigAmountMoneyCalculator calculator=new BigAmountMoneyCalculator();
        Integer money=500000;
        Integer month=3;
        ProductsDto productsDto=new ProductsDto();
        productsDto.setFeeRate(new BigDecimal("0.03"));
        List<Integer> interest=calculator.getInterest(productsDto,money,month);
        List<Integer> periodMoneys=calculator.getPeriodMoney(productsDto,money,month);
        Integer allInterests=0;
        for(int i=0;i<interest.size();i++){
            allInterests+=interest.get(i);
            System.out.println("本息:"+(interest.get(i)+periodMoneys.get(i))+",本金:"+periodMoneys.get(i)+",利息:"+interest.get(i));
        }

        System.out.println(allInterests);

    }*/

    /**
     * 等额本息计算法，计算本息
     * money*rate*（1＋rate）^month÷((1＋rate)^month－1)   本息
     *
     * @param money     本金，单位 分
     * @param monthRate 月利率
     * @param month     月数
     *
     * @return
     */
    private static Integer getPrincipalAndInterestByECAI(Integer money, BigDecimal monthRate, Integer month) {
        //money*rate
        String moneyMultiRate = MoneyUtil.multiply(String.valueOf(money), monthRate.toString());
        //(1+rate)^n
        Double powForMonthRateAddOne = Math.pow(Double.parseDouble(MoneyUtil.add(Constant.ONE_STR, monthRate.toString())), Double.parseDouble(String.valueOf(month)));
        //money*rate*（1＋rate）^n
        String temp3 = MoneyUtil.multiply(moneyMultiRate, String.valueOf(powForMonthRateAddOne));
        //(1＋rate)^n－1
        String temp4 = MoneyUtil.sub(String.valueOf(powForMonthRateAddOne), Constant.ONE_STR);
        return Integer.valueOf(MoneyUtil.div(temp3, temp4, Constant.ZERO, BigDecimal.ROUND_FLOOR));
    }

    /**
     * 等额本息计算法，计算本金
     * <p>
     * money*rate*(1+rate)^(n-1)÷[(1+rate)^month-1]  //本金
     *
     * @param money     本金，单位分
     * @param monthRate 月利率
     * @param month     月数
     * @param n         第n期
     *
     * @return
     */
    private static Integer getPrincipalByECAI(Integer money, BigDecimal monthRate, Integer month, int n) {
        //1+rate
        String monthRateAddOne = MoneyUtil.add(Constant.ONE_STR, monthRate.toString());
        //money*rate
        String moneyMultiRate = MoneyUtil.multiply(String.valueOf(money), monthRate.toString());
        //(1+rate)^(n-1)
        Double powSubOneForMonthRateAddOne = Math.pow(Double.parseDouble(monthRateAddOne), Double.parseDouble(String.valueOf(n - 1)));
        //(1+rate)^month
        Double powForMonthRateAddOne = Math.pow(Double.parseDouble(monthRateAddOne), Double.parseDouble(String.valueOf(month)));

        String temp3 = MoneyUtil.multiply(moneyMultiRate, String.valueOf(powSubOneForMonthRateAddOne));
        String temp4 = MoneyUtil.sub(String.valueOf(powForMonthRateAddOne), Constant.ONE_STR);
        return Integer.valueOf(MoneyUtil.div(temp3, temp4, Constant.ZERO, BigDecimal.ROUND_FLOOR));
    }
}
