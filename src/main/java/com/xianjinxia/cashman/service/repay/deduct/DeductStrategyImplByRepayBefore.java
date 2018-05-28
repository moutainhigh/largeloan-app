package com.xianjinxia.cashman.service.repay.deduct;

import com.xianjinxia.cashman.request.CollectionDeductReq;

//@Service("DeductStrategyImplByRepayBefore")
public class DeductStrategyImplByRepayBefore implements DeductStrategy {

    @Override
    public DeductResult deduct(CollectionDeductReq collectionDeductReq) {
        //throw CashmanExceptionBuilder.build(ErrorCodeConstants.DEDUCT_NOT_SUPPORT);
        return new DeductResult(false);
    }

}