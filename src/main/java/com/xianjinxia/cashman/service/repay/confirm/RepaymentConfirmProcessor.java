package com.xianjinxia.cashman.service.repay.confirm;

/**
 * 还款的回调接口
 * <pre>
 *     1. 还款：支持多笔批量处理
 *     2. 定时代扣：只支持单笔处理
 * </pre>
 *
 * @author zhangyongjia zyj@xianjinxia.com
 */
public interface RepaymentConfirmProcessor {

    RepaymentConfirmProcessResult process(RepaymentConfirmProcessParam repaymentConfirmProcessParam);

    /** 支付中心成功回调函数 */
    default void paymentCenterCallbackSuccAction(RepaymentConfirmProcessParam repaymentConfirmProcessParam) { }

    /** 支付中心失败回调函数 */
    default void paymentCenterCallbackFailAction(RepaymentConfirmProcessParam repaymentConfirmProcessParam) { }
}
