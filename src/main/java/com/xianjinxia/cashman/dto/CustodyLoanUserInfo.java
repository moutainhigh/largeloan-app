package com.xianjinxia.cashman.dto;

/**
 * Created by xuehan on 2017/7/11.
 */
public class CustodyLoanUserInfo {
    // 身份标识
    protected String idCardNo;
    // 证件类型 1，身份证
    private String certificateType = "1";

    public String getIdCardNo() {
        return idCardNo;
    }

    public void setIdCardNo(String idCardNo) {
        this.idCardNo = idCardNo;
    }

    public String getCertificateType() {
        return certificateType;
    }

    public void setCertificateType(String certificateType) {
        this.certificateType = certificateType;
    }

    @Override
    public String toString() {
        return "CustodyLoanUserInfo{" +
                "idCardNo='" + idCardNo + '\'' +
                ", certificateType='" + certificateType + '\'' +
                '}';
    }
}
