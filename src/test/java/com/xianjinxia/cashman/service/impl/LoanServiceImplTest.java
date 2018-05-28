package com.xianjinxia.cashman.service.impl;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.anyLong;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.xianjinxia.cashman.enums.LoanCodeMsgEnum;
import com.xianjinxia.cashman.dto.ContractDto;
import com.xianjinxia.cashman.enums.ProductTermTypeEnum;
import com.xianjinxia.cashman.mapper.ProductsFeeConfigMapper;

import com.xianjinxia.cashman.request.LoanCheckRequest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.xianjinxia.cashman.domain.Products;
import com.xianjinxia.cashman.domain.ProductsFeeConfig;
import com.xianjinxia.cashman.dto.LoanConfigModalDto;
import com.xianjinxia.cashman.enums.DataValidEnum;
import com.xianjinxia.cashman.enums.ProductCategoryEnum;
import com.xianjinxia.cashman.response.BaseResponse;
import com.xianjinxia.cashman.response.LoanCheckResponse;
import com.xianjinxia.cashman.service.IContractService;
import com.xianjinxia.cashman.service.ILoanService;
import com.xianjinxia.cashman.service.IProductsService;
import com.xianjinxia.cashman.service.IRepaymentService;

/**
 * 动态计算借款费用明细与还款计划接口
 * 
 * @author liuzhifang
 *
 *         2017年10月31日
 */
public class LoanServiceImplTest {
    @Mock
    private ILoanService loanService;
    @Mock
    private IProductsService productsService;
    @Mock
    private ProductsFeeConfigMapper productsFeeConfigMapper;
    @Mock
    private IRepaymentService repaymentService;
    @Mock
    private IContractService contractService ;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    /**
     * 动态计算借款费用明细
     * 
     * @throws Exception
     */
    @Test
    public void loanDynamicFeeCalculateTest() {

        LoanConfigModalDto loanConfigModalDto = new LoanConfigModalDto();
        loanConfigModalDto.setOrderAmount(new Double(5000));
        loanConfigModalDto.setPeriods(6);
        loanConfigModalDto.setProductId(1l);


        Products products = new Products();
        products.setId(1L);
        products.setName("big-amount");
        products.setSlogan("hello,world");
        products.setDataValid(DataValidEnum.DATA_VALID_YES.getCode());
        products.setProductCategory(ProductCategoryEnum.PRODUCT_CATEGORY_BIG.getCode());
        products.setMinAmount(50000);
        products.setMaxAmount(600000);
        products.setTerm(10);
        products.setTermType(ProductTermTypeEnum.DAY.getCode());
        products.setMinPeriods(2);
        products.setMaxPeriods(6);
        products.setQuietPeriod(30);
        products.setInterestRate(new BigDecimal(0.01));
        products.setOverdueRate(new BigDecimal(0.02));

        List<ProductsFeeConfig> fees = new ArrayList<ProductsFeeConfig>();
        ProductsFeeConfig fee1 = new ProductsFeeConfig();
        ProductsFeeConfig fee2 = new ProductsFeeConfig();
        ProductsFeeConfig fee3 = new ProductsFeeConfig();
        ProductsFeeConfig fee4 = new ProductsFeeConfig();
        fee1.setProductId(1l);
        fee1.setFeeName("管理费");
        fee1.setFeeType("manage_fee");
        fee1.setFeeRate(new BigDecimal(0.1));
        fee2.setProductId(1l);
        fee2.setFeeName("咨询费");
        fee2.setFeeType("consultation_fee");
        fee2.setFeeRate(new BigDecimal(0.025));
        fee3.setProductId(2l);
        fee3.setFeeName("信审查询费");
        fee3.setFeeType("letter_review_fee");
        fee3.setFeeRate(new BigDecimal(0.05));
        fee4.setProductId(2l);
        fee4.setFeeName("账户管理费");
        fee4.setFeeType("account_management_fee");
        fee4.setFeeRate(new BigDecimal(0.0438));
        fees.add(fee1);
        fees.add(fee2);
        fees.add(fee3);
        fees.add(fee4);
        given(productsService.getById(anyLong())).willReturn(products);
//        given(productsFeeConfigMapper.selectByProductId(anyLong())).willReturn(fees);
        loanService.loanDynamicFeeCalculate(loanConfigModalDto);
    }

