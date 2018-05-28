package com.xianjinxia.cashman.dto;

import java.util.List;

/**
 * Created by liquan on 2018/1/4.
 */
public class RiskQueryDto {

    // 订单信息
    public List<OrderInfoDto> order;

    //    还款计划
    List<RepaymentPlanInfoDto> repayment_period;

    // 还款详情 根据还款计划分析后得出
    List<RepaymentDto> repayment;

    OrderInfoDto current_order;

    UserInfoDto user_info;

    UserContactDto user_contact;


    List<UserUploadLogDto>  user_upload_log;


    public OrderInfoDto getCurrent_order() {
        return current_order;
    }

    public void setCurrent_order(OrderInfoDto current_order) {
        this.current_order = current_order;
    }


    public UserInfoDto getUser_info() {
        return user_info;
    }

    public void setUser_info(UserInfoDto user_info) {
        this.user_info = user_info;
    }

    public List<OrderInfoDto> getOrder() {
        return order;
    }

    public void setOrder(List<OrderInfoDto> order) {
        this.order = order;
    }

    public UserContactDto getUser_contact() {
        return user_contact;
    }

    public void setUser_contact(UserContactDto user_contact) {
        this.user_contact = user_contact;
    }

    public List<UserUploadLogDto> getUser_upload_log() {
        return user_upload_log;
    }

    public void setUser_upload_log(List<UserUploadLogDto> user_upload_log) {
        this.user_upload_log = user_upload_log;
    }


    public List<RepaymentPlanInfoDto> getRepayment_period() {
        return repayment_period;
    }

    public void setRepayment_period(List<RepaymentPlanInfoDto> repayment_period) {
        this.repayment_period = repayment_period;
    }

    public List<RepaymentDto> getRepayment() {
        return repayment;
    }

    public void setRepayment(List<RepaymentDto> repayment) {
        this.repayment = repayment;
    }

    @Override public String toString() {
        return "RiskQueryDto{" + "order=" + order + ", repayment_period=" + repayment_period
            + ", repay=" + repayment + ", current_order=" + current_order + ", user_info="
            + user_info + ", user_contact=" + user_contact + ", user_upload_log=" + user_upload_log
            + '}';
    }
}









