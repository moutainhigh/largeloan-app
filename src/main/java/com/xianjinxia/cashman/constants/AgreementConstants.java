package com.xianjinxia.cashman.constants;

/**
 * Created by liquan on 2017/11/24.
 */
public class AgreementConstants {

    /**
     * 订单生成后可见
     */
    public static final String SUCCESS_VISIBLE = "**订单生成后可见**";

    /**
     * 字段不可见
     */
    public static final String DATA_INVISIBLE ="**";

    /**
     * 公司不可见
     */
    public static final String COMPANY_INVISIBLE ="**公司";
    /**
     * 所在市不可见
     */
    public static final String CITY_INVISIBLE ="**市";

    /**
     * 掩码替换字符
     */
    public static final String STAR_INVISIBLE ="*";

    /**
     * 乙方公司
     */
    public static final String CAPITAL_NAME_SH = "上海鱼耀金融信息服务有限公司";
    /**
     * 乙方公司所在城市
     */
    public static final String CAPITAL_CITY_SH ="上海市";

    public static final String CAPITAL_NAME_HZ = "杭州招财猫网络科技有限公司";

    public static final String CAPITAL_CITY_HZ ="杭州市";

    /**
     * 丙方公司title（jsxjx）---存管协议中的乙方
     */
    public static final String COMPANY_TITLE_JSXJX = "上海利全信息科技服务有限公司";
    /**
     * 丙方公司简称/丁方(jsxjx)
     */
    public static final String COMPAY_SHORT_TITLE_JSXJX ="利全科技";

    public static final String COMPANY_TITLE_JQY = "上海达旷金融信息服务有限公司";

    public static final String COMPAY_SHORT_TITLE_JQY ="达旷金融";

    public static final String APP_NAME_JSXJX = "jsxjx";

    public static final String APP_NAME_JQY ="jqy";

    public static final String PAYMENT_METHOD_SMALLAMOUNT = "一次性还清";

    public static final String PAYMENT_METHOD_BIGAMOUNT = "分期还款";

    public static final String LOAN_PURPOSE_PERSONAL = "生活消费";

    public static final String PARTB_CAPITAL_PAYERS = "丁方的用户";

    //含利息及服务费--含利息
    public static final String DESC_INCLUDING_INTEREST = "含利息";
    //及服务费
    public static final String DESC_AND_SERVICE_CHARGE = "及服务费";
    //中文逗号 comma symbol
    public static final String SYMBOL_COMMA ="，";
    //中文句号 comma symbol
    public static final String SYMBOL_FULL_STOP ="。";
    //中文冒号 colon
    public static final String SYMBOL_COLON ="：";
    //下划线
    public static final String SYMBOL_UNDERLINE="_";
    //中文左括号 left bracket
    public static final String SYMBOL_LEFT_BRACKET ="（";
    //中文左括号 right parenthesis
    public static final String SYMBOL_RIGHT_PARENTHESIS ="）";
    //中国货币单位-- 人民币的单位 元 monetary unit
    public static final String CHINESE_MONETARY_UNIT ="元";

    //多期情况下：
    public static final String LOAN_END_TIME_FIRST_PERIOD_STR ="首期到期时间：";

    public static final String LOAN_END_TIME_END_PERIOD_STR ="末期到期时间：";

    //该笔订单第n期还款时间：T+n*10，T代表放款成功日，n代表借款期数，30代表30个自然日。

    public static final String LOAN_END_TIME_FORMULA ="该笔订单第n期还款时间：T+n*30，T代表放款成功日，n代表借款期数，30代表30个自然日。";
    //appName
    public static final String APP_NAME_XJX_CN= "极速现金侠";
    //appName
    public static final String APP_NAME_JQY_CN= "借钱易";

    public static final String PASS_STATUS="通过";
    public static final String FAIL_STATUS="失败";
    /**
     * pdf文件夹
     */
    public static final String PDF_FOLDER_PATH ="pdf";
    /**
     * 文件分界
     */
    public static final String PATH_BORDER = "/";

