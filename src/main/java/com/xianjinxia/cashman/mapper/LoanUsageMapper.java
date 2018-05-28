package com.xianjinxia.cashman.mapper;

import com.xianjinxia.cashman.domain.LoanUsage;
import com.xianjinxia.cashman.dto.LoanUsageDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface LoanUsageMapper {

    int insert(LoanUsage record);

    /**
     * 查询全部借款用途列表
     * @return
     */
    List<LoanUsageDto> selectAllUsages();

    /**
     * 根据用途场景编号查询用途名称
     * @param usageCode
     * @return
     */
    LoanUsageDto selectUsageBycode(@Param("usageCode") String usageCode);


}