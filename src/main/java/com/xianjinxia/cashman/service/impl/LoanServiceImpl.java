package com.xianjinxia.cashman.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xianjinxia.cashman.domain.Products;
import com.xianjinxia.cashman.domain.ProductsFeeConfig;
import com.xianjinxia.cashman.domain.RepaymentPlan;
import com.xianjinxia.cashman.dto.ContractDto;
import com.xianjinxia.cashman.dto.LoanConfigModalDto;
import com.xianjinxia.cashman.dto.ProductsDto;
import com.xianjinxia.cashman.dto.ServiceChargeFee;
import com.xianjinxia.cashman.enums.*;
import com.xianjinxia.cashman.mapper.ContractMapper;
import com.xianjinxia.cashman.mapper.LoanOrderMapper;
import com.xianjinxia.cashman.mapper.ProductsFeeConfigMapper;
import com.xianjinxia.cashman.remote.TradeAppRemoteService;
import com.xianjinxia.cashman.request.LoanCalculateRequest;
import com.xianjinxia.cashman.request.LoanCheckRequest;
import com.xianjinxia.cashman.request.NoticeOrdersReq;
import com.xianjinxia.cashman.response.*;
import com.xianjinxia.cashman.service.ILoanService;
import com.xianjinxia.cashman.service.IPaymentService;
import com.xianjinxia.cashman.service.IProductsService;
import com.xianjinxia.cashman.service.IRepaymentService;
import com.xianjinxia.cashman.strategy.money.MoneyContext;
import com.xianjinxia.cashman.utils.DateUtil;
import com.xianjinxia.cashman.utils.MoneyUtil;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

/**
 * @author fanmaowen
 * @version 1.0
 * @title LoanServiceImpl.java
 * @created 2017年9月6日
 */
@Service
public class LoanServiceImpl implements ILoanService {

    private static final Logger logger = LoggerFactory.getLogger(LoanServiceImpl.class);


    @Autowired
    private LoanOrderMapper loanOrderMapper;

    @Autowired
    private IProductsService productsService;

    @Autowired
    private ContractMapper contractMapper;

    @Autowired
    private IRepaymentService repaymentService;

    @Autowired
    private ProductsFeeConfigMapper productsFeeConfigMapper;

    @Autowired
    private MoneyContext moneyContext;

    @Autowired
    private IPaymentService paymentService;

    @Autowired
    private TradeAppRemoteService tradeAppRemoteService;

    @Override
    @Transactional
    public BaseResponse<LoanCheckResponse> loanBizCheck(LoanCheckRequest loanCheckRequest) {
        BaseResponse<LoanCheckResponse> baseResponse = new BaseResponse<>();
        logger.info("loan 参数{}", loanCheckRequest);
        if(hasUnFinallyLoanOrder(loanCheckRequest.getUserId())){
            logger.info("该用户存在一个在途订单");
            baseResponse.setData(new LoanCheckResponse(LoanCodeMsgEnum.CHECK_ULTIMATE_ORDER));
            return baseResponse;
        }
        LoanCheckResponse data = new LoanCheckResponse(LoanCodeMsgEnum.SUCCESS);
        Products products = productsService.getById(Long.valueOf(loanCheckRequest.getProductId()));
        if (null == products) {
            data = new LoanCheckResponse(LoanCodeMsgEnum.CHECK_NO_PRODUCT);
            baseResponse.setData(data);
            return baseResponse;
        }
        data = bizCheck(data, loanCheckRequest, products);
        if (!data.getCode().equals(LoanCodeMsgEnum.SUCCESS.getCode())) {
            baseResponse.setData(data);
            return baseResponse;
        }
        getFee(baseResponse, loanCheckRequest, data, products);
        getContract(baseResponse, loanCheckRequest, data);
        logger.info("方法 结束完毕");
        return baseResponse;
    }

