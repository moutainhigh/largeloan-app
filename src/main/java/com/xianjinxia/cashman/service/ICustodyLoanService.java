package com.xianjinxia.cashman.service;

import com.xianjinxia.cashman.domain.LoanOrder;
import com.xianjinxia.cashman.response.LoanByMqCallbackDto;

/**
 * @author whb
 * @date 2018/1/4.
 */
public interface ICustodyLoanService {


    void uploadCustodyLoan(LoanOrder order) throws Exception;

    void resolveCallback(LoanByMqCallbackDto payCenterCallbackDto);

//    void confirmReceived(ShoppingLoanOrderReceivedRequest );
}
