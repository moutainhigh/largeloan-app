package com.xianjinxia.cashman.service.repay;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.site.lookup.util.StringUtils;
import com.xianjinxia.cashman.constants.Constant;
import com.xianjinxia.cashman.constants.ErrorCodeConstants;
import com.xianjinxia.cashman.domain.LoanOrder;
import com.xianjinxia.cashman.domain.RepaymentPlan;
import com.xianjinxia.cashman.domain.RepaymentRecord;
import com.xianjinxia.cashman.dto.AlipayRepamentPlanDto;
import com.xianjinxia.cashman.dto.RepaymentNoticeDto;
import com.xianjinxia.cashman.dto.RiskDto;
import com.xianjinxia.cashman.dto.TradeAppRiskDto;
import com.xianjinxia.cashman.enums.RepaymentPlanStatusEnum;
import com.xianjinxia.cashman.exceptions.CashmanExceptionBuilder;
import com.xianjinxia.cashman.exceptions.ServiceException;
import com.xianjinxia.cashman.mapper.LoanOrderMapper;
import com.xianjinxia.cashman.mapper.RepaymentPlanMapper;
import com.xianjinxia.cashman.remote.TradeAppRemoteService;
import com.xianjinxia.cashman.request.NoticeOrdersReq;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

@Service
public class RepaymentPlanServiceImpl implements IRepaymentPlanService {

    private static final Logger logger = LoggerFactory.getLogger(RepaymentPlanServiceImpl.class);

    @Autowired
    private RepaymentPlanMapper repaymentPlanMapper;

    @Autowired
    private LoanOrderMapper loanOrderMapper;

    @Autowired
    private RepaymentPlanOverdueOperator repaymentPlanOverdueOperator;

    @Autowired
    private TradeAppRemoteService tradeAppRemoteService;

    @Override
    @Transactional
    public void freezeTotalAmountToWaitingAmount(List<RepaymentRecord> repaymentRecords) {
        for (Iterator<RepaymentRecord> iterator = repaymentRecords.iterator(); iterator.hasNext(); ) {
            RepaymentRecord repaymentRecord = iterator.next();
            RepaymentPlan repaymentPlan = this.getRepaymentPlanById(repaymentRecord.getRepaymentPlanId());
            //修改还款计划的在途金额(增加), 续期的不需要增加
            this.increaseWaitingAmount(repaymentPlan.getId(), repaymentRecord.getAmount(), repaymentRecord.getRepayPrincipalAmt(), repaymentRecord.getRepayInterestAmt(), repaymentRecord.getRepayOverdueAmt(), repaymentPlan.getVersion());
        }
    }

    @Override
    public void offlineIncomeByCms(Long repaymentPlanId, Integer amount, Integer version, boolean isRepaymented) {
        Integer updateStatus = null;
        if (isRepaymented){
            updateStatus = RepaymentPlanStatusEnum.Repaymented.getCode();
        }
        int count = repaymentPlanMapper.updateIncomeAmountByCMS(repaymentPlanId, amount, version, updateStatus);
        if (count != 1){
            throw CashmanExceptionBuilder.build(ErrorCodeConstants.REPAY_PLAN_INCREASE_INCOMING_AMT_ERR);
        }
    }


    @Override
    @Transactional
    public void increaseWaitingAmount(Long repaymentOrderId, Integer amount, Integer principalAmount, Integer interestAmount, Integer overdueAmount, Integer version) {
        logger.info("增加冻结金额(在途金额) {},{},{}",repaymentOrderId, amount, version);
        int count = repaymentPlanMapper.updateWaitingAmountById(repaymentOrderId, amount, principalAmount, interestAmount, overdueAmount, version);
        logger.info("{}",count);
        if (count != 1) {
            throw CashmanExceptionBuilder.build(ErrorCodeConstants.REPAY_PLAN_INCREASE_WAITING_AMT_ERR);
        }
    }

    @Override
    @Transactional
    public void substractWaitingAmount(Long repaymentOrderId, Integer amount, Integer principalAmount, Integer interestAmount, Integer overdueAmount, Integer version) {
        int count = repaymentPlanMapper.updateWaitingAmountById(repaymentOrderId, amount * -1, principalAmount * -1, interestAmount * -1, overdueAmount * -1, version);
        if (count != 1) {
            throw CashmanExceptionBuilder.build(ErrorCodeConstants.REPAY_PLAN_SUBSTRACT_WAITING_AMT_ERR);
        }
    }