    private LoanCheckResponse bizCheck(LoanCheckResponse data, LoanCheckRequest loanCheckRequest, Products products) {
        int amount = loanCheckRequest.getOrderAmount().multiply(new BigDecimal("100")).intValue();
        int minAmout = products.getMinAmount();
        int maxAmout = products.getMaxAmount();
        //去掉产品的额度检验，可借额度在0-授信额度之间
//        if (amount < minAmout || amount > maxAmout) {
//            data = new LoanCheckResponse(LoanCodeMsgEnum.CHECK_AMOUNT_RANGE);
//            return data;
//        }
        // 判断金额是不是100的整数倍(商城的产品不用判断)
        if (products.getProductCategory().intValue() != ProductCategoryEnum.PRODUCT_CATEGORY_SHOPPING.getCode().intValue()){
            if (amount % 10000 != 0) {
                data = new LoanCheckResponse(LoanCodeMsgEnum.CHECK_AMOUNT_TIMES);
                return data;
            }
        }

        // Periods 校验
        if (ProductCategoryEnum.PRODUCT_CATEGORY_SMALL.getCode() == products.getProductCategory().intValue()) {
            if (!loanCheckRequest.getPeriods().equalsIgnoreCase("1")) {
                data = new LoanCheckResponse(LoanCodeMsgEnum.CHECK_PERIODS);
                return data;
            }
        }else{
            if (Integer.parseInt(loanCheckRequest.getPeriods()) < products.getMinPeriods() || Integer.parseInt(loanCheckRequest.getPeriods()) > products.getMaxPeriods()) {
                data = new LoanCheckResponse(LoanCodeMsgEnum.CHECK_PERIODS);
                return data;
            }
        }
//        if (ProductCategoryEnum.PRODUCT_CATEGORY_BIG.getCode() == products.getProductCategory().intValue()) {
//
//        }
//        //判断过loan_order后不需再判断repayment_plan
//        if (repaymentService.hasNonUltimateOrder(loanCheckRequest.getUserId())) {
//            data = new LoanCheckResponse(LoanCodeMsgEnum.CHECK_ULTIMATE_ORDER);
//            logger.info("该用户存有在途订单");
//            return data;
//        }
        return data;
    }

    private BaseResponse<LoanCheckResponse> getFee(BaseResponse<LoanCheckResponse> baseResponse, LoanCheckRequest loanCheckRequest, LoanCheckResponse data, Products products) {
        data.setPeriods(loanCheckRequest.getPeriods());
        data.setQuietPeriod(products.getQuietPeriod() + "");
        data.setProductCategory(products.getProductCategory() + "");
        data.setIsDepository(null==products.getIsDepository()? DepositoryTypeEnum.Depository.getCode():products.getIsDepository());
        data.setTermUnit(products.getTermType());
        ProductsFeeConfig productsFeeConfig = productsFeeConfigMapper.getByProductIdAndFeeTypeAndPeriods(products.getId(),ProductsFeeConfigEnum.INTEREST_FEE.getCode(),Integer.parseInt(loanCheckRequest.getPeriods()));
        //设置期利率
        data.setTermRate(productsFeeConfig.getFeeRate());
        try {
            /*
             * ServiceChargeFee serviceChargeFee =
             * getFeeDatail(Integer.parseInt(loanCheckRequest.getOrderAmount()), products.getId());
             * BigDecimal feeAmount = serviceChargeFee.getInterestsAll();
             */

            LoanFeeDetails loanFeeDetails = loanDynamicFeeCal(new LoanConfigModalDto(products.getId(), loanCheckRequest.getOrderAmount().doubleValue(), Integer.parseInt(loanCheckRequest.getPeriods())));
            BigDecimal feeAmount = loanFeeDetails.getFeeAmount();
            data.setFeeAmount(feeAmount);
            data.setOrderAmount(loanCheckRequest.getOrderAmount());
            data.setPaymentAmount(loanFeeDetails.getPaymentAmount());
            data.setRepaymentAmount(loanFeeDetails.getRepaymentAmount());
            data.setInterestAmount(loanFeeDetails.getRepaymentAmount().subtract(loanCheckRequest.getOrderAmount()));
            baseResponse.setData(data);
        } catch (Exception ex) {
            logger.error("loanBizCheck is ex", ex);
            baseResponse.systemError();
        }
        return baseResponse;
    }

