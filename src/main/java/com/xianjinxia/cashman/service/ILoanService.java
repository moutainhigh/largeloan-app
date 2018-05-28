/****************************************
 * Copyright (c) 2017 XinJinXia. All rights reserved. Created on 2017年9月5日
 *
 * Contributors: tennyqin - initial implementation
 ****************************************/
package com.xianjinxia.cashman.service;

import com.github.pagehelper.PageInfo;
import com.xianjinxia.cashman.domain.Products;
import com.xianjinxia.cashman.dto.LoanConfigModalDto;
import com.xianjinxia.cashman.dto.ServiceChargeFee;
import com.xianjinxia.cashman.request.LoanCalculateRequest;
import com.xianjinxia.cashman.request.LoanCheckRequest;
import com.xianjinxia.cashman.request.NoticeOrdersReq;
import com.xianjinxia.cashman.response.*;

import java.util.List;


/**
 * @author fanmaowen
 * @version 1.0
 * @title ILoanService.java
 * @created 2017年9月6日
 */
public interface ILoanService {

    BaseResponse<LoanCheckResponse> loanBizCheck(LoanCheckRequest params);

    List<ServiceChargeFee> getServiceChargeFees(Double loanMoney, Long productId);

    /**
     * 借款选择配置(借款费用说明预查看)
     *
     * @param loanConfigModalDto
     *
     * @return BaseResponse<LoanConfigData>
     */
    LoanFeeDetails loanDynamicFeeCalculate(LoanConfigModalDto loanConfigModalDto);

    /**
     * 借款选择配置(借款费用说明预查看)--新增
     *
     * @param loanConfigModalDto
     *
     * @return
     */
    LoanFeeDetails loanDynamicFeeCal(LoanConfigModalDto loanConfigModalDto);

    List<LoanFeeDetails> loanDynamicFeeBatchCal(LoanCalculateRequest loanCalculateRequest);

    /**
     * 预计算还款计划
     *
     * @param loanConfigModalDto
     *
     * @return
     */
//    List<RepaymentPlanAdvance> loanDynamicRepaymentPlanCalculate(LoanConfigModalDto loanConfigModalDto);

    /**
     * 预计算还款计划--新增
     *
     * @param loanConfigModalDto
     *
     * @return
     */
    List<RepaymentPlanAdvance> loanDynamicRepaymentPlanCal(LoanConfigModalDto loanConfigModalDto);


    LoanFeeDetails calculateFee(Double orderAmount, int periods, Products products);

    Boolean hasUnFinallyLoanOrder(Long userId);

    PageInfo<UnfreezeOrdersResponse> getUnfreezeOrderList(NoticeOrdersReq unfreezeOrdersReq);
}
