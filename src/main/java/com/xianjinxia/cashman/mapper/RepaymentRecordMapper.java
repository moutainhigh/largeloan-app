package com.xianjinxia.cashman.mapper;

import com.xianjinxia.cashman.domain.RepaymentRecord;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RepaymentRecordMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(RepaymentRecord record);

    int insertSelective(RepaymentRecord record);

    RepaymentRecord selectByPrimaryKey(Long id);
    
    List<RepaymentRecord> selectByPaymentRequestId(Long paymentRequestId);
    
    List<RepaymentRecord> selectByRepaymentPlanId(Long repaymentPlanId);

    List<RepaymentRecord> selectRepaymentedRecords(Long repaymentPlanId);

    int countByRepaymentPlanId(Long repaymentPlanId);
    
    int updateByPrimaryKeySelective(RepaymentRecord record);

    int updateByPrimaryKey(RepaymentRecord record);
    
    int updatePaymentOrderStatus(@Param("paymentRequestId")Long paymentRequestId, @Param("originStatus")Integer originStatus, @Param("targetStatus")Integer targetStatus);

    int updateRefundAmt(@Param("id") Long id, @Param("refundAmt") Integer refundAmt);

}