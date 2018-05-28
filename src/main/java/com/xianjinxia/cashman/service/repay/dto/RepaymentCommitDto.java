package com.xianjinxia.cashman.service.repay.dto;

import com.xianjinxia.cashman.domain.RepaymentPlan;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RepaymentCommitDto {
    /**
     * 外部输入的还款金额
     */
    private Integer externalInputAmount;

    /**
     * 内部计算的还款金额
     */
    private Integer internalCalculateAmount;

    /**
     * 还款计划的ID数组对应的所有还款计划
     */
    private Map<Long, RepaymentPlan> matchedRepaymentPlanList = new HashMap<>();

    /**
     * 同一借款单下的所有还款计划
     */
    private List<RepaymentPlan> allRepaymentPlanList;

    public Integer getExternalInputAmount() {
        return externalInputAmount;
    }

    public void setExternalInputAmount(Integer externalInputAmount) {
        this.externalInputAmount = externalInputAmount;
    }

    public Integer getInternalCalculateAmount() {
        return internalCalculateAmount;
    }

    public void setInternalCalculateAmount(Integer internalCalculateAmount) {
        this.internalCalculateAmount = internalCalculateAmount;
    }

    public Map<Long, RepaymentPlan> getMatchedRepaymentPlanList() {
        return matchedRepaymentPlanList;
    }

    public void setMatchedRepaymentPlanList(Map<Long, RepaymentPlan> matchedRepaymentPlanList) {
        this.matchedRepaymentPlanList = matchedRepaymentPlanList;
    }

    public List<RepaymentPlan> getAllRepaymentPlanList() {
        return allRepaymentPlanList;
    }

    public void setAllRepaymentPlanList(List<RepaymentPlan> allRepaymentPlanList) {
        this.allRepaymentPlanList = allRepaymentPlanList;
    }
}
