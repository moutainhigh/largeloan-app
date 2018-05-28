package com.xianjinxia.cashman.service.repay.checker;

import java.util.Iterator;
import java.util.List;
import com.xianjinxia.cashman.exceptions.CashmanExceptionBuilder;
import com.xianjinxia.cashman.exceptions.ServiceException;

import org.springframework.stereotype.Service;

import com.xianjinxia.cashman.constants.ErrorCodeConstants;
import com.xianjinxia.cashman.domain.RepaymentPlan;
import com.xianjinxia.cashman.enums.RepaymentPlanStatusEnum;

@Service
public class RepaymentCheckerImpl implements RepaymentChecker {

    private RepaymentPlan minPeriodRepaymentPlan;

    private List<RepaymentPlan> allRepaymentPlanList;

    private List<RepaymentPlan> matchedRepaymentPlanList;

    @Override
    public void check(RepaymentCheckParam repaymentCheckParam) {
        this.allRepaymentPlanList = repaymentCheckParam.getAllRepaymentPlans();
        this.matchedRepaymentPlanList = repaymentCheckParam.getMatchedrepaymentPlans();
        Integer repaymentAmount = repaymentCheckParam.getRepaymentAmount();

        //存管新增：如果不是一笔一笔还款，并且不是一次性全部还清，则必须一笔一笔还款
        //历史有逾期不走存管
        if (!repaymentCheckParam.isHistoryOverdue()){
            if (matchedRepaymentPlanList.size()>1 && matchedRepaymentPlanList.size()!=allRepaymentPlanList.size()){
                throw new ServiceException("请单笔按期依次还款，或者一次全部还清");
            }
        }

        boolean isPeriodContinous = this.isPeriodContinuous();
        if(!isPeriodContinous){
            throw CashmanExceptionBuilder.build(ErrorCodeConstants.REPAY_COMMIT_CHECK_NOT_CONTINOUS);
        }

        boolean isHasUnRepaymentedOrdersBefore = this.isHasUnRepaymentedOrdersBefore(this.minPeriodRepaymentPlan);
        if (!isHasUnRepaymentedOrdersBefore){
            throw CashmanExceptionBuilder.build(ErrorCodeConstants.REPAY_COMMIT_CHECK_PREVIOUS_NOT_REPAYMENTED);
        }

        boolean isRepayAll = this.isRepayAll(repaymentAmount, this.getTotalRepaymentAmount());
        if (!isRepayAll){
            throw CashmanExceptionBuilder.build(ErrorCodeConstants.REPAY_COMMIT_CHECK_AMT_ERR);
        }
    }



    /**
     * 判断：是否连续的判断  最大期数 - 最小期数 + 1
     * e.g.
     *      2，3，4，5   (5-2+1)= 4, 此次还款订单是4笔，连续，验证通过
     *      2，3，6     (6-2+1) = 5, 此次还款订单是3笔，不连续，验证失败
     *
     * @return boolean
     */
    private boolean isPeriodContinuous() {
        // 先假设第一笔为最小期数的还款单
        this.minPeriodRepaymentPlan = this.matchedRepaymentPlanList.get(0);
        int minPeriod = minPeriodRepaymentPlan.getPeriod();
        int maxPeriod = minPeriodRepaymentPlan.getPeriod();

        for (Iterator<RepaymentPlan> iterator = this.matchedRepaymentPlanList.iterator(); iterator.hasNext(); ) {
            RepaymentPlan next =  iterator.next();
            if(next.getPeriod().intValue() > maxPeriod){
                maxPeriod = next.getPeriod();
            }

            if(next.getPeriod().intValue() < minPeriod){
                minPeriod = next.getPeriod();
                this.minPeriodRepaymentPlan = next;//判断连续还款的时候找到最小期数的未还款订单
            }
        }

        if((maxPeriod - minPeriod + 1) == this.matchedRepaymentPlanList.size()){
            return true;
        }

        return false;
    }

    private boolean isHasUnRepaymentedOrdersBefore(RepaymentPlan minPeriodRepaymentPlan) {
        // 如果是第一期的话，不用检查
        int period = minPeriodRepaymentPlan.getPeriod().intValue();
        if(period == 1){
            return true;
        }

        RepaymentPlan repaymentPlanPrevious = null;
        findPrevious : for (Iterator<RepaymentPlan> iterator = this.allRepaymentPlanList.iterator(); iterator.hasNext(); ) {
            RepaymentPlan next = iterator.next();
            if(next.getPeriod().intValue() == (period - 1)){
                repaymentPlanPrevious = next;
                break findPrevious;

            }
        }

        if(repaymentPlanPrevious == null){
            throw CashmanExceptionBuilder.build(ErrorCodeConstants.REPAY_COMMIT_CHECK_PREVIOUS_NOT_EXIST);
        }

        // 正常全部还款
        if(repaymentPlanPrevious.getStatus().intValue() == RepaymentPlanStatusEnum.Repaymented.getCode()){
            return true;
        }

        return false;
    }


    /**
     * 是否全额还款
     * @param externalInputAmount 外部输入金额
     * @param internalTotalAmount 内部计算金额
     * @return boolean
     */
    private boolean isRepayAll(Integer externalInputAmount, Integer internalTotalAmount){
        if(externalInputAmount.intValue() == internalTotalAmount.intValue()){
            return true;
        }
        return false;
    }


    private Integer getTotalRepaymentAmount(){
        int totalAmount = 0;
        for (Iterator<RepaymentPlan> iterator = this.matchedRepaymentPlanList.iterator(); iterator.hasNext(); ) {
            RepaymentPlan repaymentPlan = iterator.next();
            Integer repaymentAmount = repaymentPlan.getRepaymentTotalAmount();
            totalAmount += repaymentAmount;
        }
        return totalAmount;
    }
}
