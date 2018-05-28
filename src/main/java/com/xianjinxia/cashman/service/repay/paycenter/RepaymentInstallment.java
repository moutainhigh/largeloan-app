package com.xianjinxia.cashman.service.repay.paycenter;

/**
 * Created by wangwei on 2018/1/2.
 */
public class RepaymentInstallment {

    private Long installmentAmount; // 还款金额（本金+利息）
    private Integer  period; // 第几期
    private Long installmentPrincipal;// 还款本金
    private Long installInterest; // 还款利息
    /**
     * 1 是正常还款的时候推过来 比如 1期的大额债权或者多期的 你想一期期还 不一定是到期的可能没到期的都可以通过这个方式还
     2 是提前还款  ,必须之前没有还过，这次一次结清
     3 是异常情况 比如提现失败 或者  单边账这种
     */
    private Integer repayType; // 还款方式

    public Long getInstallmentAmount() {
        return installmentAmount;
    }

    public void setInstallmentAmount(Long installmentAmount) {
        this.installmentAmount = installmentAmount;
    }

    public Integer getPeriod() {
        return period;
    }

    public void setPeriod(Integer period) {
        this.period = period;
    }

    public Long getInstallmentPrincipal() {
        return installmentPrincipal;
    }

    public void setInstallmentPrincipal(Long installmentPrincipal) {
        this.installmentPrincipal = installmentPrincipal;
    }

    public Long getInstallInterest() {
        return installInterest;
    }

    public void setInstallInterest(Long installInterest) {
        this.installInterest = installInterest;
    }

    public Integer getRepayType() {
        return repayType;
    }

    public void setRepayType(Integer repayType) {
        this.repayType = repayType;
    }

    @Override
    public String toString() {
        return "RepaymentInstallment{" +
                "installmentAmount=" + installmentAmount +
                ", period=" + period +
                ", installmentPrincipal=" + installmentPrincipal +
                ", installInterest=" + installInterest +
                ", repayType=" + repayType +
                '}';
    }
}
