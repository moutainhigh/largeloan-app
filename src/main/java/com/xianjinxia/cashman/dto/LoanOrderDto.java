package com.xianjinxia.cashman.dto;

import com.xianjinxia.cashman.domain.LoanOrder;

import java.util.List;

/**
 * @author whb
 * @date 2018/1/4.
 */
public class LoanOrderDto extends LoanOrder {

    private List<CustodyRepayScheduleDtailInfo> repaymentPlans;


    private String term;

    private String termType;

    public List<CustodyRepayScheduleDtailInfo> getRepaymentPlans() {
        return repaymentPlans;
    }

    public void setRepaymentPlans(List<CustodyRepayScheduleDtailInfo> repaymentPlans) {
        this.repaymentPlans = repaymentPlans;
    }


    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public String getTermType() {
        return termType;
    }

    public void setTermType(String termType) {
        this.termType = termType;
    }
}