    /**
     * pdf后缀
     */
    public static final String PDF_SUFFIXAL = ".pdf";
    /**
     * 模板后缀
     */
    public static final String TEMPLATE_SUFFIXAL = ".html";
    /**
     * 大额平台协议模板路径
     */
    public static final String BORROW_PLATFORM_TEPLATE_PATH = "borrowPlatformServiceNew.html";
    /**
     * 借款协议模板路径
     */
    public static final String LOAN_PROTOCOL_TEPLATE_PATH = "loan-protocol-large.html";
    /**
     * 宋体--转换包
     */
    public static final String FONT_SIM_SUN="simsun.ttc";
    //平台-签章页数，偏移量x,偏移量y,宽度
    public static final Integer PLATFORM_SEALID=0;
    public static final String PLATFORM_POSPAGE="4";
    public static final Integer PLATFORM_POSX = 100;
    /**
     * 平台协议-借款期数为1-偏移量
     */
    public static final Integer PLATFORM_POSY_SINGLE = 400;
    /**
     * 平台协议-借款期数为多期-偏移量
     */
    public static final Integer PLATFORM_POSY_MULTIPLE = 390;
    public static final Integer PLATFORM_WIDTH = 100;
    //借款协议-签章页数，偏移量x,偏移量y,宽度
    public static final Integer LOAN_CONTRACT_SEALID=0;
    public static final String LOAN_CONTRACT_POSPAGE="6";
    public static final Integer LOAN_CONTRACT_POSX = 146;
    /**
     * 借款协议-借款期数为1-偏移量
     */
    public static final Integer LOAN_CONTRACT_POSY_SINGLE  =615;
    /**
     * 借款协议-借款期数为多期-偏移量
     */
    public static final Integer LOAN_CONTRACT_POSY_MULTIPLE =615;
    public static final Integer LOAN_CONTRACT_WIDTH = 100;
    //达旷签章时需要的代码号--91310000MA1K32MN7F(真实的达旷的统一社会信用代码) --C3359812B2XFKUHQPA（测试数据）
    public static final String DK_ORGAN_CODE="91310000MA1K32MN7F";
    //存管--丙方公司
    public static final String DEPOSIT_PARTC="江西银行股份有限公司";
    /**
     * 大额存管协议模板路径
     */
    public static final String DEPOSIT_CONTRACT_TEPLATE_PATH = "custody-protocol-large.html";

    /**
     * 丙方公司title（jsxjx）---存管协议中的乙方--公司主体更名为：安徽普靖金融服务外包有限公司
     */
    public static final String COMPANY_TITLE_JSXJX_NEW = "安徽普靖金融服务外包有限公司";
    /**
     * 丙方公司简称/丁方(jsxjx)
     */
    public static final String COMPAY_SHORT_TITLE_JSXJX_NEW ="普靖金融";

    /**
     * 安徽普靖金融服务外包有限公司--91340100MA2RC1DT3F(统一社会信用代码)
     */
    public static final String PJ_ORGAN_CODE="91340100MA2RC1DT3F";

    public static final String PJ_COMPANY_CITY="合肥市";

    //快借钱包相关参数
    public static final String MERCHANT_NO_KJQB="kjqb";
    //快借钱包模板存放路径多了个kjqb标识
    public static final String TEMPLATE_FOLDER_KJQB="kjqb";
     //快借钱包-平台协议模板路径
    public static final String KJQB_BORROW_PLATFORM_TEPLATE_PATH = "kjqb-platform-protocol.html";
    //快借钱包-借款协议模板路径
    public static final String KJQB_LOAN_PROTOCOL_TEPLATE_PATH = "kjqb-loan-protocol.html";

    //平台协议-借款期数为1-偏移量(其他参数一致)
    public static final Integer KJQB_PLATFORM_POSY_SINGLE = 406;
    //平台协议-借款期数为多期-偏移量
    public static final Integer KJQB_PLATFORM_POSY_MULTIPLE = 390;

    //借款协议-借款期数为1-偏移量(其他参数一致)
    public static final Integer KJQB_LOAN_CONTRACT_POSY_SINGLE  =275;
    //借款协议-借款期数为多期-偏移量
    public static final Integer KJQB_LOAN_CONTRACT_POSY_MULTIPLE =255;

}