    @Override
    @Transactional
    public void increaseIncomingAmount(Long repaymentOrderId, Integer amount, Integer version) {
        this.increaseIncomingAmount(repaymentOrderId, amount, version, false);
    }

    @Override
    @Transactional
    public void increaseIncomingAmount(Long repaymentOrderId, Integer amount, Integer version, boolean isRepaymented) {
        Integer updateStatus = null;
        if (isRepaymented){
            updateStatus = RepaymentPlanStatusEnum.Repaymented.getCode();
        }
        logger.info("增加入账金额 {},{},{}",repaymentOrderId, amount, version);

        int count = repaymentPlanMapper.updateIncomeAmountById(repaymentOrderId, amount, version, updateStatus);
        if (count != 1){
            throw CashmanExceptionBuilder.build(ErrorCodeConstants.REPAY_PLAN_INCREASE_INCOMING_AMT_ERR);
        }
    }

    @Override
    public void deductTotalAmount(Long repaymentPlanId, Integer amount, Integer version) {
        int count = repaymentPlanMapper.updateDeductAmountById(repaymentPlanId, amount, version, RepaymentPlanStatusEnum.Repaymented.getCode());
        if (count != 1) {
            throw CashmanExceptionBuilder.build(ErrorCodeConstants.REPAY_PLAN_DEDUCT_ERR);
        }
    }


    @Override
    public boolean hasRepaymentProcessing(Long repaymentOrderId) {
        RepaymentPlan repaymentPlan = repaymentPlanMapper.selectByPrimaryKey(repaymentOrderId);
        if (ObjectUtils.isEmpty(repaymentPlan)) {
            throw CashmanExceptionBuilder.build(ErrorCodeConstants.REPAY_PLAN_NOT_EXIST);
        }

        if (repaymentPlan.getRepaymentWaitingAmount() > 0) {
            return true;
        }

        return false;
    }

    @Override
    public boolean hasRepaymentProcessing(RepaymentPlan repaymentPlan) {
        if (repaymentPlan.getRepaymentWaitingAmount() > 0) {
            return true;
        }

        return false;
    }

    @Override
    @Transactional
    public void updateIsOverdueToTrue(Long repaymentOrderId, Integer version) {
        int updateOverdueCount = repaymentPlanMapper.updateIsOverdueById(repaymentOrderId, true, version);
        if (updateOverdueCount != 1) {
            throw CashmanExceptionBuilder.build(ErrorCodeConstants.REPAY_PLAN_UPDATE_TO_OVERDUE_ERR);
        }
    }

    @Override
    @Transactional
    public void updateRenewalCountAndRepaymentPlanTime(Long repaymentPlanId, Date repaymentPlanTime, Integer version) {
        int count = repaymentPlanMapper.updateRenewal(repaymentPlanId, repaymentPlanTime, version);
        if (count != 1) {
            throw new ServiceException(Constant.DB_UPDATE_ERROR);
        }
    }

    @Override
    public int countByUserIdAndStatus(Long userId,Boolean isFinish) {
        Set<Integer> finalStatusSet = RepaymentPlanStatusEnum.getByFinalStatus(false);
        Integer[] finalStatusArray =finalStatusSet.toArray(new Integer[finalStatusSet.size()]);
        int count = repaymentPlanMapper.countByUserIdAndStatus(userId, finalStatusArray);
        return count;
    }

    @Override
    public int countByUserIdAndIsOverdue(Long userId, Boolean isOverdue) {
        int count = repaymentPlanMapper.countByUserIdAndIsOverdue(userId, isOverdue);
        return count;
    }

    @Override
    public RepaymentPlan getRepaymentPlanById(Long repaymentPlanId) {
        RepaymentPlan repaymentPlan = repaymentPlanMapper.selectByPrimaryKey(repaymentPlanId);
        repaymentPlanOverdueOperator.checkAndUpdateOverdue(repaymentPlan, false);
        return repaymentPlan;
    }

