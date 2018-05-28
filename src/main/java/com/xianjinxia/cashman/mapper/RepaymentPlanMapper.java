package com.xianjinxia.cashman.mapper;

import com.xianjinxia.cashman.domain.RepaymentPlan;
import com.xianjinxia.cashman.dto.AlipayRepamentPlanDto;
import com.xianjinxia.cashman.dto.RepaymentNoticeDto;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface RepaymentPlanMapper {

    int insert(RepaymentPlan record);

    RepaymentPlan selectByPrimaryKey(Long id);

    // bigdata temp
    RepaymentPlan selectLastOverdueRepaymentPlanByUserId(@Param("userId") Long userId, @Param("isOverdue") Boolean isOverdue);

    List<RepaymentPlan> selectRepaymentPlanByLoanOrderId(@Param("loanOrderId") Long loanOrderId, @Param("productId") Long productId);

    List<RepaymentPlan> selectRepaymentPlanByRepaymentOrderId(Long repaymentOrderId);

    int countByRepaymentPlanTimeAndStatus(@Param("repaymentPlanTime") String repaymentPlanTime, @Param("status") Integer status);

    int countByUserIdAndStatus(@Param("userId") Long userId, @Param("status") Integer[] status);

    int countByUserIdAndIsOverdue(@Param("userId") Long userId, @Param("isOverdue") Boolean isOverdue);

    int updateWaitingAmountById(@Param("id") Long id, @Param("amount") Integer amount, @Param("principalAmount") Integer principalAmount, @Param("interestAmount") Integer interestAmount, @Param("overdueAmount") Integer overdueAmount, @Param("version") Integer version);

    int updateIncomeAmountByCMS(@Param("id") Long id, @Param("amount") Integer amount, @Param("version") Integer version, @Param("status") Integer status);

    int updateIncomeAmountById(@Param("id") Long id, @Param("amount") Integer amount, @Param("version") Integer version, @Param("status") Integer status);

    int updateDeductAmountById(@Param("id") Long id, @Param("amount") Integer amount, @Param("version") Integer version, @Param("status") Integer status);

//    int updateIncomeAmountAndStatusById(@Param("id") Long id, @Param("amount") Integer amount, @Param("status") Integer status, @Param("version") Integer version);

    int updateIsOverdueById(@Param("id") Long id, @Param("isOverdue") Boolean isOverdue, @Param("version") Integer version);

    int updateOverdueDayAndFeeById(@Param("id") Long id, @Param("repaymentTotalAmount") Integer repaymentTotalAmount, @Param("isOverdue") Boolean isOverdue, @Param("overdueDayCount") Integer overdueDayCount, @Param("overdueFeeAmount") Integer overdueFeeAmount, @Param("operationFlag") String operationFlag, @Param("version") Integer version);

    int updateRenewal(@Param("id") Long id, @Param("repaymentPlanTime") Date repaymentPlanTime, @Param("version") Integer version);

    //==========================job begin===========================
    List<RepaymentPlan> selectByRepaymentPlanTimeAndOperationFlag(@Param("repaymentPlanTime") Date repaymentPlanTime, @Param("operationFlag") String operationFlag, @Param("status") Integer status);

    int updateWithholdOperationFlagById(@Param("id") Long id, @Param("operationFlag") String operationFlag, @Param("version") Integer version);

    int updateOverdueOperationFlagById(@Param("id") Long id, @Param("operationFlag") String operationFlag, @Param("version") Integer version, @Param("isOverdue") Boolean isOverdue);

    //==========================job end===========================

    //还款短信发送时捞取数据
    List<RepaymentNoticeDto> getRepaymentPlanListForNotice(@Param("loanStatus") String loanStatus,@Param("repaymentStatus") String repaymentStatus,@Param("repaymentPlanTime") Date repaymentPlanTime,@Param("productCategory") Integer productCategory,@Param("merchantNo")String merchantNo,@Param("productId")Long productId);

    List<RepaymentNoticeDto> getPartRepaymentNotice(@Param("loanOrderId")Long loanOrderId,@Param("loanStatus") String[] loanStatus,@Param("repaymentStatus") String[] repaymentStatus);

    int updateStatus(@Param("loanOrderId") Long loanOrderId,@Param("status") int status);
    
    int updateRepaymentPlanTime(@Param("loanOrderId") Long loanOrderId, @Param("repaymentPlanTime") Date repaymentPlanTime, @Param("period") Integer period);

    int updateRepaymentPlanAmountById(AlipayRepamentPlanDto alipayRepaymentPlan);

    int updateRepaymentPlanAmount(AlipayRepamentPlanDto alipayRepaymentPlan);


}