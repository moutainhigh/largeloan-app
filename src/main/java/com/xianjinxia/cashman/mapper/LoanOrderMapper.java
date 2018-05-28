package com.xianjinxia.cashman.mapper;

import com.xianjinxia.cashman.domain.LoanOrder;
import com.xianjinxia.cashman.dto.LoanOrderSpeedDto;
import com.xianjinxia.cashman.dto.SpeedCardRepayDto;
import com.xianjinxia.cashman.response.UnfreezeOrdersResponse;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface LoanOrderMapper {

    int insert(LoanOrder record);

    LoanOrder selectByPrimaryKey(Long id);

    LoanOrder selectByTrdOrderId(@Param("trdLoanOrderId") Long trdLoanOrderId);

    int updateLoanOrderStatus(@Param("id") Long id, @Param("status") String status);

    int updateLoanOrderStatusByTrdLoanOrderId(@Param("trdLoanOrderId") Long trdLoanOrderId, @Param("status") String status);

    List<LoanOrder> selectByProductCategoryAndStatus(@Param("productCategoryList") List<Integer> productCategoryList, @Param("statusList") List<String> statusList);

    Integer updateselective(Map<String, Object> map);

    LoanOrder selectByBizSeqNo(@Param("bizSeqNo")String bizSeqNo);

    int updateStatus(@Param("id") Long id, @Param("newStatus") String newtatus,@Param("oldStatus") String oldStatus,@Param("reviewFailTime") Date reviewFailTime);

    int countOrderByUserIdAndStatus(@Param("userId") Long userId, @Param("status") String[] status);

    /**
     * 获取风控审核失败或人工审核失败状态的订单
     *
     * @param productId 产品ID
     * @return
     */
    List<UnfreezeOrdersResponse> selectUnfreezeOrders(@Param("merchantNo")String merchantNo,@Param("productId") Long productId, @Param("status") String[] status, @Param("reviewFailTime") Date reviewFailTime);

    int updateSpeedCardPayStatus(@Param("id") Long id,@Param("speedCardPayStatus") String speedCardPayStatus);

    SpeedCardRepayDto getSpeedRepayStatus(@Param("trdLoanOrderId") Long trdLoanOrderId, @Param("orderStatus") String[] status);
    //查询大额，小额订单，订单状态为审核成功，提速卡支付状态为支付失败
    List<LoanOrderSpeedDto> selectByCategoryAndSpeedStatus(@Param("productCategoryList") List<Integer> productCategoryList, @Param("status") List<String> status, @Param("speedCardPayStatus") List<String> speedCardPayStatus,@Param("updateTime")  Date updateTime);

    int updateStatusForSpeed(@Param("id") Long id, @Param("newStatus") String newtatus,@Param("oldStatus") String oldStatus);

    LoanOrder selectByUserPhone(@Param("userPhone") String userPhone, @Param("merchantNo") String merchantNo);

    /**
     * 当还款计划全部还清(状态30)后, 需要更新借款订单状态为终态(状态50)
     * @param trdLoanOrderId 待更新的借款订单编号
     * @return 返回受影响的行数
     */
    int updateLoanOrderStatus2Over(@Param("trdLoanOrderId") Long trdLoanOrderId);
}