    // 获取合同信息
    private BaseResponse<LoanCheckResponse> getContract(BaseResponse<LoanCheckResponse> baseResponse, LoanCheckRequest loanCheckRequest, LoanCheckResponse data) {
        List<ContractDto> list = contractMapper.selectByProductId(Long.parseLong(loanCheckRequest.getProductId()));
        data.setList(list);
        baseResponse.setData(data);
        return baseResponse;
    }

    /*
     * loanMoney 单位为元
     */
    @Override
    @Transactional
    public List<ServiceChargeFee> getServiceChargeFees(Double loanMoney, Long productId) {
        List<ServiceChargeFee> serviceChargeFees = new ArrayList<>();
        Map map = new HashMap();
        map.put("productId", productId);
        map.put("list", ServiceChargeFeeNameEnum.getFeeTypeEnum());
        List<ProductsFeeConfig> list = productsFeeConfigMapper.selectByProductId(map);
        for (ProductsFeeConfig productsFeeConfig : list) {
            if (productsFeeConfig.getFeeType().equalsIgnoreCase(ServiceChargeFeeNameEnum.CONSULTATION_FEE.getCode())) {
                // 咨询费
                serviceChargeFees.add(createServiceChargeFee(ServiceChargeFeeNameEnum.CONSULTATION_FEE.getCode(), ServiceChargeFeeNameEnum.CONSULTATION_FEE.getName(), new BigDecimal(loanMoney).multiply(productsFeeConfig.getFeeRate())));
            }
            if (productsFeeConfig.getFeeType().equalsIgnoreCase(ServiceChargeFeeNameEnum.MANAGE_FEE.getCode())) {
                // 管理费
                serviceChargeFees.add(createServiceChargeFee(ServiceChargeFeeNameEnum.MANAGE_FEE.getCode(), ServiceChargeFeeNameEnum.MANAGE_FEE.getName(), new BigDecimal(loanMoney).multiply(productsFeeConfig.getFeeRate())));
            }
            if (productsFeeConfig.getFeeType().equalsIgnoreCase(ServiceChargeFeeNameEnum.LETTER_REVIEW_FEE.getCode())) {
                // 信审查询费
                serviceChargeFees.add(createServiceChargeFee(ServiceChargeFeeNameEnum.LETTER_REVIEW_FEE.getCode(), ServiceChargeFeeNameEnum.LETTER_REVIEW_FEE.getName(), new BigDecimal(loanMoney).multiply(productsFeeConfig.getFeeRate())));
            }
            if (productsFeeConfig.getFeeType().equalsIgnoreCase(ServiceChargeFeeNameEnum.ACCOUNT_MANAGEMENT_FEE.getCode())) {
                // 账户管理费
                serviceChargeFees.add(createServiceChargeFee(ServiceChargeFeeNameEnum.ACCOUNT_MANAGEMENT_FEE.getCode(), ServiceChargeFeeNameEnum.ACCOUNT_MANAGEMENT_FEE.getName(), new BigDecimal(loanMoney).multiply(productsFeeConfig.getFeeRate())));
            }
        }
        return serviceChargeFees;
    }

    public ServiceChargeFee createServiceChargeFee(String feeType, String feeName, BigDecimal feeRate) {
        ServiceChargeFee serviceChargeFee = new ServiceChargeFee();
        serviceChargeFee.setFeeType(feeType);
        serviceChargeFee.setFeeName(feeName);
        serviceChargeFee.setFeeMoney(feeRate);
        return serviceChargeFee;

    }

    @Override
    public LoanFeeDetails loanDynamicFeeCalculate(LoanConfigModalDto loanConfigModalDto) {
        Products products = productsService.getById(loanConfigModalDto.getProductId());
        // 费用说明详细信息
        LoanFeeDetails loanFeeDetails = calculateFee(loanConfigModalDto.getOrderAmount(), loanConfigModalDto.getPeriods(), products);
        return loanFeeDetails;
    }

