package com.xianjinxia.cashman.dto;

/**
 * 用户登录日志
 * Created by liquan on 2018/1/4.
 */
public class UserUploadLogDto {

    //    经度
    String longitude;
    //    维度
    String latitude;
    //    地址
    String address;
    //    上报时间
    String time;
    //    设备版本
    String osVersion ="";
    //    应用版本
    String appVersion;
    //    设备名称
    String deviceName;
    //    应用市场
    String appMarket;
    //    设备ID
    String deviceId;

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getOsVersion() {
        return osVersion;
    }

    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getAppMarket() {
        return appMarket;
    }

    public void setAppMarket(String appMarket) {
        this.appMarket = appMarket;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    @Override public String toString() {
        return "UserUploadLogDto{" + "longitude='" + longitude + '\'' + ", latitude='" + latitude
            + '\'' + ", address='" + address + '\'' + ", time='" + time + '\'' + ", osVersion='"
            + osVersion + '\'' + ", appVersion='" + appVersion + '\'' + ", deviceName='"
            + deviceName + '\'' + ", appMarket='" + appMarket + '\'' + ", deviceId='" + deviceId
            + '\'' + '}';
    }
}
