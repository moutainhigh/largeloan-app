package com.xianjinxia.cashman.dto;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Date;

/**
 * 还款信息DTO
 * Created by liquan on 2018/1/5.
 */
public class RepaymentDto {

    //    还款订单号
    Long id;
    //    订单号
    @JSONField(name="orderId")
    Long order_id;
    //    应还款金额
    @JSONField(name="totalMoney")
    Integer total_money;

    //    实际还款金额
    @JSONField(name="trueTotalMoney")
    Integer true_total_money;
        //    实际还款时间
    @JSONField(name="trueRepaymentTime")
    Date updatedAt;

    //plan_repayment_time   计划结算日期
    // next_loan_advice  催收建议
    //    是否逾期
//    Boolean isOverdue;
//    //    逾期天数
//    @JSONField(name="overdueDay")
//    Integer overdueDayCount;

    //    还款订单状态
    Integer status;


    @JSONField(name="plan_repayment_time")
    String plan_repayment_time;

    @JSONField(name="overdue_day")
    int overdue_day;



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRepaymentPlanId() {
        return order_id;
    }

    public void setRepaymentPlanId(Long repaymentPlanId) {
        this.order_id = repaymentPlanId;
    }

    public Integer getRepayPrincipalAmt() {
        return total_money;
    }

    public void setRepayPrincipalAmt(Integer repayPrincipalAmt) {
        this.total_money = repayPrincipalAmt;
    }

    public Integer getAmount() {
        return true_total_money;
    }

    public void setAmount(Integer amount) {
        this.true_total_money = amount;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getPlan_repayment_time() {
        return plan_repayment_time;
    }

    public void setPlan_repayment_time(String plan_repayment_time) {
        this.plan_repayment_time = plan_repayment_time;
    }

    public int getOverdue_day() {
        return overdue_day;
    }

    public void setOverdue_day(int overdue_day) {
        this.overdue_day = overdue_day;
    }
}
