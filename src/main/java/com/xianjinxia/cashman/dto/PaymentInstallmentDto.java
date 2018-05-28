package com.xianjinxia.cashman.dto;

/**
 *
 * repayType 详情见http://192.168.6.71:8090/pages/viewpage.action?pageId=3414835
 *
 * 1 是正常还款的时候推过来 比如 1期的大额债权或者多期的 你想一期期还 不一定是到期的可能没到期的都可以通过这个方式还
 * 2 是提前还款,这个是针对不款计划总数大于1 的情况 钱是真放出去 然后提前还了
 * 3 是异常情况 比如提现失败 或者 单边账这种
 */
public class PaymentInstallmentDto {

    private Integer period;//还款对应期数
    private Integer installmentAmount;//每期还款金额（本金+利息）,单位：分
    private Integer installmentPrincipal;//还款本金,单位：分
    private Integer installInterest;//还款利息,单位：分
    private Integer repayType;//还款方式

    public Integer getPeriod() {
        return period;
    }

    public void setPeriod(Integer period) {
        this.period = period;
    }

    public Integer getInstallmentAmount() {
        return installmentAmount;
    }

    public void setInstallmentAmount(Integer installmentAmount) {
        this.installmentAmount = installmentAmount;
    }

    public Integer getInstallmentPrincipal() {
        return installmentPrincipal;
    }

    public void setInstallmentPrincipal(Integer installmentPrincipal) {
        this.installmentPrincipal = installmentPrincipal;
    }

    public Integer getInstallInterest() {
        return installInterest;
    }

    public void setInstallInterest(Integer installInterest) {
        this.installInterest = installInterest;
    }

    public Integer getRepayType() {
        return repayType;
    }

    public void setRepayType(Integer repayType) {
        this.repayType = repayType;
    }
}
