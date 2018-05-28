package com.xianjinxia.cashman.dto;

/**
 * Created by liquan on 2017/11/27.
 */
public class UserNameInfoDto {
    private Long userId;
    private String indCardNumber;
    private String userRealName;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getIndCardNumber() {
        return indCardNumber;
    }

    public void setIndCardNumber(String indCardNumber) {
        this.indCardNumber = indCardNumber;
    }

    public String getUserRealName() {
        return userRealName;
    }

    public void setUserRealName(String userRealName) {
        this.userRealName = userRealName;
    }

    @Override
    public String toString() {
        return "UserNameInfoDto{" +
                "userId=" + userId + ", userRealName=" + userRealName + ", indCardNumber='" + indCardNumber+ '}';
    }
}