    @Override
    public RepaymentPlan getRepaymentPlanById(Long repaymentPlanId, boolean isCheckOverdue) {
        if (isCheckOverdue){
            return this.getRepaymentPlanById(repaymentPlanId);
        }else{
            return this.getRepaymentPlanByIdWithoutCheck(repaymentPlanId);
        }
    }

    @Override
    public RepaymentPlan getRepaymentPlanByIdWithoutCheck(Long repaymentOrderId) {
        RepaymentPlan repaymentPlan = repaymentPlanMapper.selectByPrimaryKey(repaymentOrderId);
        return repaymentPlan;
    }


    @Override
    public List<RepaymentPlan> getRepaymentOrderListByLoanOrderId(Long loanOrderId) {
        LoanOrder loanOrder = loanOrderMapper.selectByTrdOrderId(loanOrderId);
        if (ObjectUtils.isEmpty(loanOrder)){
            throw new ServiceException("借款订单未找到");
        }
        List<RepaymentPlan> repaymentPlanList = repaymentPlanMapper.selectRepaymentPlanByLoanOrderId(loanOrderId, loanOrder.getProductId());
        for (Iterator<RepaymentPlan> iterator = repaymentPlanList.iterator(); iterator.hasNext(); ) {
            RepaymentPlan repaymentPlan = iterator.next();
            repaymentPlanOverdueOperator.checkAndUpdateOverdue(repaymentPlan,false);
        }
        return repaymentPlanList;
    }

    @Override
    public List<RepaymentPlan> getRepaymentOrderListByLoanOrderId(Long loanOrderId, boolean isCheckOverdue) {
        LoanOrder loanOrder = loanOrderMapper.selectByTrdOrderId(loanOrderId);
        if (ObjectUtils.isEmpty(loanOrder)){
            throw new ServiceException("借款订单未找到");
        }
        if (!isCheckOverdue){
            List<RepaymentPlan> repaymentPlanList = repaymentPlanMapper.selectRepaymentPlanByLoanOrderId(loanOrder.getTrdLoanOrderId(), loanOrder.getProductId());
            return repaymentPlanList;
        }

        return this.getRepaymentOrderListByLoanOrderId(loanOrderId);
    }


    @Override
    public List<RepaymentPlan> getAllRepaymentPlansBySingleId(Long repaymentPlanId) {
        RepaymentPlan repaymentPlan = repaymentPlanMapper.selectByPrimaryKey(repaymentPlanId);
        List<RepaymentPlan> allRepaymentPlans = repaymentPlanMapper.selectRepaymentPlanByLoanOrderId(repaymentPlan.getLoanOrderId(), repaymentPlan.getProductId());
        for (Iterator<RepaymentPlan> iterator = allRepaymentPlans.iterator(); iterator.hasNext(); ) {
            RepaymentPlan item = iterator.next();
            repaymentPlanOverdueOperator.checkAndUpdateOverdue(item,false);

        }
        return allRepaymentPlans;
    }

    @Override
    public List<RepaymentPlan> getMatchedRepaymentPlansByIds(Long[] repaymentOrderIds) {
        List<RepaymentPlan> allRepaymentPlans = this.getAllRepaymentPlansBySingleId(repaymentOrderIds[0]);
        List<RepaymentPlan> matchedRepaymentPlans = new ArrayList<>();
        for (Iterator<RepaymentPlan> iterator = allRepaymentPlans.iterator(); iterator.hasNext(); ) {
            RepaymentPlan repaymentPlan = iterator.next();
            for (int i = 0; i < repaymentOrderIds.length; i++) {
                Long repaymentOrderId = repaymentOrderIds[i];
                if (repaymentPlan.getId().longValue() == repaymentOrderId) {
                    matchedRepaymentPlans.add(repaymentPlan);
                }
            }
        }

        // 如果找到的订单数据和传入参数不同，则说明是非同一笔借款单产生的数据，抛出异常
        if (matchedRepaymentPlans.size() != repaymentOrderIds.length) {
            throw new ServiceException("还款订单数据异常，非同一笔借款单");
        }
        return matchedRepaymentPlans;
    }


    @Override
    @Transactional
    public void checkAndUpdateOverdue(Long repaymentOrderId) {
        RepaymentPlan repaymentPlan = repaymentPlanMapper.selectByPrimaryKey(repaymentOrderId);
        repaymentPlanOverdueOperator.checkAndUpdateOverdue(repaymentPlan,true);
    }

