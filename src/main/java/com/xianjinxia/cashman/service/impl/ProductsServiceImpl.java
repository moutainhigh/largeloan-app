/****************************************
 * Copyright (c) 2017 XinJinXia.
 * All rights reserved.
 * Created on 2017年9月5日
 *
 * Contributors:
 * tennyqin - initial implementation
 ****************************************/
package com.xianjinxia.cashman.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.xianjinxia.cashman.enums.*;
import com.xianjinxia.cashman.request.NoticeOrdersReq;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import com.xianjinxia.cashman.exceptions.ServiceException;
import com.xianjinxia.cashman.domain.Products;
import com.xianjinxia.cashman.domain.ProductsFeeConfig;
import com.xianjinxia.cashman.dto.IndexProductsDto;
import com.xianjinxia.cashman.dto.ProductsDto;
import com.xianjinxia.cashman.mapper.ProductsFeeConfigMapper;
import com.xianjinxia.cashman.mapper.ProductsMapper;
import com.xianjinxia.cashman.request.ProductReq;
import com.xianjinxia.cashman.response.BaseResponse;
import com.xianjinxia.cashman.response.UnfreezeProductsResponse;
import com.xianjinxia.cashman.service.IProductsService;
import com.xianjinxia.cashman.utils.DateUtil;
import com.xianjinxia.cashman.utils.MoneyUtil;
import org.springframework.util.StringUtils;

/**
 * @author tennyqin
 * @version 1.0
 * @title ProductsServiceImpl.java
 * @created 2017年9月5日
 */
@Service
public class ProductsServiceImpl implements IProductsService {

    private Logger logger= LoggerFactory.getLogger(ProductsServiceImpl.class);

    @Autowired
    private ProductsMapper productsMapper;

    @Autowired
    private ProductsFeeConfigMapper productsFeeConfigMapper;
    
    @Override
    public IndexProductsDto getByCategory(Integer productCategory) {
        Products products = productsMapper.getByCategory(productCategory);
        if (products == null) {
            return null;
        }
        ProductsFeeConfig config = productsFeeConfigMapper.getByProductIdAndFeeTypeAndPeriods(products.getId(), ProductsFeeConfigEnum.INTEREST_FEE.getCode(), 1);
        logger.info("feerate====>"+config.getFeeRate());
        return new IndexProductsDto(products.getId(), products.getName(), products.getSlogan(), products.getProductCategory(), MoneyUtil.changeCentToYuan(products.getMinAmount()), MoneyUtil.changeCentToYuan(products.getMaxAmount()), products.getTerm(), products.getTermType(), products.getMinPeriods(), products.getMaxPeriods(), products.getQuietPeriod(), config.getFeeRate(), products.getIsDepository());
    }

    @Override
    public IndexProductsDto getByCategoryAndMerchantNo(Integer productCategory,String merchantNo) {
        logger.info("=====getByCategoryAndMerchantNo start=====");
        Products products = productsMapper.getByCategoryAndMerchantNo(productCategory,merchantNo);
        logger.info("=====getByCategoryAndMerchantNo end=====");
        if (products == null) {
            return null;
        }
        ProductsFeeConfig config = productsFeeConfigMapper.getByProductIdAndFeeTypeAndPeriods(products.getId(), ProductsFeeConfigEnum.INTEREST_FEE.getCode(), 1);
        logger.info("feerate====>"+config.getFeeRate());
        return new IndexProductsDto(products.getId(), products.getName(), products.getSlogan(), products.getProductCategory(), MoneyUtil.changeCentToYuan(products.getMinAmount()), MoneyUtil.changeCentToYuan(products.getMaxAmount()), products.getTerm(), products.getTermType(), products.getMinPeriods(), products.getMaxPeriods(), products.getQuietPeriod(), config.getFeeRate(),products.getIsDepository());
    }


    @Override
    public Products getById(Long id) {
        return productsMapper.selectById(id);
    }
    @Override
    public boolean isBigAmount(Long productId) {
        Integer productCategory = getById(productId).getProductCategory();
        return ProductCategoryEnum.PRODUCT_CATEGORY_BIG.getCode().intValue() == productCategory.intValue();
    }

    @Override
    public boolean isSmallAmount(Long productId) {
        Integer productCategory = getById(productId).getProductCategory();
        return ProductCategoryEnum.PRODUCT_CATEGORY_SMALL.getCode().intValue() == productCategory.intValue();
    }

