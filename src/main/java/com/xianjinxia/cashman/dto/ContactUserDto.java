package com.xianjinxia.cashman.dto;

import com.google.gson.annotations.SerializedName;

/**
 * Created by liquan on 2018/1/10.
 */
public class ContactUserDto {

    @SerializedName(value = "name",alternate="regName")
    String name;
    //        用户手机号
    @SerializedName(value = "phone",alternate="regPhone")
    String phone;
    //    用户身份证号
    @SerializedName(value = "id_number",alternate="regIdNumber")
    String id_number;
    //        注册时间
    @SerializedName(value = "reg_time",alternate="regTime")
    String reg_time;
    //    注册IP
    @SerializedName(value = "reg_ip",alternate="regIp")
    String reg_ip;

    public String getRegName() {
        return name;
    }

    public void setRegName(String regName) {
        this.name = regName;
    }

    public String getRegPhone() {
        return phone;
    }

    public void setRegPhone(String regPhone) {
        this.phone = regPhone;
    }

    public String getRegIdNumber() {
        return id_number;
    }

    public void setRegIdNumber(String regIdNumber) {
        this.id_number = regIdNumber;
    }

    public String getRegRegTime() {
        return reg_time;
    }

    public void setRegRegTime(String regRegTime) {
        this.reg_time = regRegTime;
    }

    public String getRegRegIp() {
        return reg_ip;
    }

    public void setRegRegIp(String regRegIp) {
        this.reg_ip = regRegIp;
    }

    @Override public String toString() {
        return "ContactUserDto{" + "name='" + name + '\'' + ", phone='" + phone + '\''
            + ", id_number='" + id_number + '\'' + ", reg_time='" + reg_time + '\'' + ", reg_ip='"
            + reg_ip + '\'' + '}';
    }
}
