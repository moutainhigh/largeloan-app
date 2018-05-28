package com.xianjinxia.cashman.service;

import com.xianjinxia.cashman.domain.LoanOrder;
import com.xianjinxia.cashman.dto.SpeedCardPayResultDto;
import com.xianjinxia.cashman.request.GroupRiskResultReq;
import com.xianjinxia.cashman.request.SyncLoanOrderReq;
import com.xianjinxia.cashman.request.SyncShoppingLoanOrderDeliverRequest;
import com.xianjinxia.cashman.response.SpeedCardRepayRsp;
import org.springframework.transaction.annotation.Transactional;

/**
 * loan order的状态cashman-app和trade-app要保持同步
 *
 * 这里的update status方法会保持2个服务都被更新到
 *
 */
public interface ILoanOrderService {

    void updateLoanOrderStatusToOverdueByTradeAppId(Long trdLoanOrderId);

    void updateLoanOrderStatusToOverdueById(Long loanOrderId);

    void updateLoanOrderStatusToSettledByTradeAppId(Long trdLoanOrderId);

    void updateLoanOrderStatusByTrdLoanOrderId(Long trdLoanOrderId, String status);

    void updateLoanOrderStatusToSettledById(Long loanOrderId);

    void handleGroupRiskCallbackResult(GroupRiskResultReq groupRiskResultReq);

    void processConfirmSuccess(LoanOrder trdLoanOrder);
    
    void sendMessage(Long  trdLoanOrderId, String status, Integer productCategory);
    
    /**
     * 更新还款计划表预期还款时间
     * @param syncShoppingLoanOrderDeliverRequest
     */
	void updateListRepaymentPlanTime(SyncShoppingLoanOrderDeliverRequest syncShoppingLoanOrderDeliverRequest);

    void updateSpeedCardPayStatus(SpeedCardPayResultDto speedCardPayResultDto);

    SpeedCardRepayRsp getSpeedRepayStatus(Long trdLoanOrderId);

    /**
     * 查询订单逾期费用--分为单位
     * @param trdLoanOrderId
     * @return
     */
    Integer getOverdueAmtByLoanId(Long trdLoanOrderId);

    LoanOrder selectByUserPhone(String userPhone, String merchantNo);

    /**
     * 同步订单的状态
     * @param syncLoanOrderReq
     */
    void syncLoanOrderStatus(SyncLoanOrderReq syncLoanOrderReq);

    /**
     * 当还款计划全部还清(状态30)后, 需要更新借款订单状态为终态(状态50)
     * @param trdLoanOrderId 待更新的借款订单编号
     */
    int updateLoanOrderStatus2Over(Long trdLoanOrderId);
}
