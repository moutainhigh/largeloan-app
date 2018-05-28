package com.xianjinxia.cashman.service;

import com.xianjinxia.cashman.dto.LoanUsageDto;

import java.util.List;

/**
 * Created by liquan on 2017/12/15.
 */
public interface ILoanUsageService {
    /**
     * 查询全部借款用途列表
     * @return
     */
    List<LoanUsageDto> selectAllUsages();
}
