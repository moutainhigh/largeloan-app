package com.xianjinxia.cashman.service.repay.deduct;

import com.xianjinxia.cashman.request.CollectionDeductReq;

/**
 * 还款减免：
 *
 * 减免的前置条件：还款计划逾期
 *
 * 1、后置减免：催收进行沟通后，先还款，再减免
 * 2、前置减免：催收进行沟通后，先减免，再还款
 */
public interface DeductStrategy {
    DeductResult deduct(CollectionDeductReq collectionDeductReq);
}