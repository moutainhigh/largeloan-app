package com.xianjinxia.cashman.dto;

import java.io.Serializable;
import java.util.List;

import com.xianjinxia.cashman.response.LoanFeeDetails;
import com.xianjinxia.cashman.response.RepaymentPlanAdvance;

/**
 * 借款选择配置
 * 
 * @author liuzhifang
 *
 *         2017年10月11日
 */
public class LoanConfigData implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private LoanFeeDetails loanFeeDetails;

    private List<RepaymentPlanAdvance> repaymentPlanLists;

    public LoanFeeDetails getLoanFeeDetails() {
        return loanFeeDetails;
    }

    public void setLoanFeeDetails(LoanFeeDetails loanFeeDetails) {
        this.loanFeeDetails = loanFeeDetails;
    }

    public List<RepaymentPlanAdvance> getRepaymentPlanLists() {
        return repaymentPlanLists;
    }

    public void setRepaymentPlanLists(List<RepaymentPlanAdvance> repaymentPlanLists) {
        this.repaymentPlanLists = repaymentPlanLists;
    }



}
