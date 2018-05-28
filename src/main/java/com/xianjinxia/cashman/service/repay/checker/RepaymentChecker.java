package com.xianjinxia.cashman.service.repay.checker;

/**
 * 还款订单的检查，分为2类情况：
 * <pre>
 *     1. 单个还款订单
 *          1.1 是否此还款订单是多笔订单中的某中间一个，即跳过了前面的还款订单进行还款的操作
 *          1.2 此还款订单是否是全额还款
 *     2. 多个还款订单
 *          2.1 是否连续，检查period字段的连续性
 *          2.2 多笔还款订单的最小期数订单(minPeriod)的前序订单(minPeriod -1)是否已经还清，即按照单笔的检查是否跳过
 *          2.3 是否每笔订单都是全额还款
 * </pre>
 */
public interface RepaymentChecker {

    void check(RepaymentCheckParam repaymentCheckParam);

}
