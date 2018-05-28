package com.xianjinxia.cashman.mapper;

import com.xianjinxia.cashman.domain.LoanCapitalInfo;


public interface LoanCapitalInfoMapper {

    int insert(LoanCapitalInfo record);

    LoanCapitalInfo selectByPrimaryKey(Long id);

    LoanCapitalInfo getLoanCapitalInfoByOrdId(Long trdLoanOrderId);

}