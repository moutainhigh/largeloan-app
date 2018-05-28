package com.xianjinxia.cashman.constants;

public class QueueConstants {

    /**
     * 支付中心发起代扣接口mq名称
     */
    public static final String PAYCENTER_MQNAME = "cashmanapp_paycenter_withhold_queue";

    /**
     * 修改loan order状态为已还清的MQ
     */
    public static final String LOAN_ORDER_REPAYMENTED_MQNAME = "trade_app_loan_order_repaymented_queue";

    /**
     * 逾期还款计划，催收通知
     */
    public static final String COLLECTION_REPAYMENT_PLAN_OVERDUE_NOTIFY = "cashmanapp_collect_repayment_plan_overdue_notify";

    /**
     * 逾期已还款，催收通知关闭订单
     */
    public static final String COLLECTION_REPAYMENT_PLAN_OVERDUE_REPAY = "cashmanapp_collect_repayment_plan_overdue_repay";

    /**
     * 恢复用户可用额度的接口（和trade-app公用的mq, 都会调用归还额度的MQ）
     */
    public static final String USER_AVAILABLE_AMOUNT_MQ_NAME = "trd_return_quota";

    /**
     * 支付中心推送存管放款订单mq名称
     */
    public static final String CUSTODY_LOAN_MQNAME = "cashmanapp_paycenter_custodyloan_queue";

    /**
     * trad-app存管放款订单状态回调mq
     */
    public static final String CUSTODY_LOAN_TRAND_MQNAME = "cashmanapp_trand_app_custodyloan_queue";

    /**
     * 风控结果回调后,cashman-app通知trade-app更新LoanOrder订单状态
     */
    public static final String CASHMANAPP_SYNC_TRD_ORDER_STAUTS_TO_TRADE = "cashmanapp_sync_orderstatus_to_trade_queue";


    /**
     * 风控结果回调后,cashman-app通知trade-app更新ShoppingLoanOrder订单状态
     */
    public static final String CASHMANAPP_SYNC_SHOPPING_ORDER_STAUTS_TO_TRADE = "cashmanapp_sync_shopping_orderstatus_to_trade_queue";


    /**
     * 风控结果回调后,cashman-app通知product-app更新ProductOrder订单状态
     */
    public static final String CASHMANAPP_SYNC_PDT_ORDER_STAUTS_TO_TRADE = "cashmanapp_sync_product_orderstatus_to_trade_queue";


    /**
     * 提速卡扣款
     */
    public static final String CASHMANAPP_SPEED_CARD_WITHHOLD = "cashmanapp_speed_card_withhold";

    /**
     * 使用提速卡的，通知cashmanapp，更新提速卡状态
     */
    public static final String CASHMANAPP_PAY_RESULT_QUEUE = "cashmanapp_pay_result_queue";

    /**
     * 催收代扣结果通知催收系统的队列名称
     */
    public static final String CUISHOU_WITHHOLD_QUEUE_BIG = "cuishou_withhold_queue_big";

    /**
     * 催收减免结果通知催收系统的队列名称
     */
    public static final String CUISHOU_DEDUCT_QUEUE_BIG = "cuishou_deduct_queue_big";

    /**
     * 统计还款数据对应sms队列名称
     */
    public static final String SMS_STATISTICS_QUEUE = "sms_queque";

    /**
     * 支付宝还款入账
     */
    public static final String ALIPAY_REPAYMENT_INCOME_QUEUE = "alipay-repayment-income";

    /**
     * 支付宝还款入账回调CMS
     */
    public static final String CMS_ALIPAY_REPAYMENT_CALLBACK_QUEUE = "cmsystem_alipay_backstatus";

    /**
     * 支付中心冲正
     */
    public static  final String PAYMENT_BEFORE_HAND_PAY_QUEUE ="before_hand_pay_queue";

}