    /**
     * 计算还款计划
     *
     * @param products    产品
     * @param orderAmount 借款金额
     * @param periods     借款期数
     *
     * @return
     */
    public List<RepaymentPlanAdvance> calRepayPlan(Products products, Integer orderAmount, int periods) {
        if (products.getProductCategory().equals(ProductCategoryEnum.PRODUCT_CATEGORY_SMALL.getCode())) {
            periods = 1;
        }
        Date repaymentTime = new Date();
        repaymentTime = DateUtil.addDay(repaymentTime, 1);// 起息日是从1天后开始

        List<RepaymentPlanAdvance> repaymentPlans = new ArrayList<RepaymentPlanAdvance>();
        RepaymentPlanAdvance repaymentPlan = null;
        BigDecimal eachPeriodInterest = null;
        //        new BigDecimal(orderAmount).multiply(products.getInterestRate());
        // 每期应还
        BigDecimal repaymentEachPeriodAmount = new BigDecimal(orderAmount).divide(new BigDecimal(periods), 2, BigDecimal.ROUND_HALF_UP).add(eachPeriodInterest);
        for (int i = 1; i <= periods; i++) {
            repaymentPlan = new RepaymentPlanAdvance();
            if (products.getTermType().equals(ProductTermTypeEnum.MONTH.getCode())) {
                repaymentTime = DateUtil.addMonth(repaymentTime, products.getTerm());
            }
            if (products.getTermType().equals(ProductTermTypeEnum.DAY.getCode())) {
                repaymentTime = DateUtil.addDay(repaymentTime, products.getTerm());
            }
            if (products.getProductCategory().equals(ProductCategoryEnum.PRODUCT_CATEGORY_BIG.getCode())) {
                repaymentPlan.setInterest(eachPeriodInterest);
                repaymentPlan.setRepaymentEachPeriodAmount(repaymentEachPeriodAmount);
            }
            repaymentPlan.setPeriodsNum(i);
            repaymentPlan.setProductId(products.getId());
            repaymentPlan.setRepaymentTime(repaymentTime);
            repaymentPlans.add(repaymentPlan);
        }
        return repaymentPlans;
    }

