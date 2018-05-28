/****************************************
 * Copyright (c) 2017 XinJinXia.
 * All rights reserved.
 * Created on 2017年9月5日
 * 
 * Contributors:
 * tennyqin - initial implementation
 ****************************************/
package com.xianjinxia.cashman.service;

import com.lowagie.text.DocumentException;
import com.xianjinxia.cashman.domain.LoanContract;
import com.xianjinxia.cashman.domain.contract.DepositAgreementContract;
import com.xianjinxia.cashman.domain.contract.LargeLoanAgreementContract;
import com.xianjinxia.cashman.domain.contract.PlatformAgreementContract;
import com.xianjinxia.cashman.dto.ContractDto;
import com.xianjinxia.cashman.request.ContractReq;
import com.xianjinxia.cashman.request.ContractUrlReq;
import com.xianjinxia.cashman.response.MerchantInfoResponse;

import java.io.IOException;
import java.util.List;


/**
 * @title IContractService.java
 *
 * @author fanmaowen
 * @version 1.0
 * @created 2017年10月13日
 */
public interface IContractService {


	List<ContractDto> selectByProductId(Long productId);

	LargeLoanAgreementContract getBigAmountContractParams(ContractReq contractReq);

	PlatformAgreementContract getPlatformContractParams(ContractReq contractReq);

	void uploanLoanContract(LoanContract contract) throws IOException,DocumentException;

	void uploanPlatFormContract(LoanContract contract) throws IOException,DocumentException;

	String getLoanContractUrl(String contractType, Long trdLoanOrderId)throws IOException,DocumentException;

	String getPlatformContractUrl(String contractType, Long trdLoanOrderId)throws IOException,DocumentException;

	List<ContractDto> getContracts(Long trdLoanOrderId)throws IOException, DocumentException;

	List<ContractDto> getContractUrlForDetail(ContractUrlReq contractReq);

	DepositAgreementContract getDepositContractParams(Long userId);

	/**
	 * 获取商户号相关信息
	 * @param merchantNo
	 * @return
	 */
	MerchantInfoResponse getMerchantInfo(String merchantNo);

}
