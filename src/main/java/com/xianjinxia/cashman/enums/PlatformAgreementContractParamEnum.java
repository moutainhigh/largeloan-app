package com.xianjinxia.cashman.enums;

/**
 * Created by liquan on 2017/11/24.
 */
public enum PlatformAgreementContractParamEnum {
    PARM_LOAN_ORDER_ID("###loanOrderId###","loanOrderId"),
    PARM_COMPANY_TITLE("###companyTitle###","companyTitle"),
    PARM_REAL_NAME("###realName###","realName"),
    PARM_MASK_IDCARD_NUM("###maskIdCardNum###","maskIdCardNum"),
    PARM_LOAN_ENDTIME("###loanEndTime###","loanEndTime"),
    PARM_MONEYA_MOUNT("###moneyAmount###","moneyAmount"),
    PARM_INTEREST("###interest###","interest"),
    PARM_STATUS("###status###","status"),
    PARM_LOAN_TIME("###loanTime###","loanTime"),
    PARM_APP_NAME("###appName###","appName"),
    PARM_PERIOD("###period###","period"),
    PARM_REPAYMENT_AMOUNT("###repaymentAmount###","repaymentAmount"),
    PARM_OVERDUE_RARE("###overdueRate###","overdueRate");

    private String code;
    private String name;

    PlatformAgreementContractParamEnum(String code, String name) {
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