    /**
     * 借款利息费用说明等计算
     *
     * @param orderAmount 借款金额 单位元
     * @param periods
     * @param products
     *
     * @return
     */
    @Override
    public LoanFeeDetails calculateFee(Double orderAmount, int periods, Products products) {
        if (products.getProductCategory().equals(ProductCategoryEnum.PRODUCT_CATEGORY_SMALL.getCode())) {
            periods = 1;
        }
        LoanFeeDetails loanFeeDetails = new LoanFeeDetails();
        BigDecimal feeAmount = new BigDecimal(0);
        List<ServiceChargeFee> serviceChargeFees = getServiceChargeFees(orderAmount, products.getId());
        // 管理费
        BigDecimal manage_fee = new BigDecimal(0);
        // 咨询费
        BigDecimal consultation_fee = new BigDecimal(0);
        // 信审查询费
        BigDecimal letter_review_fee = new BigDecimal(0);
        // 账户管理费
        BigDecimal account_management_fee = new BigDecimal(0);
        for (ServiceChargeFee serviceChargeFee : serviceChargeFees) {
            if (serviceChargeFee.getFeeType().equals(ServiceChargeFeeNameEnum.MANAGE_FEE.getCode())) {
                manage_fee = serviceChargeFee.getFeeMoney();
            }
            if (serviceChargeFee.getFeeType().equals(ServiceChargeFeeNameEnum.CONSULTATION_FEE.getCode())) {
                consultation_fee = serviceChargeFee.getFeeMoney();
            }
            if (serviceChargeFee.getFeeType().equals(ServiceChargeFeeNameEnum.LETTER_REVIEW_FEE.getCode())) {
                letter_review_fee = serviceChargeFee.getFeeMoney();
            }
            if (serviceChargeFee.getFeeType().equals(ServiceChargeFeeNameEnum.ACCOUNT_MANAGEMENT_FEE.getCode())) {
                account_management_fee = serviceChargeFee.getFeeMoney();
            }
        }
        loanFeeDetails.setPeriods(periods);
        loanFeeDetails.setProductId(products.getId());
        // 借款金额
        loanFeeDetails.setOrderAmount(orderAmount);
        // 实际到款
        BigDecimal paymentAmount = new BigDecimal(0);
        // 每期利息 借款金额*1%(大额)
        BigDecimal eachPeriodInterest = null;
        //        new BigDecimal(orderAmount).multiply(products.getInterestRate());
        // 借款利息（小额）
        BigDecimal loanInterest = null;
        //        new BigDecimal(orderAmount).multiply(products.getInterestRate());
        // 总计应还
        BigDecimal repaymentAmount = new BigDecimal(0);
        if (products.getProductCategory().equals(ProductCategoryEnum.PRODUCT_CATEGORY_BIG.getCode())) {
            // 服务费
            feeAmount = manage_fee.add(consultation_fee);
            // 总计应还=本金+利息，即借款金额+每期利息*期数(大额)
            repaymentAmount = new BigDecimal(orderAmount).add(eachPeriodInterest.multiply(new BigDecimal(periods)));
            // 实际到款=借款金额-服务费（大额）
            paymentAmount = new BigDecimal(orderAmount).subtract(feeAmount);
        } else if (products.getProductCategory().equals(ProductCategoryEnum.PRODUCT_CATEGORY_SMALL.getCode())) {
            // 服务费
            feeAmount = letter_review_fee.add(account_management_fee);
            // 总计应还=本金(小额)
            repaymentAmount = new BigDecimal(orderAmount);
            // 实际到款=借款金额-服务费（信审查询费+账户管理费）-借款利息（小额）
            paymentAmount = new BigDecimal(orderAmount).subtract(feeAmount).subtract(loanInterest);
        }
        loanFeeDetails.setFeeAmount(feeAmount);
        loanFeeDetails.setPaymentAmount(paymentAmount);
        // 总计应还
        loanFeeDetails.setRepaymentAmount(repaymentAmount);
        // 每期应还
        BigDecimal repaymentEachPeriodAmount = repaymentAmount.divide(new BigDecimal(periods), 2, BigDecimal.ROUND_HALF_UP);
        loanFeeDetails.setRepaymentEachPeriodAmount(repaymentEachPeriodAmount);
        loanFeeDetails.setServiceChargeFeeList(serviceChargeFees);
        return loanFeeDetails;
    }

//    @Override
//    public List<RepaymentPlanAdvance> loanDynamicRepaymentPlanCalculate(LoanConfigModalDto loanConfigModalDto) {
//        Products products = productsService.getById(loanConfigModalDto.getProductId());
//
//        // 费用说明详细信息
//        List<RepaymentPlanAdvance> repaymentPlans = calRepayPlan(products, loanConfigModalDto.getOrderAmount(), loanConfigModalDto.getPeriods());
//        return repaymentPlans;
//
//    }

