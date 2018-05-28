package com.xianjinxia.cashman.mapper;

import com.xianjinxia.cashman.domain.Contract;
import com.xianjinxia.cashman.dto.ContractDto;

import java.util.List;


public interface ContractMapper {

    int insert(Contract record);

    Contract selectByPrimaryKey(Long id);


    List<ContractDto> selectByProductId(Long productId);

}