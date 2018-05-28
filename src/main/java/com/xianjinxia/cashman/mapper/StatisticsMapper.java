package com.xianjinxia.cashman.mapper;

import com.xianjinxia.cashman.dto.StatisticRepayOrderDto;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * Created by liquan on 2018/4/11.
 *
 * @Author: liquan
 * @Description:
 * @Date: Created in 17:31 2018/4/11
 * @Modified By:
 */
public interface StatisticsMapper {
    /**
     * 根据订单产品ID，商户号统计对应时间应还人数
     * @param productId 产品ID
     * @param repaymentPlanTime 计划还款时间
     * @param planStatus 还款计划状态
     * @return 对应应还人数
     */
    int countRepaymentNumber(@Param("productId") Long productId, @Param("repaymentPlanTime") String repaymentPlanTime, @Param("planStatus") List<Integer> planStatus);

    /**
     * 根据还款计划应还时间查询订单数据
     * @param startRepaymentPlanTime 计划还款时间
     * @param productCategoryBig 产品类型
     * @param planStatus 还款计划状态
     * @param orderStatus 订单状态
     * @return
     */
    List<StatisticRepayOrderDto> countRepaymentForCollection(@Param("startRepaymentPlanTime") String startRepaymentPlanTime,@Param("endRepaymentPlanTime") String endRepaymentPlanTime,@Param("productCategoryBig") List<Integer> productCategoryBig, @Param("planStatus") List<Integer> planStatus, @Param("orderStatus") List<String> orderStatus);
}
