package com.xianjinxia.cashman.mapper;

import com.xianjinxia.cashman.domain.Products;
import com.xianjinxia.cashman.dto.ProductsDto;
import com.xianjinxia.cashman.response.UnfreezeProductsResponse;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;

public interface ProductsMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Products record);

    int insertSelective(Products record);

    Products selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Products record);

    int updateByPrimaryKey(Products record);


    Products selectByTerm(@Param("term") Integer term);

    Products getTermTypeAndTermById(Long id);

    Products selectById(Long id);


    ProductsDto getById(Long id);

    Products getByCategory(Integer category);

    Products getByCategoryAndMerchantNo(@Param("productCategory") Integer productCategory,@Param("merchantNo") String merchantNo);

    List<UnfreezeProductsResponse> getFreezeInfoProductByCategoryList(@Param("productCategoryList") Collection<Integer> productCategoryList);
}