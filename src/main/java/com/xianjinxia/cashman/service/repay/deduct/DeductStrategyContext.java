package com.xianjinxia.cashman.service.repay.deduct;

import com.xianjinxia.cashman.constants.ErrorCodeConstants;
import com.xianjinxia.cashman.exceptions.CashmanExceptionBuilder;
import com.xianjinxia.cashman.request.CollectionDeductReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;

@Service
public class DeductStrategyContext {


    @Autowired
    private List<DeductStrategy> deductStrategyList;

    public void deduct(CollectionDeductReq collectionDeductReq){
        for (Iterator<DeductStrategy> iterator = deductStrategyList.iterator(); iterator.hasNext(); ) {
            DeductStrategy deductStrategy = iterator.next();
            DeductResult deductResult = deductStrategy.deduct(collectionDeductReq);
            if (deductResult.isMatched()){
                return;
            }
        }
        throw CashmanExceptionBuilder.build(ErrorCodeConstants.DEDUCT_ERROR);
    }

}