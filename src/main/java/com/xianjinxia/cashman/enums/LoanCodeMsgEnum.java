package com.xianjinxia.cashman.enums;

/**
 * Created by fanmaowen on 2017/9/25 0025.
 */
public enum LoanCodeMsgEnum {

    /**
     * 成功
     */
    SUCCESS("00", "成功"),

    CHECK_NO_LOGIN("01","未登录"),

    CHECK_NEW_USER("02","您当前为新用户，信用正在审核中，请您耐心等待"),

    CHECK_LESS_PHONE("03","您认证的手机号很少使用，暂未通过信用审核，试试多打电看话再来看吧"),

    CHECK_BLACK_USER("04","黑名单用户"),

    CHECK_NO_BANK("05","请先绑定银行卡"),

    CHECK_PAY_PASSWORD_ERROR("06","支付密码错误"),

    CHECK_NO_REALNAME("07","请先实名认证"),

    CHECK_NO_FIRSTCONTACTPHONE("08","请完善紧急联系人信息"),

    CHECK_NO_ZMSTATUS("09","芝麻信用未认证"),

    CHECK_NO_JXLSTATUS("10","手机运营商未认证"),

    CHECK_NO_PRODUCT("11","找不到对应的产品"),

    CHECK_AMOUNT_RANGE("12","额度不在指定的范围"),

    CHECK_SYSMAX_AMOUNT("13","不能超过最高可借款额度"),

    CHECK_NO_TERM("14","借款天数不在指定的范围"),

    CHECK_ULTIMATE_ORDER("15","当前有一笔正在运行的订单"),

    CHECK_AMOUNT_TIMES("16","额度不是100的整数倍"),

    CHECK_NO_AUTH_MAIL("17","大额邮箱未认证"),

    CHECK_NO_AUTH_JD("18","大额京东未认证"),

    CHECK_NO_AUTH_ALIPAY("19","大额支付宝未认证"),

    CHECK_NO_AUTH_CREDIT_CARD("20","大额信用卡未认证"),

    CHECK_USER_AMOUNT_RANGE("21","您的借款额度为0元，暂不能借款。"),

    CHECK_USER_AVAILABLE_AMOUNT_RANGE("22","您的剩余可借额度为0元，暂不能借款。"),

    CHECK_USER_MIN_AMOUNT("23","低于您的最低借款额度"),

    CHECK_USER_MAX_AMOUNT("24","高于您的最高借款额度"),

    CHECK_USER_AVAILABLE_AMOUNT("25","高于您的剩余可借额度"),

    CHECK_PERIODS("26","期数不对");


    /**
     * 名称
     */
    private final String code;

    /**
     * 值
     */
    private final String value;

    LoanCodeMsgEnum(String code, String value) {
        this.code = code;
        this.value = value;
    }

    /**
     * 获取名称
     * @return
     */
    public String getCode() {
        return code;
    }

    /**
     * 获取值
     * @return
     */
    public String getValue() {

        return value;
    }

}