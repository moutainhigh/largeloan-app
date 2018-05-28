package com.xianjinxia.cashman.service.repay.checker;

import com.xianjinxia.cashman.domain.LoanOrder;
import com.xianjinxia.cashman.domain.Products;
import com.xianjinxia.cashman.domain.RepaymentPlan;

import java.util.List;

/**
 * 还款的检查对象
 */
public class RepaymentCheckParam {

    private Integer repaymentAmount;
    private List<RepaymentPlan> allRepaymentPlans;
    private List<RepaymentPlan> matchedrepaymentPlans;
    private Products products;
    private LoanOrder loanOrder;
    private boolean isHistoryOverdue;
    private boolean isRepayAll;
    private boolean isLoanRepayOneDay;
    public List<RepaymentPlan> getAllRepaymentPlans() {
        return allRepaymentPlans;
    }

    public void setAllRepaymentPlans(List<RepaymentPlan> allRepaymentPlans) {
        this.allRepaymentPlans = allRepaymentPlans;
    }

    public List<RepaymentPlan> getMatchedrepaymentPlans() {
        return matchedrepaymentPlans;
    }

    public void setMatchedrepaymentPlans(List<RepaymentPlan> matchedrepaymentPlans) {
        this.matchedrepaymentPlans = matchedrepaymentPlans;
    }

    public Products getProducts() {
        return products;
    }

    public void setProducts(Products products) {
        this.products = products;
    }

    public Integer getRepaymentAmount() {
        return repaymentAmount;
    }

    public void setRepaymentAmount(Integer repaymentAmount) {
        this.repaymentAmount = repaymentAmount;
    }

    public LoanOrder getLoanOrder() {
        return loanOrder;
    }

    public void setLoanOrder(LoanOrder loanOrder) {
        this.loanOrder = loanOrder;
    }

    public RepaymentCheckParam() {
    }


    public boolean isHistoryOverdue() {
        return isHistoryOverdue;
    }

    public void setHistoryOverdue(boolean historyOverdue) {
        isHistoryOverdue = historyOverdue;
    }

    public boolean isRepayAll() {
        return isRepayAll;
    }

    public void setRepayAll(boolean repayAll) {
        isRepayAll = repayAll;
    }

    public boolean isLoanRepayOneDay() {
        return isLoanRepayOneDay;
    }

    public void setLoanRepayOneDay(boolean loanRepayOneDay) {
        isLoanRepayOneDay = loanRepayOneDay;
    }

    public RepaymentCheckParam(Integer repaymentAmount, List<RepaymentPlan> allRepaymentPlans, List<RepaymentPlan> matchedrepaymentPlans, Products products, LoanOrder loanOrder) {
        this.repaymentAmount = repaymentAmount;
        this.allRepaymentPlans = allRepaymentPlans;
        this.matchedrepaymentPlans = matchedrepaymentPlans;
        this.products = products;
        this.loanOrder = loanOrder;
    }

    public RepaymentCheckParam(Integer repaymentAmount, List<RepaymentPlan> allRepaymentPlans, List<RepaymentPlan> matchedrepaymentPlans, Products products, LoanOrder loanOrder, boolean isHistoryOverdue, boolean isRepayAll, boolean isLoanRepayOneDay) {
        this.repaymentAmount = repaymentAmount;
        this.allRepaymentPlans = allRepaymentPlans;
        this.matchedrepaymentPlans = matchedrepaymentPlans;
        this.products = products;
        this.loanOrder = loanOrder;
        this.isHistoryOverdue = isHistoryOverdue;
        this.isRepayAll = isRepayAll;
        this.isLoanRepayOneDay = isLoanRepayOneDay;
    }
}
