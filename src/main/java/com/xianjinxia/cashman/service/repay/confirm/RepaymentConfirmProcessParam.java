package com.xianjinxia.cashman.service.repay.confirm;

import java.util.List;
import java.util.Map;

import com.xianjinxia.cashman.domain.LoanOrder;
import com.xianjinxia.cashman.domain.PaymentRequest;
import com.xianjinxia.cashman.domain.Products;
import com.xianjinxia.cashman.domain.RepaymentPlan;
import com.xianjinxia.cashman.domain.RepaymentRecord;
import com.xianjinxia.cashman.request.PayCenterCallbackReq;

public class RepaymentConfirmProcessParam {

    private PayCenterCallbackReq payCenterCallbackReq;

    private LoanOrder loanOrder;

    private PaymentRequest paymentRequest;

    private List<RepaymentRecord> repaymentRecordList;

    private List<RepaymentPlan> matchedRepaymentPlans;

    private Integer totalPrincipalAmount;

    private List<RepaymentPlan> allRepaymentPlans;

    private Map<Long, Products> productsMap;

    private String productCategory;

    public PaymentRequest getPaymentRequest() {
        return paymentRequest;
    }

    public void setPaymentRequest(PaymentRequest paymentRequest) {
        this.paymentRequest = paymentRequest;
    }

    public List<RepaymentRecord> getRepaymentRecordList() {
        return repaymentRecordList;
    }

    public void setRepaymentRecordList(List<RepaymentRecord> repaymentRecordList) {
        this.repaymentRecordList = repaymentRecordList;
    }

    public List<RepaymentPlan> getMatchedRepaymentPlans() {
        return matchedRepaymentPlans;
    }

    public void setMatchedRepaymentPlans(List<RepaymentPlan> matchedRepaymentPlans) {
        this.matchedRepaymentPlans = matchedRepaymentPlans;
    }

    public List<RepaymentPlan> getAllRepaymentPlans() {
        return allRepaymentPlans;
    }

    public void setAllRepaymentPlans(List<RepaymentPlan> allRepaymentPlans) {
        this.allRepaymentPlans = allRepaymentPlans;
    }

    public Map<Long, Products> getProductsMap() {
        return productsMap;
    }

    public void setProductsMap(Map<Long, Products> productsMap) {
        this.productsMap = productsMap;
    }

    public String getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }

    public LoanOrder getLoanOrder() {
        return loanOrder;
    }

    public void setLoanOrder(LoanOrder loanOrder) {
        this.loanOrder = loanOrder;
    }

    public PayCenterCallbackReq getPayCenterCallbackReq() {
        return payCenterCallbackReq;
    }

    public void setPayCenterCallbackReq(PayCenterCallbackReq payCenterCallbackReq) {
        this.payCenterCallbackReq = payCenterCallbackReq;
    }

    public Integer getTotalPrincipalAmount() {
        return totalPrincipalAmount;
    }

    public void setTotalPrincipalAmount(Integer totalPrincipalAmount) {
        this.totalPrincipalAmount = totalPrincipalAmount;
    }

    @Override
    public String toString() {
        return "RepaymentConfirmProcessParam{" + "loanOrder=" + loanOrder + ", paymentRequest=" + paymentRequest + ", repaymentRecordList=" + repaymentRecordList + ", matchedRepaymentPlans=" + matchedRepaymentPlans + ", allRepaymentPlans=" + allRepaymentPlans + ", productsMap=" + productsMap + ", productCategory='" + productCategory + '\'' + '}';
    }
}
