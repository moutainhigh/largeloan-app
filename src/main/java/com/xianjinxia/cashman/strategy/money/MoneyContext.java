package com.xianjinxia.cashman.strategy.money;

import com.alibaba.fastjson.JSON;
import com.xianjinxia.cashman.conf.ExtProperties;
import com.xianjinxia.cashman.domain.Products;
import com.xianjinxia.cashman.domain.RepaymentPlan;
import com.xianjinxia.cashman.dto.ProductsDto;
import com.xianjinxia.cashman.dto.ServiceChargeFee;
import com.xianjinxia.cashman.enums.MoneyCalcEnum;
import com.xianjinxia.cashman.enums.ProductCategoryEnum;
import com.xianjinxia.cashman.enums.ProductTermTypeEnum;
import com.xianjinxia.cashman.enums.ProductsFeeConfigEnum;
import com.xianjinxia.cashman.mapper.ProductsMapper;
import com.xianjinxia.cashman.service.IProductsService;
import com.xianjinxia.cashman.utils.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;

@Component
public class MoneyContext {

    private static final Logger logger = LoggerFactory.getLogger(MoneyContext.class);

    @Autowired
    @Qualifier("bigAmountMoneyCalculator")
    private MoneyCalculator bigAmountMoneyCalculator;

    @Autowired
    @Qualifier("smallAmountMoneyCalculator")
    private MoneyCalculator smallAmountMoneyCalculator;

    @Autowired
    @Qualifier("averageMoneyAndInterestCalculator")
    private MoneyCalculator averageMoneyAndInterestCalculator;

    @Autowired
    private ExtProperties extProperties;


/*    @Autowired
    @Qualifier("shoppingAmountMoneyCalculator")
    private MoneyCalculator shoppingAmountMoneyCalculator;*/



    @Autowired
    private IProductsService productsService;

    @Autowired
    private ProductsMapper productsMapper;


    public Map<Integer, MoneyCalculator> moneyCalculatorMap = new HashMap<>();


    @PostConstruct
    public void init() {
        moneyCalculatorMap = new HashMap<>();
        String method=extProperties.getMoneyInterestCalc().getBigAmount();
        //根据配置文件中的配置，决定大额选择哪种本息计算方式
        if(MoneyCalcEnum.ECAI.getCalcMethod().equals(method)){
            moneyCalculatorMap.put(ProductCategoryEnum.PRODUCT_CATEGORY_BIG.getCode(), bigAmountMoneyCalculator);
        }
        else if(MoneyCalcEnum.EMAI.getCalcMethod().equals(method)){
            moneyCalculatorMap.put(ProductCategoryEnum.PRODUCT_CATEGORY_BIG.getCode(), averageMoneyAndInterestCalculator);
        }
        else{
            throw new RuntimeException("大额利息计算方式配置错误");
        }
        moneyCalculatorMap.put(ProductCategoryEnum.PRODUCT_CATEGORY_SMALL.getCode(), smallAmountMoneyCalculator);
        moneyCalculatorMap.put(ProductCategoryEnum.PRODUCT_CATEGORY_SHOPPING.getCode(), bigAmountMoneyCalculator);
        logger.info("moneyCalculatorMap :{}", JSON.toJSONString(moneyCalculatorMap));
    }

    /**
     * 获取每期利息
     *
     * @param productId
     * @param money
     * @param period
     *
     * @return
     */
    public List<Integer> getInterest(Long productId, Integer money, Integer period) {
        ProductsDto products = productsService.getProductsDto(productId, ProductsFeeConfigEnum.INTEREST_FEE, period);
        MoneyCalculator moneyCalculator = getMoneycalculator(products.getProductCategory());
        return moneyCalculator.getInterest(products, money, period);
    }

    /**
     * 获取每期应还本金
     *
     * @param productId
     * @param money
     * @param period
     *
     * @return
     */
    public List<Integer> getPeriodMoney(Long productId, Integer money, Integer period) {
        ProductsDto products = productsService.getProductsDto(productId, ProductsFeeConfigEnum.INTEREST_FEE, period);
        MoneyCalculator moneyCalculator = getMoneycalculator(products.getProductCategory());
        return moneyCalculator.getPeriodMoney(products, money, period);
    }

    private MoneyCalculator getMoneycalculator(Integer key) {
        logger.info("get calculator key:{}", key.intValue());

        MoneyCalculator moneyCalculator=moneyCalculatorMap.get(key);
        if(moneyCalculator==null){
            throw new IllegalArgumentException("key is illegal");
        }
        return moneyCalculator;
    }

    /**
     * 根据产品ID获取获取费用列表
     *
     * @param money
     * @param productId
     *
     * @return serviceChargeFees
     */
    public List<ServiceChargeFee> getServiceChargeFees(Long productId, Integer money) {
        //目前大额小额的费用列表计算方法是一样的，所以直接调用小额的实现类
        return smallAmountMoneyCalculator.getServiceChargeFees(productId, money);

    }

    /**
     * 根据借款的期数和金额，计算还款时间,每期金额，利息
     *
     * @param productId
     * @param money
     * @param periods
     * @param paymentTime
     *
     * @return
     */
    public List<RepaymentPlan> calcPeriodData(Long productId, Integer money, Integer periods, Date paymentTime) {
        //获取每期的利息
        List<Integer> interests = getInterest(productId, money, periods);
        //获取每期的还款本金
        List<Integer> peridMoneys = getPeriodMoney(productId, money, periods);
        List<RepaymentPlan> list = new ArrayList<>();
        Products product = productsMapper.getTermTypeAndTermById(productId);
        if (ProductTermTypeEnum.DAY.getCode().equals(product.getTermType())) {
            for (int i = 0; i < periods; i++) {
                int temp = i + 1;
                RepaymentPlan ro = new RepaymentPlan(DateUtil.addDay(paymentTime, product.getTerm() * temp), temp);
                ro.setRepaymentPrincipalAmount(peridMoneys.get(i));
                ro.setRepaymentInterestAmount(interests.get(i));
                list.add(ro);
            }
            return list;
        }

        if (ProductTermTypeEnum.MONTH.getCode().equals(product.getTermType())) {
            for (int i = 0; i < periods; i++) {
                int currentPeriod = i + 1;
                RepaymentPlan ro = new RepaymentPlan(DateUtil.addMonth(paymentTime, currentPeriod), currentPeriod);
                ro.setRepaymentPrincipalAmount(peridMoneys.get(i));
                ro.setRepaymentInterestAmount(interests.get(i));
                list.add(ro);
            }
            return list;
        }
        throw new RuntimeException("not support termType");
    }
    
	/**
	 * 根据产品信息和还款时间,当前基数,获取当前期数的还款时间
	 * 
	 * @param product
	 * @param paymentTime
	 * @param currentPeriod
	 * @return
	 */
	public Date getCurrPeriodRepaymentPlanTime(Products product, Date paymentTime, Integer currentPeriod) {
		Date currPeriodRepaymentPlanTime = null;
		if (ProductTermTypeEnum.DAY.getCode().equals(product.getTermType())) {
			currPeriodRepaymentPlanTime = DateUtil.addDay(paymentTime, product.getTerm() * currentPeriod);
		} else if (ProductTermTypeEnum.MONTH.getCode().equals(product.getTermType())) {
			currPeriodRepaymentPlanTime = DateUtil.addMonth(paymentTime, currentPeriod);
		}else{
			throw new RuntimeException("not support termType");
		}
		return currPeriodRepaymentPlanTime;
	}
}
