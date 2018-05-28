package com.xianjinxia.cashman.strategy.money.impl;

import com.xianjinxia.cashman.constants.Constant;
import com.xianjinxia.cashman.domain.ProductsFeeConfig;
import com.xianjinxia.cashman.dto.ProductsDto;
import com.xianjinxia.cashman.dto.ServiceChargeFee;
import com.xianjinxia.cashman.enums.ServiceChargeFeeNameEnum;
import com.xianjinxia.cashman.mapper.ProductsFeeConfigMapper;
import com.xianjinxia.cashman.strategy.money.MoneyCalculator;
import com.xianjinxia.cashman.utils.MoneyUtil;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component("smallAmountMoneyCalculator")
public class SmallAmountMoneyCalculator implements MoneyCalculator {


    private Map<String, ServiceChargeFeeNameEnum> serviceChargeFeeNameEnumMap;

    @Autowired
    private ProductsFeeConfigMapper productsFeeConfigMapper;


    @PostConstruct
    public void init() {
        serviceChargeFeeNameEnumMap = new HashMap<>();
        serviceChargeFeeNameEnumMap.put(ServiceChargeFeeNameEnum.ACCOUNT_MANAGEMENT_FEE.getCode(),
                ServiceChargeFeeNameEnum.ACCOUNT_MANAGEMENT_FEE);
        serviceChargeFeeNameEnumMap.put(ServiceChargeFeeNameEnum.LETTER_REVIEW_FEE.getCode(),
                ServiceChargeFeeNameEnum.LETTER_REVIEW_FEE);
        serviceChargeFeeNameEnumMap.put(ServiceChargeFeeNameEnum.MANAGE_FEE.getCode(),
                ServiceChargeFeeNameEnum.MANAGE_FEE);
        serviceChargeFeeNameEnumMap.put(ServiceChargeFeeNameEnum.CONSULTATION_FEE.getCode(),
                ServiceChargeFeeNameEnum.CONSULTATION_FEE);
    }
/*
    public static void main(String[] args) {
        SmallAmountMoneyCalculator calculator=new SmallAmountMoneyCalculator();
        ProductsDto dto=new ProductsDto();
        dto.setTerm(21);
        dto.setFeeRate(new BigDecimal("0.03"));
        for (Integer integer : calculator.getInterest(dto, 5000, 1)) {
            System.out.println(integer);
        }
    }*/

    @Override
    public List<Integer> getInterest(ProductsDto products, Integer money, Integer period) {
        if (period != 1) {
            throw new RuntimeException("smallAmount loan's period must be one,actually  period:" + period);
        }

        List<Integer> list = new ArrayList<>();
        String temp1=MoneyUtil.multiply(MoneyUtil.multiply(money.toString(),products.getFeeRate().toString()),String.valueOf(products.getTerm()));
        String interest=MoneyUtil.div(temp1,"30",Constant.ZERO,BigDecimal.ROUND_FLOOR);
        list.add(Integer.parseInt(interest));
        return list;
    }

    @Override
    public List<Integer> getPeriodMoney(ProductsDto products, Integer money, Integer period) {
        if (period != 1) {
            throw new RuntimeException("smallAmount loan's period must be one,actually  period:" + period);
        }
        List<Integer> list = new ArrayList<>();
        list.add(money);
        return list;
    }

    /**
     * 获取费用明细列表--（由于大额小额计算费用明细的方法一致，使用小额计算方法）
     *
     * @param productId
     * @param money
     * @return
     */
    @Override
    public List<ServiceChargeFee> getServiceChargeFees(Long productId, Integer money) {
        Map map = new HashMap();
        map.put("productId", productId);
        map.put("list", ServiceChargeFeeNameEnum.getFeeTypeEnum());
        List<ProductsFeeConfig> list = productsFeeConfigMapper.selectByProductId(map);
        List<ServiceChargeFee> serviceChargeFees = new ArrayList<>();
        ServiceChargeFeeNameEnum feeTypeEnum;
        if (CollectionUtils.isNotEmpty(list)) {
            for (ProductsFeeConfig productsFeeConfig : list) {
                feeTypeEnum = getEnumByFeeType(productsFeeConfig.getFeeType());
                serviceChargeFees.add(new ServiceChargeFee(feeTypeEnum.getCode(),
                        feeTypeEnum.getName(), new BigDecimal(money).multiply(productsFeeConfig.getFeeRate())));
            }
        }
        return serviceChargeFees;
    }


    /**
     * 获取费用类型对应的枚举
     *
     * @param feeType
     * @return
     */
    private ServiceChargeFeeNameEnum getEnumByFeeType(String feeType) {
        ServiceChargeFeeNameEnum serviceChargeFeeNameEnum = serviceChargeFeeNameEnumMap.get(feeType);
        if (serviceChargeFeeNameEnum == null) {
            throw new IllegalArgumentException("feeType is illegal");
        }
        return serviceChargeFeeNameEnum;
    }


}
