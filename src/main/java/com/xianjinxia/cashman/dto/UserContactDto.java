package com.xianjinxia.cashman.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 *用户紧急联系人
 * Created by liquan on 2018/1/4.
 */

public class UserContactDto {

    //    第一联系人与联系人关系
    @SerializedName(value = "first_relation",alternate="firstRelation")
    String first_relation;
    //        第一联系人通讯录姓名
    @SerializedName(value = "first_name",alternate="firstName")
    String first_name;
    //    第一联系人手机号
    @SerializedName(value = "first_mobile",alternate="firstMobile")
    String first_mobile;
    //        第一联系人是否在本平台注册
    @SerializedName(value = "first_is_reg",alternate="firstIsReg")
    int first_is_reg;
    //    第一联系人注册信息[一维数组]
    @SerializedName(value = "first_reg_info",alternate="firstRegInfo")
    ContactUserDto  first_reg_info;

    //    第一联系人登录日志[最近一个月]
    @SerializedName(value = "first_user_upload_log",alternate="firstUserUploadLog")
    List<UserUploadLogDto> first_user_upload_log;
    //    第一联系人借款信息
    @SerializedName(value = "first_order",alternate="firstOrder")

    List<OrderInfoDto> first_order;
    //        第一联系人分期还款信息
    @SerializedName(value = "first_repayment_period",alternate="firstRepaymentPeriod")
    List<RepaymentPlanInfoDto> first_repayment_period;
    //    第一联系人总还款信息
    @SerializedName(value = "first_repayment",alternate="firstRepayment")
    List<RepaymentDto> first_repayment;
    //        第二联系人与联系人关系
    @SerializedName(value = "two_relation",alternate="twoRelation")
    String two_relation;
    //    第二联系人通讯录姓名
    @SerializedName(value = "two_name",alternate="twoName")
    String two_name;
    //        第二联系人手机号
    @SerializedName(value = "two_mobile",alternate="twoMobile")
    String two_mobile;
    //    第二联系人是否在本平台注册
    @SerializedName(value = "two_is_reg",alternate="twoIsReg")
    int two_is_reg;
    //    第二联系人注册信息[一维数组]
    @SerializedName(value = "two_reg_info",alternate="twoRegInfo")
    ContactUserDto two_reg_info;

    //    第二联系人登录日志[最近一个月]
    @SerializedName(value = "two_user_upload_log",alternate="twoUserUploadLog")
    List<UserUploadLogDto> two_user_upload_log;
    //    第二联系人借款信息
    @SerializedName(value = "two_order",alternate="twoOrder")
    List<OrderInfoDto> two_order;
    //        第二联系人分期还款信息
    @SerializedName(value = "two_repayment_period",alternate="twoRepaymentPeriod")
    List<RepaymentPlanInfoDto> two_repayment_period;
    //    第二联系人总还款信息
    @SerializedName(value = "two_repayment",alternate="twoRepayment")
    List<RepaymentDto> two_repayment;

    //第一联系人的id
    transient  String first_userId;

    //第二联系人的id
    transient  String two_userId;

    public String getFirst_relation() {
        return first_relation;
    }

