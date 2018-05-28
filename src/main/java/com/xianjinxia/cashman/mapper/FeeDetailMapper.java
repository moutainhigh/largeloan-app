package com.xianjinxia.cashman.mapper;

import com.xianjinxia.cashman.domain.FeeDetail;

public interface FeeDetailMapper {

    int insert(FeeDetail record);

    FeeDetail selectByPrimaryKey(Long id);

}