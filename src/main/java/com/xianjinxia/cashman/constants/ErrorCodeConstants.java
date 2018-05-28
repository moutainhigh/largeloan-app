package com.xianjinxia.cashman.constants;

public class ErrorCodeConstants {

    //=============  Dao ================
    public static final String DB_PARAM_ERROR = "DAO_10000";
    public static final String DB_INSERT_ERROR = "DAO_10001";
    public static final String DB_UPDATE_ERROR = "DAO_10002";
    public static final String DB_DELETE_ERROR = "DAO_10003";

    //============= 还款订单验证 ================
    public static final String REPAY_VERIFY_ORDER_NOT_EXIST = "REPAY_VERIFY_10001";//校验订单失败，订单不存在
    public static final String REPAY_VERIFY_AMOUNT_ERROR = "REPAY_VERIFY_10002";//校验订单失败，金额有误
    public static final String REPAY_VERIFY_USER_NOT_MATCH = "REPAY_VERIFY_10003";//校验订单失败，用户不一致
    public static final String REPAY_VERIFY_ORDER_TYPE_ERROR = "REPAY_VERIFY_10004";//校验订单失败，订单业务类型
    public static final String REPAY_VERIFY_ORDER_SOURCE_ERROR = "REPAY_VERIFY_10005";//校验订单失败，订单请求不是来源于交易系统


    //============= 还款计划更新 ================
    public static final String REPAY_PLAN_INCREASE_WAITING_AMT_ERR = "REPAY_PLAN_10000";//更新还款计划失败：增加在途金额,减去待还款金额
    public static final String REPAY_PLAN_SUBSTRACT_WAITING_AMT_ERR = "REPAY_PLAN_10001";//更新还款计划失败：减去在途金额,增加待还款金额
    public static final String REPAY_PLAN_INCREASE_INCOMING_AMT_ERR = "REPAY_PLAN_10002";//更新还款计划失败：增加已还款金额,减去在途金额,更新订单状态
    public static final String REPAY_PLAN_DEDUCT_ERR = "REPAY_PLAN_10003";//更新还款计划失败：减免待还款金额,更新订单状态
    public static final String REPAY_PLAN_NOT_EXIST = "REPAY_PLAN_10004";//更新还款计划失败：还款订单查询数据为空
    public static final String REPAY_PLAN_UPDATE_TO_OVERDUE_ERR = "REPAY_PLAN_10005";//更新还款计划失败：修改订单为逾期状态失败


    //============= 定时代扣================
    public static final String REPAY_WITHHOLD_PREPARE_DATA_ERR = "REPAY_WITHHOLD_10000";//准备代扣数据发生异常
    public static final String REPAY_WITHHOLD_UPDATE_STATUS_ERR = "REPAY_WITHHOLD_10001";//修改schedule_task_withhold中的记录状态失败

    //============== 还款请求提交的检查 ===============
    public static final String REPAY_COMMIT_CHECK_NOT_CONTINOUS = "REPAY_COMMIT_CHECK_10000";//多笔还款订单非连续期数的还款订单
    public static final String REPAY_COMMIT_CHECK_PREVIOUS_NOT_REPAYMENTED  = "REPAY_COMMIT_CHECK_10001";//您之前的还款订单还有未结清的订单
    public static final String REPAY_COMMIT_CHECK_AMT_ERR = "REPAY_COMMIT_CHECK_10002";//还款金额有误，请刷新后重试
    public static final String REPAY_COMMIT_CHECK_PREVIOUS_NOT_EXIST = "REPAY_COMMIT_CHECK_10003";//上一期的还款计划未找到，数据异常

    //=============  催收减免 ================
    public static final String DEDUCT_NOT_SUPPORT_REPAYMENT_PLAN_STATUS = "DEDUCT_10000";//当前还款计划未逾期，不支持催收减免
    public static final String DEDUCT_NOT_SUPPORT_REPAYMENT_PLAN_WAITING_AMT = "DEDUCT_10001";//还款计划当前有未完成入账的支付请求，不允许进行催收减免
    public static final String DEDUCT_AMOUNT_ERROR = "DEDUCT_10002";//催收减免金额有误
    public static final String DEDUCT_AMOUNT_PRINCIPAL_INTEREST_ERROR = "DEDUCT_10003";//催收减免前置条件本金和利息没有还清,不能催收减免
    public static final String DEDUCT_AMOUNT_OVERDUEFEE_ERROR = "DEDUCT_10004";//催收减免前置条件待还款总金额不等于待减免金额或者待还款总金额不等于滞纳金,不能催收减免
    public static final String DEDUCT_REPAYMENT_PLAN_STATUS_HOLDED = "DEDUCT_10005";//催收代还款单不存在或对应的还款单的标识不是逾期
    public static final String DEDUCT_ERROR = "DEDUCT_10006";//减免失败
    public static final String DEDUCT_NOT_SUPPORT = "DEDUCT_10007";//目前不支持先减免后还款

    //=============  催收代扣 ================
    public static final String COLLECT_WITHHOLD_NOT_SUPPORT_REPAYMENT_PLAN_STATUS = "COLLECT_WITHHOLD_10000";//当前还款计划未逾期，不支持催收代扣
    public static final String COLLECT_WITHHOLD_NOT_SUPPORT_REPAYMENT_PLAN_WAITING_AMT = "COLLECT_WITHHOLD_10001";//还款计划当前有未完成入账的支付请求，不允许进行催收代扣
    public static final String COLLECT_WITHHOLD_AMOUNT_ERROR = "COLLECT_WITHHOLD_10002";//催收代扣金额有误
    public static final String COLLECT_WITHHOLD_REPAYMENT_PLAN_STATUS_HOLDED = "COLLECT_WITHHOLD_10003";//催收代还款单不存在或对应的借款单被hold住,通常有定时器在执行,避免重复扣款
    public static final String COLLECT_WITHHOLD_REPAYMENT_PLAN_TOTAL_AMOUNT_ERROR = "COLLECT_WITHHOLD_10004"; //催收代扣对应的借款单的待还款金额为0,不需要执行代扣
}