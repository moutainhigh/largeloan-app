package com.xianjinxia.cashman.dto;

import java.util.Date;

/**
 * 还款计划
 * Created by liquan on 2018/1/4.
 */
public class RepaymentPlanInfoDto {

    public RepaymentPlanInfoDto(){}

    public RepaymentPlanInfoDto(String id, String loanOrderId, String repaymentId, Long user_id,
        Integer period, Integer planWillRepaymentAmount,Integer  planRepaymentMoney,String planRepaymentTime, Integer counterFee,
        Integer trueRepaymentMoney, String trueRepaymentTime, Integer interests, Integer lateFee,
        Integer principal, Integer lateDay, String isOverdue, Integer couponMoney,
        String status) {
        this.id = id;
        this.order_id = loanOrderId;
        this.repayment_id = repaymentId;
        this.user_id = change(user_id);
        this.period = change(period);
        this.plan_will_repayment_amount = change(planWillRepaymentAmount);
        this.plan_repayment_time = planRepaymentTime;
        this.counter_fee = change(counterFee);
        this.true_repayment_money = change(trueRepaymentMoney);
        this.true_repayment_time = trueRepaymentTime;
        this.interests = change(interests);
        this.late_fee = change(lateFee);
        this.principal = change(principal);
        this.late_day = change(lateDay);
        this.is_overdue = isOverdue;
        this.coupon_money = change(couponMoney);
        this.status = status;
        this.plan_repayment_money = change(planRepaymentMoney);
    }

    //           分期还款订单号
    String id;
    //订单号
    String order_id;

    // 总还款订单号 和 orderId 相同
    String repayment_id;

    String plan_repayment_money;

    //    用户ID
    String user_id;
    //当前期数
    String period;
    //    预期还款金额
//    @JSONField(name="")
//    repaymentTotalAmount
    String plan_will_repayment_amount;

    //    预期还款时间
//    @JSONField(name="planRepaymentTime")
//        repaymentPlanTime
    String plan_repayment_time;
    String plan_next_repayment_time;
    //TODO: plan_next_repayment_time 预期下一还款日期   暂时先去掉
    //  TODO:  plan_will_repayment_amount 预期剩余还款金额  暂时先去掉
    // counter_fee 服务费 目前没有先填0

    String counter_fee="0";

    //    实际还款金额
//    @JSONField(name="trueRepaymentMoney")
//    repaymentIncomeAmount
    String true_repayment_money;

    //实际还款时间
//    @JSONField(name="trueRepaymentTime")
//        repaymentRealTime
    String true_repayment_time;

    public String getPlan_next_repayment_time() {
        return plan_next_repayment_time;
    }

    public void setPlan_next_repayment_time(String plan_next_repayment_time) {
        this.plan_next_repayment_time = plan_next_repayment_time;
    }

    //    利息
//    @JSONField(name="interests")
//        repaymentInterestAmount
    String interests;
    //    Late_fee 滞纳金
//    @JSONField(name="lateFee ")
//        overdueFeeAmount
    String late_fee;
    //    本金
//    repaymentPrincipalAmount
    String principal;
    //  late_day 滞纳天数
//    @JSONField(name="lateDay")
//        overdueDayCount
    String late_day;
    //    是否逾期
    String is_overdue;
    //   coupon_money 券抵扣金额 暂时为0
    String coupon_money="0";
    //    还款订单状态
    String status;

    public String getPlan_repayment_money() {
        return plan_repayment_money;
    }

    public void setPlan_repayment_money(String plan_repayment_money) {
        this.plan_repayment_money = plan_repayment_money;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLoanOrderId() {
        return order_id;
    }

    public void setLoanOrderId(String loanOrderId) {
        this.order_id = loanOrderId;
    }

    public String getRepaymentId() {
        return repayment_id;
    }

    public void setRepaymentId(String repaymentId) {
        this.repayment_id = repaymentId;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getPlanRepaymentMoney() {
        return plan_will_repayment_amount;
    }

    public void setPlanRepaymentMoney(String planRepaymentMoney) {
        this.plan_will_repayment_amount = planRepaymentMoney;
    }

    public String getPlanRepaymentTime() {
        return plan_repayment_time;
    }

    public void setPlanRepaymentTime(String planRepaymentTime) {
        this.plan_repayment_time = planRepaymentTime;
    }

    public String getCounterFee() {
        return counter_fee;
    }

    public void setCounterFee(String counterFee) {
        this.counter_fee = counterFee;
    }

    public String getTrueRepaymentMoney() {
        return true_repayment_money;
    }

    public void setTrueRepaymentMoney(String trueRepaymentMoney) {
        this.true_repayment_money = trueRepaymentMoney;
    }

    public String getTrueRepaymentTime() {
        return true_repayment_time;
    }

    public void setTrueRepaymentTime(String trueRepaymentTime) {
        this.true_repayment_time = trueRepaymentTime;
    }

    public String getInterests() {
        return interests;
    }

    public void setInterests(String interests) {
        this.interests = interests;
    }

    public String getLateFee() {
        return late_fee;
    }

    public void setLateFee(String lateFee) {
        this.late_fee = lateFee;
    }

    public String getPrincipal() {
        return principal;
    }

    public void setPrincipal(String principal) {
        this.principal = principal;
    }

    public String getLateDay() {
        return late_day;
    }

    public void setLateDay(String lateDay) {
        this.late_day = lateDay;
    }

    public String getOverdue() {
        return is_overdue;
    }

    public void setOverdue(String overdue) {
        is_overdue = overdue;
    }

    public String getCouponMoney() {
        return coupon_money;
    }

    public void setCouponMoney(String couponMoney) {
        this.coupon_money = couponMoney;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override public String toString() {
        return "RepaymentPlanInfoDto{" + "id='" + id + '\'' + ", order_id='" + order_id + '\''
            + ", repayment_id='" + repayment_id + '\'' + ", user_id=" + user_id + ", period=" + period
            + ", plan_will_repayment_amount=" + plan_will_repayment_amount
            + ", plan_repayment_time=" + plan_repayment_time + ", counter_fee=" + counter_fee
            + ", true_repayment_money=" + true_repayment_money + ", true_repayment_time="
            + true_repayment_time + ", interests=" + interests + ", late_fee=" + late_fee
            + ", principal=" + principal + ", late_day=" + late_day + ", is_overdue=" + is_overdue
            + ", coupon_money=" + coupon_money + ", status='" + status + '\'' + '}';
    }

    String change(Object o){
        if(null!=o){
            return o.toString();
        }
        return "";
    }
}
