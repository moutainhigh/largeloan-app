package com.xianjinxia.cashman.mapper;

import com.xianjinxia.cashman.domain.LoanContract;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface LoanContractMapper {

    int insert(LoanContract record);


    LoanContract selectByPrimaryKey(Long id);

    List<LoanContract> getByStatus(String status);

    List<LoanContract> getByStatusAndType(@Param("status") String status,@Param("contractType")String contractType);

    int updateStatusAndcontractPathById(@Param("id")Long id,@Param("status") String status,
                                        @Param("contractPath") String contractPath);

    int updateStatusById(@Param("id")Long id,@Param("status") String status);

    LoanContract selectByTrdNoAndType(@Param("trdLoanId")Long trdLoanId,@Param("contractType")String contractType);
}