package com.xianjinxia.cashman.dto;

import com.alibaba.fastjson.JSON;

public class WithholdTimerTaskParams {

    private String repaymentPlanTime;

    public String getRepaymentPlanTime() {
        return repaymentPlanTime;
    }

    public void setRepaymentPlanTime(String repaymentPlanTime) {
        this.repaymentPlanTime = repaymentPlanTime;
    }


    public String toJsonString(){
        return JSON.toJSONString(this);
    }
}
