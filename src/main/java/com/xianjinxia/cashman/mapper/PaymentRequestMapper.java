package com.xianjinxia.cashman.mapper;

import com.xianjinxia.cashman.domain.PaymentRequest;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface PaymentRequestMapper {
    int insert(PaymentRequest record);

    PaymentRequest selectByPrimaryKey(Long id);

    PaymentRequest selectByUserIdAndStatus(@Param("userId")Long userId, @Param("status")Integer status);

    int countByUserIdAndStatus(@Param("userId")Long userId, @Param("status")Integer status);

    int updateByPrimaryKeySelective(PaymentRequest record);

    int updatePaymentRequestStatus(@Param("id") Long id, @Param("originStatus") Integer originStatus, @Param("targetStatus") Integer targetStatus);

    int updatePaymentRequestStatusAndExipredTime(@Param("id") Long id, @Param("originStatus") Integer originStatus, @Param("targetStatus") Integer targetStatus, @Param("expiredTime") Date expiredTime);


    List<PaymentRequest> selectByExpiredTime();

    List<PaymentRequest> selectByStatusAndExpiredTime(@Param("status") Integer status, @Param("expiredTime") Date expiredTime);

    List<PaymentRequest> selectExpiredPaymentRequest(@Param("status") Integer status, @Param("expiredTime") Date expiredTime, @Param("paymentType") String paymentType);

    PaymentRequest getPaymentRequestByRespOrderId(@Param("thirdOrderNo") String thirdOrderNo);
}