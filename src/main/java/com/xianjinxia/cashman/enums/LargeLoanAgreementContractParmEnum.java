package com.xianjinxia.cashman.enums;

/**
 * Created by liquan on 2017/11/24.
 */
public enum LargeLoanAgreementContractParmEnum {
    PARM_LOAN_ORDER_ID("###loanOrderId###","loanOrderId"),
    PARM_REAL_NAME("###realName###","realName"),
    PARM_MASK_IDCARD_NUM("###maskIdCardNum###","maskIdCardNum"),
    PARM_PARTB_REAL_NAME("###partBRealName###","partBRealName"),
    PARM_COMPANY_TITLE("###companyTitle###","companyTitle"),
    PARM_CAPITAL_NAME("###capitalName###","capitalName"),
    PARM_COMPANY_SHORT_TITLE("###companyShortTitle###","companyShortTitle"),
    PARM_CAPITAL_CITY("###capitalCity###","capitalCity"),
    PARM_MONEYA_MOUNT("###moneyAmount###","moneyAmount"),
    PARM_CN_MONEY_AMOUNT("###cnMoneyAmount###","cnMoneyAmount"),
    PARM_LOAN_PURPOSE("###loanPurpose###","loanPurpose"),
    PARM_LOAN_TIME("###loanTime###","loanTime"),
    PARM_LOAN_ENDTIME("###loanEndTime###","loanEndTime"),
    PARM_PAYMENT_METHOD("###paymentMethod###","paymentMethod"),
    PARM_INTEREST("###interest###","interest"),
    PARM_PERIOD("###period###","period"),
    PARM_COMPANY_CITY("###companyCity###","companyCity");

    private String code;
    private String name;

    LargeLoanAgreementContractParmEnum(String code, String name) {
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
}