    public void setFirst_relation(String first_relation) {
        this.first_relation = first_relation;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getFirst_mobile() {
        return first_mobile;
    }

    public void setFirst_mobile(String first_mobile) {
        this.first_mobile = first_mobile;
    }

    public int getFirst_is_reg() {
        return first_is_reg;
    }

    public void setFirst_is_reg(int first_is_reg) {
        this.first_is_reg = first_is_reg;
    }

    public ContactUserDto getFirst_reg_info() {
        return first_reg_info;
    }

    public void setFirst_reg_info(ContactUserDto first_reg_info) {
        this.first_reg_info = first_reg_info;
    }

    public List<UserUploadLogDto> getFirst_user_upload_log() {
        return first_user_upload_log;
    }

    public void setFirst_user_upload_log(List<UserUploadLogDto> first_user_upload_log) {
        this.first_user_upload_log = first_user_upload_log;
    }

    public List<OrderInfoDto> getFirst_order() {
        return first_order;
    }

    public void setFirst_order(List<OrderInfoDto> first_order) {
        this.first_order = first_order;
    }

    public List<RepaymentPlanInfoDto> getFirst_repayment_period() {
        return first_repayment_period;
    }

    public void setFirst_repayment_period(List<RepaymentPlanInfoDto> first_repayment_period) {
        this.first_repayment_period = first_repayment_period;
    }

    public List<RepaymentDto> getFirst_repayment() {
        return first_repayment;
    }

    public void setFirst_repayment(List<RepaymentDto> first_repayment) {
        this.first_repayment = first_repayment;
    }

    public String getTwo_relation() {
        return two_relation;
    }

    public void setTwo_relation(String two_relation) {
        this.two_relation = two_relation;
    }

    public String getTwo_name() {
        return two_name;
    }

    public void setTwo_name(String two_name) {
        this.two_name = two_name;
    }

    public String getTwo_mobile() {
        return two_mobile;
    }

    public void setTwo_mobile(String two_mobile) {
        this.two_mobile = two_mobile;
    }

    public int getTwo_is_reg() {
        return two_is_reg;
    }

    public void setTwo_is_reg(int two_is_reg) {
        this.two_is_reg = two_is_reg;
    }

    public ContactUserDto getTwo_reg_info() {
        return two_reg_info;
    }

    public void setTwo_reg_info(ContactUserDto two_reg_info) {
        this.two_reg_info = two_reg_info;
    }

    public List<UserUploadLogDto> getTwo_user_upload_log() {
        return two_user_upload_log;
    }

    public void setTwo_user_upload_log(List<UserUploadLogDto> two_user_upload_log) {
        this.two_user_upload_log = two_user_upload_log;
    }

    public List<OrderInfoDto> getTwo_order() {
        return two_order;
    }

    public void setTwo_order(List<OrderInfoDto> two_order) {
        this.two_order = two_order;
    }

    public List<RepaymentPlanInfoDto> getTwo_repayment_period() {
        return two_repayment_period;
    }

    public void setTwo_repayment_period(List<RepaymentPlanInfoDto> two_repayment_period) {
        this.two_repayment_period = two_repayment_period;
    }

    public List<RepaymentDto> getTwo_repayment() {
        return two_repayment;
    }

    public String getFirst_userId() {
        return first_userId;
    }

    public void setTwo_repayment(List<RepaymentDto> two_repayment) {
        this.two_repayment = two_repayment;
    }

    public void setFirst_userId(String first_userId) {
        this.first_userId = first_userId;
    }

    public String getTwo_userId() {
        return two_userId;
    }

    public void setTwo_userId(String two_userId) {
        this.two_userId = two_userId;
    }

    @Override
    public String toString() {
        return "UserContactDto{" +
                "first_relation='" + first_relation + '\'' +
                ", first_name='" + first_name + '\'' +
                ", first_mobile='" + first_mobile + '\'' +
                ", first_is_reg=" + first_is_reg +
                ", first_reg_info=" + first_reg_info +
                ", first_user_upload_log=" + first_user_upload_log +
                ", first_order=" + first_order +
                ", first_repayment_period=" + first_repayment_period +
                ", first_repayment=" + first_repayment +
                ", two_relation='" + two_relation + '\'' +
                ", two_name='" + two_name + '\'' +
                ", two_mobile='" + two_mobile + '\'' +
                ", two_is_reg=" + two_is_reg +
                ", two_reg_info=" + two_reg_info +
                ", two_user_upload_log=" + two_user_upload_log +
                ", two_order=" + two_order +
                ", two_repayment_period=" + two_repayment_period +
                ", two_repayment=" + two_repayment +
                '}';
    }
}
