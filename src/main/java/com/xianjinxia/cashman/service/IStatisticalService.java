package com.xianjinxia.cashman.service;

import com.xianjinxia.cashman.dto.StatisticRepayOrderDto;
import com.xianjinxia.cashman.dto.StatisticsRepayDto;

import java.util.Date;
import java.util.List;

/**
 * Created by liquan on 2018/4/10.
 * 统计数据
 * @Author: liquan
 * @Description:
 * @Date: Created in 17:53 2018/4/10
 * @Modified By:
 */
public interface IStatisticalService {

    /**
     * 根据产品ID查询统计数据信息
     * @param productId --产品ID
     * @param productCategory --产品类型
     * @param merchantNo --商户号
     * @param repaymentPlanTime --计划还款时间
     * @return
     */
    StatisticsRepayDto getStatisticsInfo(Long productId, Integer productCategory, String merchantNo, String repaymentPlanTime);


    /**
     * 查询指定某天的应还订单数据
     * @param startDay--某一天
     * @param endDay --结束某天
     * @return
     */
    public List<StatisticRepayOrderDto> getRepayOrderForDay(String startDay, String endDay);
}
