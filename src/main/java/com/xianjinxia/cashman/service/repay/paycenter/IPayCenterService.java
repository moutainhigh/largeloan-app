package com.xianjinxia.cashman.service.repay.paycenter;

import com.xianjingxia.paymentclient.paycenter.params.UserInfo;
import com.xianjinxia.cashman.domain.LoanOrder;
import com.xianjinxia.cashman.domain.RepaymentPlan;
import com.xianjinxia.cashman.dto.PaymentInstallmentDto;
import com.xianjinxia.cashman.dto.PaymentParamDto;
import com.xianjinxia.cashman.enums.PaymentCenterBizTypeEnum;

import java.util.Date;
import java.util.List;

/**
 * 调用支付中心有2种方式：
 *
 * 1.MQ的方式, 在定时代扣的时候由服务端以MQ的形式发送支付请求
 * 2.收银台的方式, 在用户主动发起支付的时候, 创建支付请求的参数并进行加签返回给Client端
 *
 */
public interface IPayCenterService {

//	void payWithMQ(String userId, Long paymentRequestId, Integer amount, PaymentCenterBizTypeEnum bizType, String extData, boolean isHistoryOverdue, boolean isRepayAll, boolean isLoanRepayOneDay, boolean isWithhold, List<RepaymentPlan> repaymentPlans,LoanOrder loanOrder, UserInfo userInfo);

	void payWithMQ(String userId, Long paymentRequestId, Integer amount, PaymentCenterBizTypeEnum bizType, String extData, boolean isHistoryOverdue, boolean isRepayAll, boolean isLoanRepayOneDay, boolean isWithhold, List<PaymentInstallmentDto> paymentInstallmentDtos, LoanOrder loanOrder, UserInfo userInfo);

//	PaymentParamDto payWithH5(String md5salt, Integer userId, Long paymentRequestId, Integer paymentAmount, Integer couponId, Integer couponAmount, String exextData, PaymentCenterBizTypeEnum bizType, String requestSource, Date expiredDate,  LoanOrder loanOrder, List<RepaymentPlan> repaymentPlanList, boolean isHistoryOverdue, boolean isRepayAll, boolean isLoanRepayOneDay);

}