    /**
     * 预计算还款计划
     * 
     * @throws Exception
     */
    @Test
    public void loanDynamicRepaymentPlanCalculateTest() {
        LoanConfigModalDto loanConfigModalDto = new LoanConfigModalDto();
        loanConfigModalDto.setOrderAmount(new Double(5000));
        loanConfigModalDto.setPeriods(6);
        loanConfigModalDto.setProductId(1l);

        Products products = new Products();
        products.setId(1L);
        products.setName("big-amount");
        products.setSlogan("hello,world");
        products.setDataValid(DataValidEnum.DATA_VALID_YES.getCode());
        products.setProductCategory(ProductCategoryEnum.PRODUCT_CATEGORY_BIG.getCode());
        products.setMinAmount(50000);
        products.setMaxAmount(600000);
        products.setTerm(10);
        products.setTermType(ProductTermTypeEnum.DAY.getCode());
        products.setMinPeriods(2);
        products.setMaxPeriods(6);
        products.setQuietPeriod(30);
        products.setInterestRate(new BigDecimal(0.01));
        products.setOverdueRate(new BigDecimal(0.02));

        given(productsService.getById(anyLong())).willReturn(products);
        loanService.loanDynamicRepaymentPlanCal(loanConfigModalDto);

    }


    @Test
    public void loanTest() {

        BaseResponse<LoanCheckResponse> baseResponse = new BaseResponse<LoanCheckResponse>();
        LoanCheckResponse data = new LoanCheckResponse(LoanCodeMsgEnum.SUCCESS);

        LoanCheckRequest loanCheckRequest = new LoanCheckRequest();
        loanCheckRequest.setOrderAmount(new BigDecimal("500000"));
        loanCheckRequest.setPeriods("3");
        loanCheckRequest.setProductId("1");
        loanCheckRequest.setUserId(428L);

        Products products = new Products();
        products.setId(1L);
        products.setName("big-amount");
        products.setSlogan("hello,world");
        products.setDataValid(DataValidEnum.DATA_VALID_YES.getCode());
        products.setProductCategory(ProductCategoryEnum.PRODUCT_CATEGORY_BIG.getCode());
        products.setMinAmount(50000);
        products.setMaxAmount(600000);
        products.setTerm(10);
        products.setTermType(ProductTermTypeEnum.DAY.getCode());
        products.setMinPeriods(2);
        products.setMaxPeriods(6);
        products.setQuietPeriod(30);
        products.setInterestRate(new BigDecimal(0.01));
        products.setOverdueRate(new BigDecimal(0.02));

        Products product =
                productsService.getById(Long.valueOf(loanCheckRequest.getProductId()));
        Assert.assertEquals(products,product);

        int amount = loanCheckRequest.getOrderAmount().multiply(new BigDecimal("100")).intValue();
        int minAmout = products.getMinAmount();
        int maxAmout = products.getMaxAmount();
        if (amount < minAmout || amount > maxAmout) {
            data = new LoanCheckResponse(LoanCodeMsgEnum.CHECK_AMOUNT_RANGE);
            Assert.assertEquals(LoanCodeMsgEnum.CHECK_AMOUNT_RANGE,data.getCode());
        }
        // 判断金额是不是100的整数倍
        if (amount % 10000 != 0) {
            data = new LoanCheckResponse(LoanCodeMsgEnum.CHECK_AMOUNT_TIMES);
            Assert.assertEquals(LoanCodeMsgEnum.CHECK_AMOUNT_TIMES,data.getCode());
        }
        // Periods 校验
        if (ProductCategoryEnum.PRODUCT_CATEGORY_SMALL.getCode() == products.getProductCategory().intValue()) {
            if (!loanCheckRequest.getPeriods().equalsIgnoreCase("1")) {
                data = new LoanCheckResponse(LoanCodeMsgEnum.CHECK_PERIODS);
                Assert.assertEquals(LoanCodeMsgEnum.CHECK_PERIODS,data.getCode());
            }
        }
        if (ProductCategoryEnum.PRODUCT_CATEGORY_BIG.getCode() == products.getProductCategory().intValue()) {
            if (Integer.parseInt(loanCheckRequest.getPeriods()) < products.getMinPeriods()
                    || Integer.parseInt(loanCheckRequest.getPeriods()) > products.getMaxPeriods()) {
                data = new LoanCheckResponse(LoanCodeMsgEnum.CHECK_PERIODS);
                Assert.assertEquals(LoanCodeMsgEnum.CHECK_PERIODS,data.getCode());
            }
        }
        given(repaymentService.hasNonUltimateOrder(anyLong())).willReturn(Boolean.TRUE);

        data.setPeriods(loanCheckRequest.getPeriods());
        data.setQuietPeriod(products.getQuietPeriod() + "");
        data.setProductCategory(products.getProductCategory() + "");
        data.setFeeAmount(new BigDecimal("10000"));
        data.setOrderAmount(loanCheckRequest.getOrderAmount());
        data.setPaymentAmount(new BigDecimal("490000"));
        data.setRepaymentAmount(new BigDecimal("500000"));

        List<ContractDto> list = contractService.selectByProductId(Long.parseLong(loanCheckRequest.getProductId()));
        data.setList(list);
        baseResponse.setData(data);
    }

}
