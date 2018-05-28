package com.xianjinxia.cashman.enums;

/**
 * Created by Administrator on 2017/11/14 0014.
 */
public enum IndexRepaymentDtoEnum {

    DHK("10","待还款"),
    YYQ("-11","已逾期"),
    YHQ("30","已还清");

    private String  code;
    private String msg;

    IndexRepaymentDtoEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