    /**
     * 获取还款列表--新增
     *
     * @param loanConfigModalDto
     *
     * @return
     */
    @Override
    public List<RepaymentPlanAdvance> loanDynamicRepaymentPlanCal(LoanConfigModalDto loanConfigModalDto) {
        //获取还款计划列表
        List<RepaymentPlan> repaymentPlans = moneyContext.calcPeriodData(loanConfigModalDto.getProductId(), new Double(loanConfigModalDto.getOrderAmount()*100).intValue(), loanConfigModalDto.getPeriods(), new Date());

        List<RepaymentPlanAdvance> repaymentPlanAdvances = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(repaymentPlans)) {
            BigDecimal eachInteret;
            BigDecimal eachRepayment;
            //借款计划中算出的金额改为以元为单位输出
            for (RepaymentPlan repaymentPlan : repaymentPlans) {
                eachInteret = MoneyUtil.changeCentToYuan(repaymentPlan.getRepaymentInterestAmount());
                eachRepayment = MoneyUtil.changeCentToYuan(repaymentPlan.getRepaymentPrincipalAmount());
                RepaymentPlanAdvance repaymentPlanAdvance = new RepaymentPlanAdvance();
                repaymentPlanAdvance.setPeriodsNum(repaymentPlan.getPeriod());
                repaymentPlanAdvance.setRepaymentTime(repaymentPlan.getRepaymentPlanTime());
                repaymentPlanAdvance.setProductId(repaymentPlan.getProductId());
                if (productsService.isBigAmount(loanConfigModalDto.getProductId())) {
                    repaymentPlanAdvance.setRepaymentEachPeriodAmount(eachRepayment.add(eachInteret));
                } else {
                    repaymentPlanAdvance.setRepaymentEachPeriodAmount(eachRepayment);
                }
                repaymentPlanAdvance.setInterest(eachInteret);
                repaymentPlanAdvances.add(repaymentPlanAdvance);
            }
        }
        return repaymentPlanAdvances;
    }


    /**
     * 计算单期费用利息说明
     *
     * @param loanConfigModalDto 借款金额 单位元
     *
     * @return
     */
    @Override
    public LoanFeeDetails loanDynamicFeeCal(LoanConfigModalDto loanConfigModalDto) {
        LoanFeeDetails loanFeeDetails = new LoanFeeDetails();

        //计算时全部用分 订单金额（单位为分）
        Integer orderAmountCent = new Double(loanConfigModalDto.getOrderAmount()*100).intValue();
        //利息列表
        List<Integer> interets = moneyContext.getInterest(loanConfigModalDto.getProductId(), orderAmountCent, loanConfigModalDto.getPeriods());
        //利息总金额
        Integer interestSum = 0;
        for (Integer val : interets) {
            interestSum = interestSum + val;
        }
        loanFeeDetails.setInterestAmount(MoneyUtil.changeCentToYuan(interestSum));
        //是否大额
        Boolean isSmallAmount = productsService.isSmallAmount(loanConfigModalDto.getProductId());
        //费用列表
        List<ServiceChargeFee> serviceChargeFeeList = moneyContext.getServiceChargeFees(loanConfigModalDto.getProductId(), orderAmountCent);
        //获取费用总和
        BigDecimal feeSum = new BigDecimal("0");
        //是否有费用列表（大额不启动费用）
        if (isSmallAmount && CollectionUtils.isNotEmpty(serviceChargeFeeList)) {
            for (ServiceChargeFee serviceChargeFee : serviceChargeFeeList) {
                feeSum = feeSum.add(serviceChargeFee.getFeeMoney());
            }
        }
        //服务费 = 利息+ 费用明细总和(小额)----服务费 = 费用明细总和（大额）
        Integer feeAmount = 0;
        if (isSmallAmount) {
            //小额服务费 = 利息+ 费用明细总和
            feeAmount = feeSum.intValue() + interestSum;
        } else {
            //大额服务费 = 费用明细总和(不含利息)
            feeAmount = feeSum.intValue();
        }
        loanFeeDetails.setFeeAmount(MoneyUtil.changeCentToYuan(feeAmount));
        //实际到账金额 = 订单金额- 服务费
        loanFeeDetails.setPaymentAmount(MoneyUtil.changeCentToYuan(orderAmountCent - feeAmount));
        //总计应还 = 订单金额(小额)--订单金额+利息总数（大额）
        loanFeeDetails.setRepaymentAmount(MoneyUtil.changeCentToYuan(getRepaymentAmount(orderAmountCent, interestSum, !isSmallAmount)));
        //每期应还 = 订单金额（小额）--首期本金+首期利息（大额）
        loanFeeDetails.setRepaymentEachPeriodAmount(MoneyUtil.changeCentToYuan(getEachPeriodRepay(interets, loanConfigModalDto, !isSmallAmount, orderAmountCent)));
        //费用明细--转换结果为元(大额目前不需要这个)
        if (isSmallAmount && CollectionUtils.isNotEmpty(serviceChargeFeeList)) {
            for (ServiceChargeFee serviceChargeFee : serviceChargeFeeList) {
                serviceChargeFee.setFeeMoney(MoneyUtil.changeBigCentToYuan(serviceChargeFee.getFeeMoney()));
            }
        }

        loanFeeDetails.setServiceChargeFeeList(isSmallAmount ? serviceChargeFeeList :(new ArrayList<ServiceChargeFee>()));
        // 借款金额 单位元
        loanFeeDetails.setOrderAmount(loanConfigModalDto.getOrderAmount());
        //产品ID
        loanFeeDetails.setProductId(loanConfigModalDto.getProductId());
        //期数
        loanFeeDetails.setPeriods(loanConfigModalDto.getPeriods());

        //设置月利息
        ProductsDto productsDto = productsService.getProductsDto(loanConfigModalDto.getProductId(), ProductsFeeConfigEnum.INTEREST_FEE, loanConfigModalDto.getPeriods());
        loanFeeDetails.setFeeRate(productsDto.getFeeRate());
        return loanFeeDetails;
    }

    @Override
    public List<LoanFeeDetails> loanDynamicFeeBatchCal(LoanCalculateRequest loanCalculateRequest) {
        List<LoanFeeDetails> loanFeeDetailsList = new ArrayList<>();
        List<Integer> orderAmountList = loanCalculateRequest.getOrderAmountList();
        for (Iterator<Integer> iterator = orderAmountList.iterator(); iterator.hasNext(); ) {
            Integer orderAmount =  iterator.next();
//            Long productId, Integer orderAmount, int periods
            //todo 批量试算 单位为元，应该设置成Double类型
            LoanConfigModalDto loanConfigModalDto = new LoanConfigModalDto(loanCalculateRequest.getProductId(), orderAmount.doubleValue(), loanCalculateRequest.getPeriods());
            LoanFeeDetails loanFeeDetails = this.loanDynamicFeeCal(loanConfigModalDto);
            loanFeeDetailsList.add(loanFeeDetails);
        }
        return loanFeeDetailsList;
    }


    /**
     * 应还金额
     *
     * @return
     */
    private Integer getRepaymentAmount(Integer orderAmount, Integer interestSum, Boolean isBigAmount) {
        if (isBigAmount) {
            //大额服务费 = 费用明细总和(不含利息)
            return orderAmount + interestSum;
        } else {
            //小额服务费 = 利息+ 费用明细总和
            return orderAmount;
        }
    }

    /**
     * 获取每期应还金额
     *
     * @param interets
     * @param loanConfigModalDto
     * @param isBigAmount
     * @param orderAmountCent
     *
     * @return
     */
    private Integer getEachPeriodRepay(List<Integer> interets, LoanConfigModalDto loanConfigModalDto, Boolean isBigAmount, Integer orderAmountCent) {
        if (isBigAmount) {
            //每期应还列表
            List<Integer> periodMoneys = moneyContext.getPeriodMoney(loanConfigModalDto.getProductId(), orderAmountCent, loanConfigModalDto.getPeriods());
            return interets.get(0) + periodMoneys.get(0);
        } else {
            return orderAmountCent;
        }
    }

    //是否有在途订单
    @Override
    public Boolean hasUnFinallyLoanOrder(Long userId){
        Set<String> finalStatusSet = TrdLoanOrderStatusEnum.getByFinalStatus(false);
        String[] finalStatusArray = finalStatusSet.toArray(new String[finalStatusSet.size()]);//toArray();//toArray(new String[finalStatus.size()]);//new String[]{LoanOrderStatusEnum.APPROVALING.getCode(), LoanOrderStatusEnum.LOANING.getCode(), LoanOrderStatusEnum.MANUALREVIEWING.getCode(), LoanOrderStatusEnum.MANUALREVIEW_SUCCESS.getCode()};
        int count = loanOrderMapper.countOrderByUserIdAndStatus(userId, finalStatusArray);
        return count>0;
    }

    @Override
    public PageInfo<UnfreezeOrdersResponse> getUnfreezeOrderList(NoticeOrdersReq unfreezeOrdersReq) {
        logger.info("params:{}", unfreezeOrdersReq.toString());
        //待解冻订单状态--人工审核失败 -风控审核失败
        String[] status  = new String[]{ TrdLoanOrderStatusEnum.REFUSED.getCode() , TrdLoanOrderStatusEnum.MANUAL_REFUSED.getCode() };
        PageHelper.startPage(unfreezeOrdersReq.getPageNum(), unfreezeOrdersReq.getPageSize());
        List<UnfreezeOrdersResponse> unfreezeOrdersResponses = loanOrderMapper.selectUnfreezeOrders(unfreezeOrdersReq.getMerchantNo(),unfreezeOrdersReq.getId(),status,unfreezeOrdersReq.getUseTime());
        Page<UnfreezeOrdersResponse> page = (Page<UnfreezeOrdersResponse>) unfreezeOrdersResponses;
        logger.info("result:{}", page);
        return page.toPageInfo();
    }
}
