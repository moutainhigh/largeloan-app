package com.xianjinxia.cashman.dto;

/**
 *
 * @author JaJian
 * @create 2018-05-15 21:25
 **/
public class CountRepaymentPlanDto {

    private boolean isRepaymented;

    private Integer repaymentAmount;

    public boolean isRepaymented() {
        return isRepaymented;
    }

    public void setRepaymented(boolean repaymented) {
        isRepaymented = repaymented;
    }

    public Integer getRepaymentAmount() {
        return repaymentAmount;
    }

    public void setRepaymentAmount(Integer repaymentAmount) {
        this.repaymentAmount = repaymentAmount;
    }
}
