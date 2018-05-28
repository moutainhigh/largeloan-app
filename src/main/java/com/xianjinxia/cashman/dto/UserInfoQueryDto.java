package com.xianjinxia.cashman.dto;

import java.util.List;

/**
 * Created by liquan on 2018/1/19.
 */
public class UserInfoQueryDto {

    UserInfoDto user_info;

    UserContactDto user_contact;

    List<UserUploadLogDto> user_upload_log;

    public UserInfoDto getUser_info() {
        return user_info;
    }

    public void setUser_info(UserInfoDto user_info) {
        this.user_info = user_info;
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
}
