package com.xianjinxia.cashman.service.repay;

import com.github.pagehelper.PageInfo;
import com.xianjinxia.cashman.domain.RepaymentPlan;
import com.xianjinxia.cashman.domain.RepaymentRecord;
import com.xianjinxia.cashman.dto.AlipayRepamentPlanDto;
import com.xianjinxia.cashman.dto.RepaymentNoticeDto;
import com.xianjinxia.cashman.dto.RiskDto;
import com.xianjinxia.cashman.request.NoticeOrdersReq;

import java.util.Date;
import java.util.List;

public interface IRepaymentPlanService {

    void freezeTotalAmountToWaitingAmount(List<RepaymentRecord> repaymentRecords);

    void offlineIncomeByCms(Long repaymentPlanId, Integer amount, Integer version, boolean isRepaymented);

    void increaseWaitingAmount(Long repaymentOrderId, Integer amount, Integer principalAmount, Integer interestAmount, Integer overdueAmount, Integer version);
    void substractWaitingAmount(Long repaymentOrderId, Integer amount, Integer principalAmount, Integer interestAmount, Integer overdueAmount, Integer version);

    void increaseIncomingAmount(Long repaymentOrderId, Integer amount, Integer version);
    void increaseIncomingAmount(Long repaymentOrderId, Integer amount, Integer version, boolean isRepaymented);

    /**
     * 催收减免修改还款计划
     * modify by ganminghui @2018-05-16
     * <p>
     *     1. total_amount 增量减待减免金额
     *     2. income_amount 增量加待减免金额
     *     3. overdue_fee_amount 增量减待减免金额
     *     4. status 更新为已还清状态
     * </p>
     * @param repaymentPlanId 待修改的还款计划编号
     * @param amount  待减免的金额
     * @param version 乐观锁编号
     * */
    void deductTotalAmount(Long repaymentPlanId, Integer amount, Integer version);

    boolean hasRepaymentProcessing(Long repaymentOrderId);
    boolean hasRepaymentProcessing(RepaymentPlan repaymentPlan);
    void updateIsOverdueToTrue(Long repaymentOrderId, Integer version);
    void updateRenewalCountAndRepaymentPlanTime(Long repaymentOrderId, Date repaymentTime,Integer version);

    //===========query method=============

    int countByUserIdAndStatus(Long userId, Boolean isFinish);

    int countByUserIdAndIsOverdue(Long userId, Boolean isCollection);

    RepaymentPlan getRepaymentPlanByIdWithoutCheck(Long repaymentOrderId);

    RepaymentPlan getRepaymentPlanById(Long repaymentPlanId);

    RepaymentPlan getRepaymentPlanById(Long repaymentPlanId, boolean isCheckOverdue);

    List<RepaymentPlan> getRepaymentOrderListByLoanOrderId(Long loanOrderId);

    List<RepaymentPlan> getRepaymentOrderListByLoanOrderId(Long loanOrderId, boolean isCheckOverdue);

    //通过repaymentOrderId查询其同一借款单下的所有还款计划
    List<RepaymentPlan> getAllRepaymentPlansBySingleId(Long repaymentPlanId);

    List<RepaymentPlan> getMatchedRepaymentPlansByIds(Long[] repaymentOrderIds);

    void checkAndUpdateOverdue(Long repaymentOrderId);

//    List<RepaymentPlanAdvance> calculateRepaymentPlan(Products products, Integer loanOrderAmount, int periodCount);

    //========== bigdata aggregation (temp interface) ================
    RiskDto getRiskDto(Long userId);
    //根据条件捞取还款计划数据记录
    PageInfo<RepaymentNoticeDto> getReapymentPlansForNotice(NoticeOrdersReq unfreezeOrdersReq);

    Long getUserIdByOrderBizSeqNo(String bizSeqNo);

    void updateRepaymentPlanAmount(AlipayRepamentPlanDto alipayRepamentPlanDto);

    void updateRepaymentPlanAmount4Income(AlipayRepamentPlanDto alipayRepamentPlanDto);
}