    @Override
    public ProductsDto getProductsDto(Integer productCategory, Integer periods) {
        Products products = productsMapper.getByCategory(productCategory);
        if (products == null) {
            return null;
        }

        // 如果期数为空，则设置为产品的最小期数
        if (ObjectUtils.isEmpty(periods)) {
            periods = products.getMinPeriods();

        }
        ProductsFeeConfig productsFeeConfig = productsFeeConfigMapper.getByProductIdAndFeeTypeAndPeriods(products.getId(), ProductsFeeConfigEnum.INTEREST_FEE.getCode(), periods);

        ProductsDto productsDto = new ProductsDto();
        BeanUtils.copyProperties(products, productsDto);
        BeanUtils.copyProperties(productsFeeConfig, productsDto);
        productsDto.setMinAmount(MoneyUtil.changeCentToYuan(products.getMinAmount()).intValue());
        productsDto.setMaxAmount(MoneyUtil.changeCentToYuan(products.getMaxAmount()).intValue());

        return productsDto;
    }

    @Override
    public ProductsDto getProductsDto(Long id, ProductsFeeConfigEnum productsFeeConfigEnum, Integer periods) {
        ProductsDto products = productsMapper.getById(id);
        ProductsFeeConfig config = productsFeeConfigMapper.getByProductIdAndFeeTypeAndPeriods(id, productsFeeConfigEnum.getCode(), periods);
        products.setProductsFeeConfig(config);
        return products;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse saveProduct(ProductReq productReq) {
        BaseResponse baseResponse = new BaseResponse();
        /**参数校验*/
        if (productReq == null || productReq.getProductCategory() == null) {
            baseResponse = new BaseResponse(BaseResponse.ResponseCode.PARAM_CHECK_FAIL);
            return baseResponse;
        }
        if (ProductCategoryEnum.getValue(productReq.getProductCategory()) == null) {
            baseResponse = new BaseResponse(ProductCodeMsgEnum.CHECK_CATEGORY_ERROR);
            return baseResponse;
        }
        /**检查产品表是否已经存在此产品*/
        Products product = new Products();
        product.setDataValid(DataValidEnum.DATA_VALID_YES.getCode());
        product.setName(ProductCategoryEnum.getValue(productReq.getProductCategory()));
        product.setSlogan(productReq.getSlogan());
        product.setProductCategory(productReq.getProductCategory());

        product.setMinAmount(productReq.getMinAmount());
        product.setMaxAmount(productReq.getMaxAmount());
        product.setMinPeriods(productReq.getMinPeriods()==null?1:productReq.getMinPeriods());
        product.setMaxPeriods(productReq.getPeriods());
        product.setTerm(productReq.getTerm());
        product.setTermType(productReq.getTermType());
        product.setQuietPeriod(productReq.getQuietPeriod());

        product.setStartValidDate(productReq.getStartValidDate());
        product.setEndValidDate(productReq.getEndValidDate());
        product.setIsRenewal(productReq.getIsRenewal());
        product.setIsPrepayment(productReq.getIsPrepayment());
        product.setRepayMethod(productReq.getRepayMethod());
        product.setCreatedUser(productReq.getCreatedUser());
        product.setCreatedTime(Calendar.getInstance().getTime());
        product.setIsDepository(productReq.getIsDepository());
        product.setDayPaymentMaxAmount(productReq.getDayPaymentMaxAmount());

        Products productData = productsMapper.getByCategory(productReq.getProductCategory());
        if (productData!=null && productData.getMaxPeriods()!=null && productData.getMaxPeriods().intValue() < productReq.getPeriods().intValue()){
            product.setMaxPeriods(productReq.getPeriods());
        }
        if (productData==null){
            productsMapper.insertSelective(product);
        }else{
            product.setId(productData.getId());
            productsMapper.updateByPrimaryKeySelective(product);
        }
        /**保存产品配置信息*/
        productReq.setProductId(product.getId());
        if (ProductCategoryEnum.PRODUCT_CATEGORY_SMALL.getCode().intValue() == productReq.getProductCategory()){
            return baseResponse;
        }
        boolean ret1 = saveProductsFeeConfig(ProductsFeeConfigEnum.INTEREST_FEE, productReq.getPeriodRate(), productReq);
        boolean ret2 =  saveProductsFeeConfig(ProductsFeeConfigEnum.OVERDUE_FEE, productReq.getDailyOverdueRate(), productReq);
        baseResponse =  (ret1 && ret2 )?  baseResponse : new BaseResponse(ProductCodeMsgEnum.CHECK_PERIODS_REPETITION);
        return baseResponse;
    }

    /**
     * @描述 保存利率配置信息 -内部调用
     **/
    private boolean saveProductsFeeConfig(ProductsFeeConfigEnum productsFeeConfigEnum, BigDecimal feeRate, ProductReq productReq) {
        ProductsFeeConfig productsFeeConfig = productsFeeConfigMapper.getByProductIdAndFeeTypeAndPeriods(productReq.getProductId(),productsFeeConfigEnum.getCode(),productReq.getPeriods());
        boolean result = false;
        if (productsFeeConfig == null){
            ProductsFeeConfig feeConfig = new ProductsFeeConfig();
            feeConfig.setFeeType(productsFeeConfigEnum.getCode());
            feeConfig.setFeeName(productsFeeConfigEnum.getValue());
            feeConfig.setFeeRate(feeRate);
            feeConfig.setProductId(productReq.getProductId());
            feeConfig.setCreatedUser(productReq.getCreatedUser());
            feeConfig.setUpdatedUser(productReq.getCreatedUser());
            feeConfig.setPeriods(productReq.getPeriods());
            feeConfig.setDataValid(DataValidEnum.DATA_VALID_YES.getCode());
            feeConfig.setDescription(productsFeeConfigEnum.getValue());
            feeConfig.setCreatedTime(Calendar.getInstance().getTime());
            feeConfig.setUpdatedTime(Calendar.getInstance().getTime());
            productsFeeConfigMapper.insert(feeConfig);
            result = true;
        }
        return result;
    }

    /**
     * @描述 保存利率配置信息 -内部调用
     **/
    private int updataProductsFeeConfig(ProductsFeeConfigEnum productsFeeConfigEnum, BigDecimal feeRate, ProductReq productReq, DataValidEnum dataValidEnum) {
        ProductsFeeConfig feeConfig = new ProductsFeeConfig();
        feeConfig.setFeeType(productsFeeConfigEnum.getCode());
        feeConfig.setFeeName(productsFeeConfigEnum.getValue());
        feeConfig.setFeeRate(feeRate);
        feeConfig.setProductId(productReq.getProductId());
        feeConfig.setPeriods(productReq.getPeriods());
        feeConfig.setDataValid(dataValidEnum.getCode());
        feeConfig.setUpdatedUser(productReq.getCreatedUser());
        feeConfig.setDescription(productsFeeConfigEnum.getValue());
        feeConfig.setUpdatedTime(Calendar.getInstance().getTime());
        return productsFeeConfigMapper.updateByProductIdAndFeeTypeAndPeriods(feeConfig);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteProduct(ProductReq productReq) {

        int ret1 = updataProductsFeeConfig(ProductsFeeConfigEnum.INTEREST_FEE, productReq.getPeriodRate(), productReq, DataValidEnum.DATA_VALID_NO);
        int ret2 = updataProductsFeeConfig(ProductsFeeConfigEnum.OVERDUE_FEE, productReq.getDailyOverdueRate(), productReq, DataValidEnum.DATA_VALID_NO);

        /**产品配置全部删除后，更新产品表状态*/
        List<String> list = new ArrayList<>();
        list.add(ProductsFeeConfigEnum.INTEREST_FEE.getCode());
        list.add(ProductsFeeConfigEnum.OVERDUE_FEE.getCode());
        Map map = new HashMap(16);
        map.put("productId", productReq.getProductId());
        map.put("list", list);
        List<ProductsFeeConfig> configList =  productsFeeConfigMapper.selectByProductId(map);
        if (CollectionUtils.isEmpty(configList)){
            Products product = new Products();
            product.setId(productReq.getProductId());
            product.setDataValid(DataValidEnum.DATA_VALID_NO.getCode());
            return productsMapper.updateByPrimaryKeySelective(product);
        }
        return (ret1==ret2) ? 1 : 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse updateProduct(ProductReq productReq) {
        Products product = productsMapper.getByCategory(productReq.getProductCategory());
        BaseResponse baseResponse = new BaseResponse();
        product.setId(productReq.getProductId());
        product.setDataValid(DataValidEnum.DATA_VALID_YES.getCode());

        product.setName(ProductCategoryEnum.getValue(productReq.getProductCategory()));
        product.setSlogan(productReq.getSlogan());
        product.setProductCategory(productReq.getProductCategory());
        product.setMinAmount(productReq.getMinAmount());
        product.setMaxAmount(productReq.getMaxAmount());
        product.setMinPeriods(productReq.getMinPeriods()==null?1:productReq.getMinPeriods());
        if (product.getMaxPeriods()!=null && product.getMaxPeriods().intValue() < productReq.getPeriods().intValue()){
            product.setMaxPeriods(productReq.getPeriods());
        }
        product.setTerm(productReq.getTerm());
        product.setTermType(productReq.getTermType());
        product.setQuietPeriod(productReq.getQuietPeriod());
        product.setStartValidDate(productReq.getStartValidDate());
        product.setEndValidDate(productReq.getEndValidDate());
        product.setIsRenewal(productReq.getIsRenewal());

        product.setIsPrepayment(productReq.getIsPrepayment());
        product.setRepayMethod(productReq.getRepayMethod());
        product.setCreatedUser(productReq.getCreatedUser());
        product.setIsDepository(productReq.getIsDepository());
        product.setDayPaymentMaxAmount(productReq.getDayPaymentMaxAmount());
        product.setUpdatedTime(Calendar.getInstance().getTime());

        updataProductsFeeConfig(ProductsFeeConfigEnum.INTEREST_FEE, productReq.getPeriodRate(), productReq, DataValidEnum.DATA_VALID_YES);
        updataProductsFeeConfig(ProductsFeeConfigEnum.OVERDUE_FEE, productReq.getDailyOverdueRate(), productReq, DataValidEnum.DATA_VALID_YES);
        productsMapper.updateByPrimaryKeySelective(product);
        return baseResponse;
    }

    @Override
    public List<NoticeOrdersReq> getFreezeInfoProductByCategoryList(List<Integer> productCategoryList, Boolean isUnfreeNotice) {
        List<UnfreezeProductsResponse> productsList = productsMapper.getFreezeInfoProductByCategoryList(productCategoryList);
        List<NoticeOrdersReq> unfreezeOrdersReq = new ArrayList<>();
        if (CollectionUtils.isEmpty(productsList)) {
            throw new ServiceException("未查到产品信息");
        }
        for(UnfreezeProductsResponse unfreezeProduct:productsList){
            if(null==unfreezeProduct.getQuietPeriod()||0==unfreezeProduct.getQuietPeriod()){
                logger.info("本产品冷却时间为0，产品id为"+unfreezeProduct.getId());
                continue;
            }
            if(isUnfreeNotice) {
                //待解冻订单审核失败时间=系统时间-冷冻时间
                Date unfreeTime = DateUtil.daysBefore(DateUtil.yyyyMMdd2Date(DateUtil.yyyyMMdd()), unfreezeProduct.getQuietPeriod());
                unfreezeOrdersReq.add(new NoticeOrdersReq(unfreeTime, unfreezeProduct.getId(), unfreezeProduct.getMerchantNo(), unfreezeProduct.getProductCategory()));
            }else{
                //非待解冻信息不需要解冻时间字段
                unfreezeOrdersReq.add(new NoticeOrdersReq(unfreezeProduct.getId(), unfreezeProduct.getMerchantNo(), unfreezeProduct.getProductCategory()));
            }

        }
        return unfreezeOrdersReq;
    }

    /**
     * 大额小额类型列表
     * @return
     */
    @Override
    public List<Integer> getAllCategoryType() {
        List<Integer> categoryList =new ArrayList<>();
        categoryList.add(ProductCategoryEnum.PRODUCT_CATEGORY_BIG.getCode());
        categoryList.add(ProductCategoryEnum.PRODUCT_CATEGORY_SMALL.getCode());
        categoryList.add(ProductCategoryEnum.PRODUCT_CATEGORY_SHOPPING.getCode());
        return categoryList;
    }

    @Override
    public List<NoticeOrdersReq> getNoticeProductsRemoveAssigned(List<NoticeOrdersReq> productReqs, MerchantNoEnum merchantNoEnum) {
        if(CollectionUtils.isEmpty(productReqs)){
            return productReqs;
        }else{
            for(NoticeOrdersReq noticeOrdersReq:productReqs){
                if(!StringUtils.isEmpty(noticeOrdersReq.getMerchantNo())&&(merchantNoEnum.getMerchantNo().equals(noticeOrdersReq.getMerchantNo()))){
                    productReqs.remove(noticeOrdersReq);
                    return productReqs;
                }
            }
        }
        return productReqs;
    }
}
