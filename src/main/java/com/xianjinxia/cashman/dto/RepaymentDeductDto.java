package com.xianjinxia.cashman.dto;

public class RepaymentDeductDto {

    private Long repaymentPlanId;
    private Long deductAmount;
    private Integer deductType;

    public RepaymentDeductDto(Long repaymentPlanId, Long deductAmount, Integer deductType) {
        this.repaymentPlanId = repaymentPlanId;
        this.deductAmount = deductAmount;
        this.deductType = deductType;
    }

    public Long getRepaymentPlanId() {
        return repaymentPlanId;
    }

    public void setRepaymentPlanId(Long repaymentPlanId) {
        this.repaymentPlanId = repaymentPlanId;
    }

    public Long getDeductAmount() {
        return deductAmount;
    }

    public void setDeductAmount(Long deductAmount) {
        this.deductAmount = deductAmount;
    }

    public Integer getDeductType() {
        return deductType;
    }

    public void setDeductType(Integer deductType) {
        this.deductType = deductType;
    }

    @Override
    public String toString() {
        return "RepaymentDeductDto{" + "repaymentPlanId=" + repaymentPlanId + ", deductAmount=" + deductAmount + ", deductType=" + deductType + '}';
    }

    public RepaymentDeductDto() {
    }
}
