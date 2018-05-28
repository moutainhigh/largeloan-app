package com.xianjinxia.cashman.dto;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 风控查询用户信息bean
 * Created by liquan on 2018/1/4.
 */

public class UserInfoDto {
    //用户id
    @SerializedName(value = "user_id",alternate="userId")
    String user_id;
    // 用户姓名
    String name;
    //    用户手机号
    String phone;
    //    用户身份证号
    @SerializedName(value = "id_number",alternate="idNumber") String id_number;
    //      注册时间
    @SerializedName(value = "reg_time",alternate="regTime") String reg_time;
    //    注册IP
    @SerializedName(value = "reg_ip",alternate="regIp")  String reg_ip;

    //    是否老用户
    @SerializedName(value = "customer_type",alternate="customerType") String customer_type;

    //    学历
    String degrees;
    //    公司名称
    @SerializedName(value = "company_name",alternate="companyName") String company_name;
    //    常住地址
    @SerializedName(value = "live_address",alternate="liveAddress") String live_address;
    //    常住地址经度
    @SerializedName(value = "live_longitude",alternate="liveLongitude") String live_longitude;
    //    常住地址维度
    @SerializedName(value = "live_latitude",alternate="liveLatitude") String live_latitude;
    //    居住时长
    @SerializedName(value = "live_time_type",alternate="liveTimeType") String live_time_type;
    //    婚姻状况 []
    String marriage;
    //     当前用户最大可借额度
    @SerializedName(value = "credit_total",alternate="creditTotal") String credit_total;
    // 身份证、人脸识别数量
    @SerializedName(value = "id_card_count",alternate="idCardCount") Integer id_card_count;

    // 身份证 正面照
    @SerializedName(value = "id_card_z",alternate="idCardZ") List<RiskQueryUserInfoImageDto> id_card_z;
    //      身份证反面照
    @SerializedName(value = "id_card_f",alternate="idCardF") List<RiskQueryUserInfoImageDto> id_card_f;
    //    人脸照片
    @SerializedName(value = "face_recognition",alternate="faceRecognition") List<RiskQueryUserInfoImageDto> face_recognition;
    //工作证照
    @SerializedName(value = "work_card",alternate="workCard") List<RiskQueryUserInfoImageDto> work_card;
    //    是否VIP
    @SerializedName(value = "is_vip",alternate="isVip") Integer is_vip;

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public List<RiskQueryUserInfoImageDto> getIdCard_z() {
        return id_card_z;
    }

    public void setIdCard_z(List<RiskQueryUserInfoImageDto> idCard_z) {
        this.id_card_z = idCard_z;
    }

    public List<RiskQueryUserInfoImageDto> getIdCard_f() {
        return id_card_f;
    }

    public void setIdCard_f(List<RiskQueryUserInfoImageDto> idCard_f) {
        this.id_card_f = idCard_f;
    }

    public List<RiskQueryUserInfoImageDto> getFaceRecognition() {
        return face_recognition;
    }

    public void setFaceRecognition(List<RiskQueryUserInfoImageDto> faceRecognition) {
        this.face_recognition = faceRecognition;
    }

    public List<RiskQueryUserInfoImageDto> getWorkCard() {
        return work_card;
    }

    public void setWorkCard(List<RiskQueryUserInfoImageDto> workCard) {
        this.work_card = workCard;
    }

    public Integer getIsVip() {
        return is_vip;
    }

    public void setIsVip(Integer isVip) {
        this.is_vip = isVip;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getIdNumber() {
        return id_number;
    }

    public void setIdNumber(String idNumber) {
        this.id_number = idNumber;
    }

    public String getRegTime() {
        return reg_time;
    }

    public void setRegTime(String regTime) {
        this.reg_time = regTime;
    }

    public String getRegIp() {
        return reg_ip;
    }

    public void setRegIp(String regIp) {
        this.reg_ip = regIp;
    }

    public String getCustomerType() {
        return customer_type;
    }

    public void setCustomerType(String customerType) {
        this.customer_type = customerType;
    }

    public String getDegrees() {
        return degrees;
    }

    public void setDegrees(String degrees) {
        this.degrees = degrees;
    }

    public String getCompanyName() {
        return company_name;
    }

    public void setCompanyName(String companyName) {
        this.company_name = companyName;
    }

    public String getLiveAddress() {
        return live_address;
    }

    public void setLiveAddress(String liveAddress) {
        this.live_address = liveAddress;
    }

    public String getLiveLongitude() {
        return live_longitude;
    }

    public void setLiveLongitude(String liveLongitude) {
        this.live_longitude = liveLongitude;
    }

    public String getLiveLatitude() {
        return live_latitude;
    }

    public void setLiveLatitude(String liveLatitude) {
        this.live_latitude = liveLatitude;
    }

    public String getLiveTimeType() {
        return live_time_type;
    }

    public void setLiveTimeType(String liveTimeType) {
        this.live_time_type = liveTimeType;
    }

    public String getMarriage() {
        return marriage;
    }

    public void setMarriage(String marriage) {
        this.marriage = marriage;
    }

    public String getCreditTotal() {
        return credit_total;
    }

    public void setCreditTotal(String creditTotal) {
        this.credit_total = creditTotal;
    }

    public Integer getIdCardCount() {
        return id_card_count;
    }

    public void setIdCardCount(Integer idCardCount) {
        this.id_card_count = idCardCount;
    }


    @Override public String toString() {
        return "UserInfoDto{" + "name='" + name + '\'' + ", phone='" + phone + '\''
            + ", id_number='" + id_number + '\'' + ", reg_time='" + reg_time + '\'' + ", reg_ip='"
            + reg_ip + '\'' + ", customer_type='" + customer_type + '\'' + ", degrees='" + degrees
            + '\'' + ", company_name='" + company_name + '\'' + ", live_address='" + live_address
            + '\'' + ", live_longitude='" + live_longitude + '\'' + ", live_latitude='"
            + live_latitude + '\'' + ", live_time_type='" + live_time_type + '\'' + ", marriage='"
            + marriage + '\'' + ", credit_total='" + credit_total + '\'' + ", id_card_count="
            + id_card_count + ", id_card_z=" + id_card_z + ", id_card_f=" + id_card_f
            + ", face_recognition=" + face_recognition + ", work_card=" + work_card + ", is_vip="
            + is_vip + '}';
    }

}







