package com.xianjinxia.cashman.mapper;

import com.xianjinxia.cashman.domain.LoanOrder;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by liquan on 2018/1/5.
 */

public interface RiskQueryMapper {

    LoanOrder selectBybizSeqNo(@Param("trdLoanOrderId") String trdLoanOrderId,@Param("userId") Long userId);

    List<LoanOrder> selectUserLoanOrderListByProductId(@Param("userId") Long userId,@Param("productId") Long productId);


}
