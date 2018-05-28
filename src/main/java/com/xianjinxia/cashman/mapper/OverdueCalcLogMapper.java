package com.xianjinxia.cashman.mapper;

import org.apache.ibatis.annotations.Param;

/**
 * @author ganminghui
 * 罚息计算LOG mapper
 */
public interface OverdueCalcLogMapper {
    /**
     * 插入日罚息计算日志
     * @param repaymentPlanId 还款计划ID
     */
    void insert(@Param("repaymentPlanId") long repaymentPlanId);
}