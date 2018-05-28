
package com.xianjinxia.cashman.service;

import com.xianjinxia.cashman.domain.Products;
import com.xianjinxia.cashman.dto.IndexProductsDto;
import com.xianjinxia.cashman.dto.ProductsDto;
import com.xianjinxia.cashman.enums.MerchantNoEnum;
import com.xianjinxia.cashman.enums.ProductsFeeConfigEnum;
import com.xianjinxia.cashman.request.ProductReq;
import com.xianjinxia.cashman.request.NoticeOrdersReq;
import com.xianjinxia.cashman.response.BaseResponse;

import java.util.List;


/**
 * @title IProductsService.java
 *
 * @author tennyqin
 * @version 1.0
 * @created 2017年9月5日
 */
public interface IProductsService {


	boolean isBigAmount(Long productId);
	
	IndexProductsDto getByCategory(Integer category);

	public IndexProductsDto getByCategoryAndMerchantNo(Integer productCategory,String merchantNo);

	boolean isSmallAmount(Long productId);

	Products getById(Long productId);

	ProductsDto getProductsDto(Integer productCategory, Integer periods);

	ProductsDto getProductsDto(Long id, ProductsFeeConfigEnum productsFeeConfigEnum, Integer periods);

	BaseResponse saveProduct(ProductReq productReq);

	int deleteProduct(ProductReq productReq);

	BaseResponse updateProduct(ProductReq productReq);

	List<NoticeOrdersReq> getFreezeInfoProductByCategoryList(List<Integer> productCategoryList, Boolean isUnfreeNotice);

	List<Integer> getAllCategoryType();

	//在产品列表中去除某个商户号的产品
	List<NoticeOrdersReq> getNoticeProductsRemoveAssigned(List<NoticeOrdersReq> productReqs, MerchantNoEnum merchantNoEnum);
}
