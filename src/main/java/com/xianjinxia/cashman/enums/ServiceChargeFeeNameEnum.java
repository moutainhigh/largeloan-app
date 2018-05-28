package com.xianjinxia.cashman.enums;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/11/14 0014.
 */
public enum ServiceChargeFeeNameEnum {

        // 咨询费
        CONSULTATION_FEE("consultation_fee", "咨询费"),
        // 管理费
        MANAGE_FEE("manage_fee", "管理费"),
        // 信审查询费
        LETTER_REVIEW_FEE("letter_review_fee", "信审查询费"),
        // 账户管理费
        ACCOUNT_MANAGEMENT_FEE("account_management_fee", "账户管理费");
        private String code;
        private String name;

        ServiceChargeFeeNameEnum(String code, String name) {
            this.code = code;
            this.name = name;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

    public static List<String> getFeeTypeEnum(){
        List<String> feeTypeList = new ArrayList<>();
        feeTypeList.add(ServiceChargeFeeNameEnum.ACCOUNT_MANAGEMENT_FEE.getCode());
        feeTypeList.add(ServiceChargeFeeNameEnum.CONSULTATION_FEE.getCode());
        feeTypeList.add(ServiceChargeFeeNameEnum.LETTER_REVIEW_FEE.getCode());
        feeTypeList.add(ServiceChargeFeeNameEnum.MANAGE_FEE.getCode());
        return feeTypeList;
    }
}
