package com.xianjinxia.cashman.mapper;

import com.xianjinxia.cashman.domain.ProductsFeeConfig;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ProductsFeeConfigMapper {

    int insert(ProductsFeeConfig record);
    /**
     * 根据产品ID查询费用配置列表--费用数据非利息
     * @param map -- Long productId,List feeTypeList
     * @return
     */
    List<ProductsFeeConfig> selectByProductId(Map map);

    ProductsFeeConfig selectMinPeriod(@Param("productId") Long productId);

    ProductsFeeConfig getByProductIdAndFeeTypeAndPeriods(@Param("productId") Long productId, @Param("feeType")String feeType,@Param("periods") Integer periods);

    ProductsFeeConfig selectByProductIdAndfeeType(@Param("productId") Long productId,@Param("feeType")String feeType);

    int updateByProductIdAndFeeTypeAndPeriods(ProductsFeeConfig feeConfig);
}