    @Override
    public RiskDto getRiskDto(Long userId) {
        if (null == userId){
            throw new ServiceException("风控查询的用户ID为空");
        }
        // 返回对象
        RiskDto riskDto = new RiskDto();

        // 最近1次的逾期天数
        RepaymentPlan lastOverdueRepaymentPlan = repaymentPlanMapper.selectLastOverdueRepaymentPlanByUserId(userId, true);
        Integer lastOverdueDayCount = ObjectUtils.isEmpty(lastOverdueRepaymentPlan) ? null : lastOverdueRepaymentPlan.getOverdueDayCount();

        // 历史逾期的总记录数
        Integer historyOverdueRecordCount = repaymentPlanMapper.countByUserIdAndIsOverdue(userId, true);

        // 最近一次被拒绝的时间到当前时间的间隔天数
        TradeAppRiskDto tradeAppRiskDto = tradeAppRemoteService.getLastRejectDayCount(userId);

        // 赋值
        BeanUtils.copyProperties(tradeAppRiskDto, riskDto);
        riskDto.setLastOverdueDayCount(lastOverdueDayCount);
        riskDto.setHistoryOverdueRecordCount(historyOverdueRecordCount);
        return riskDto;
    }

    @Override
    public Long getUserIdByOrderBizSeqNo(String bizSeqNo) {
        if (StringUtils.isEmpty(bizSeqNo)){
            throw new ServiceException("查询的订单追踪号不可以为空");
        }
        Long userId = tradeAppRemoteService.getUserIdByOrderBizSeqNo(bizSeqNo);

        if (null == userId){
            throw new ServiceException("查询的订单追踪号未找到匹配的用户");
        }
        return userId;
    }

    @Override
    public void updateRepaymentPlanAmount(AlipayRepamentPlanDto alipayRepaymentPlan) {
        logger.info("更新入账金额：repaymentPlanId= {},amount= {},version= {}", alipayRepaymentPlan.getId(), alipayRepaymentPlan.getAmount(), alipayRepaymentPlan.getVersion());
        int count = repaymentPlanMapper.updateRepaymentPlanAmountById(alipayRepaymentPlan);
        logger.info("count= {}", count);
        if (count != 1) {
            throw CashmanExceptionBuilder.build(ErrorCodeConstants.REPAY_PLAN_INCREASE_WAITING_AMT_ERR);
        }
    }

    @Override
    public void updateRepaymentPlanAmount4Income(AlipayRepamentPlanDto alipayRepaymentPlan) {
        logger.info("更新入账金额：repaymentPlanId= {},amount= {},version= {}", alipayRepaymentPlan.getId(), alipayRepaymentPlan.getAmount(), alipayRepaymentPlan.getVersion());
        int count = repaymentPlanMapper.updateRepaymentPlanAmount(alipayRepaymentPlan);
        logger.info("count= {}", count);
        if (count != 1) {
            throw CashmanExceptionBuilder.build(ErrorCodeConstants.REPAY_PLAN_INCREASE_WAITING_AMT_ERR);
        }
    }

    @Override
    public PageInfo<RepaymentNoticeDto> getReapymentPlansForNotice(NoticeOrdersReq noticeOrdersReq){
        //Integer pageNum,Integer pageSize,String loanStatus,String repaymentStatus, Date repaymentPlanTime,Integer productCategory,String merchantNo,Long productId) {
        logger.info("查询满足条件的还款计划信息开始--");
        PageHelper.startPage(noticeOrdersReq.getPageNum(), noticeOrdersReq.getPageSize());
        List<RepaymentNoticeDto> repaymentNoticeDtos = repaymentPlanMapper.getRepaymentPlanListForNotice(noticeOrdersReq.getStatus(),noticeOrdersReq.getRepayStatus(),noticeOrdersReq.getUseTime(),noticeOrdersReq.getCategoryType(),noticeOrdersReq.getMerchantNo(),noticeOrdersReq.getId());//.selectUnfreezeOrders(unfreezeOrdersReq.getId(),status,unfreezeOrdersReq.getUnfreezeTime());
        Page<RepaymentNoticeDto> page = (Page<RepaymentNoticeDto>) repaymentNoticeDtos;
        logger.info("result:{}", page);
        return page.toPageInfo();
    }
}
