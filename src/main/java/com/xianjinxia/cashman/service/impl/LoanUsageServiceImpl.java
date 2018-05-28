package com.xianjinxia.cashman.service.impl;

import com.xianjinxia.cashman.dto.LoanUsageDto;
import com.xianjinxia.cashman.mapper.LoanUsageMapper;
import com.xianjinxia.cashman.service.ILoanUsageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by liquan on 2017/12/15.
 */
@Service
public class LoanUsageServiceImpl implements ILoanUsageService{
    @Autowired
    private LoanUsageMapper loanUsageMapper;
    @Override
    public List<LoanUsageDto> selectAllUsages() {
        return loanUsageMapper.selectAllUsages();
    }
}
