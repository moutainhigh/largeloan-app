package com.xianjinxia.cashman.constants;

import com.alibaba.fastjson.JSONObject;

public class Constant {

    public static final String SMS_MSG_SOURCE = "cashman-app";



    /**审核不通过解禁后 notification-43 9:30:00*/
    public static final String SMS_UNFREEZE_ID = "notification-43";
    /**notification-46	还款日前提醒 9:30 每期到期前2日（未逾期/未结清）*/
    public static final String SMS_EXPIRATION_REMINDER_ADVANCE_ID ="notification-46";
    /**notification-47	还款日提醒 10:30 每期到期日（未逾期/未结清）*/
    public static final String SMS_EXPIRATION_REMINDER_ID ="notification-47";
    /**notification-48	还款成功--部分期数（含多期）11:30:00*/
    public static final String SMS_PAYMENT_SUCCESS_PART_ID ="notification-48";
    /**notification-49	还款成功--全部期数（未提额）AM 10:00:00*/
    public static final String SMS_LOAN_SUCCESS_ALL_ID ="notification-49";
    /**notification-51	逾期首日 AM 9:30:00 逾期未还款-首日*/
    public static final String SMS_LATE_REMIND_ID ="notification-51";
    /** notification-44	放款结果通知--成功 即时*/
    public static final String SMS_LOAN_SUCCESS_ID ="notification-44";
    /** notification-45	放款结果通知--失败 即时*/
    public static final String SMS_LOAN_FAILED_ID ="notification-45";
    /** 提交申请成功后，短信模板ID 即时*/
    public static final String SMS_APPLY_SUCCESS_ID = "notification-40";
    /**风控审核结果,失败后，短信模板ID 即时*/
    public static final String SMS_RISK_FAILED_ID = "notification-41";

    public static final Integer ZERO=0;

    public static final String ONE_STR="1";


    public static final String APPLICATION_PAYMENT_SOURCE = "A5";    // 支付中心发起支付请求的Request Source
    public static final String	DB_INSERT_ERROR	= "数据库插入失败";
    public static final String	DB_UPDATE_ERROR	= "数据库更新失败";


    //存管
    public static final String BIZ_TYPE = "C2";
    public static final String SMALL_AMOUNT_BIZ_TYPE = "C4";
    public static final String ROUTE_STRATEGY = "custody";
    public static final String CUSTODYLOAN_SUCCESS = "200";
    public static final String CUSTODYLOAN_FAILED = "4000";
    public static final String CUSTODYLOAN_HANDLING = "1000";
    public static final String CUSTODYLOAN_HANDLING_MSG = "订单已推送";
    public static final Integer LOAN_METHOD_DAY = 0;
    public static final Integer LOAN_METHOD_MONTH = 1;
    public static final String NONE_REPAYMENT_PLAN = "未找到对应的还款计划";
    public static final String NONE_USER_INFO = "未找到对应的用户信息";

    public static final String YES = "y";

    public static final String NO = "n";

    public static final String PAYING = "paying";

    public static final String MERCHANT_NUMBER_CJXJX = "cjxjx";
    public static final String MERCHANT_NUMBER_KJQB = "kjqb";
    public static final JSONObject ALIPAY_MERCHANT_INFO_CJXJX=new JSONObject();
    public static final JSONObject ALIPAY_MERCHANT_INFO_KJQB=new JSONObject();
    public static final String MERCHANT_NUMBER_ALIPAY_TRANSFER_URL_KJQB="https://ds.alipay.com/?from=mobilecodec&scheme=alipays%3A%2F%2Fplatformapi%2Fstartapp%3FsaId%3D10000007%26clientVersion%3D3.7.0.0718%26qrcode%3Dhttps%253A%252F%252Fqr.alipay.com%252Faex06414bn9ody6lpepoq1c%253F_s%253Dweb-other";

    static {
        ALIPAY_MERCHANT_INFO_CJXJX.put("url","https://qr.alipay.com/aex09283m60ibp60ufid941");
        ALIPAY_MERCHANT_INFO_CJXJX.put("acount","qy@xianjinxia.com");
        ALIPAY_MERCHANT_INFO_CJXJX.put("name","安徽普靖金融服务外包有限公司");


        ALIPAY_MERCHANT_INFO_KJQB.put("url","https://qr.alipay.com/aex09978wqpqxhmyy8xh77d");
        ALIPAY_MERCHANT_INFO_KJQB.put("acount","ahpj@xianjinxia.com");
        ALIPAY_MERCHANT_INFO_KJQB.put("name","安徽普靖金融服务外包有限公司");
    }

    //产品要求变动文案--不使用原来的文案调整文案内容和发送时间
    /**notification-64	还款当天首次	08:30 （未逾期/未结清）*/
    public static final String SMS_EXPIRATION_REMINDER_ID_FIRST="notification-64";
    /**notification-64	还款当天第二次	13:30 （未逾期/未结清）*/
    public static final String SMS_EXPIRATION_REMINDER_ID_SECOND="notification-65";
    /**notification-64	还款当天第三次	20:30 （未逾期/未结清）*/
    public static final String SMS_EXPIRATION_REMINDER_ID_THIRD="notification-66";

    /**notification-67	还款前一天首次	08:30  每期到期日前一天（未逾期/未结清）*/
    public static final String SMS_DAY_BEFORE_DUE_REMINDER_ID_FIRST="notification-67";
    /**notification-68	还款前一天第二次13:30 每期到期日前一天（未逾期/未结清）*/
    public static final String SMS_DAY_BEFORE_DUE_REMINDER_ID_SECOND="notification-80";
    /**notification-68	还款前一天第二次20:30 每期到期日前一天（未逾期/未结清）*/
    public static final String SMS_DAY_BEFORE_DUE_REMINDER_ID_THIRD="notification-81";

    /**notification-69	还款日前两天	10:00  到期前两日 （未逾期/未结清）*/
    public static final String SMS_EXPIRATION_REMINDER_ADVANCE_ID_FIRST ="notification-69";
    /**notification-69	还款日前两天	10:00  到期前两日 （未逾期/未结清）*/
    public static final String SMS_EXPIRATION_REMINDER_ADVANCE_ID_SECOND ="notification-82";
    /**notification-69	还款日前两天	10:00  到期前两日 （未逾期/未结清）*/
    public static final String SMS_EXPIRATION_REMINDER_ADVANCE_ID_THIRD ="notification-83";

    public static final Integer SMS_NOTICE_8_AM =8;
    public static final Integer SMS_NOTICE_1_PM =13;
    public static final Integer SMS_NOTICE_8_PM =20;
    public static final Integer SMS_NOTICE_MINITE_PART=30;
}
