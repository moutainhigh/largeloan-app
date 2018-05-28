/****************************************
 * Copyright (c) 2017 XinJinXia.
 * All rights reserved.
 * Created on 2017年9月5日
 * 
 * Contributors:
 * tennyqin - initial implementation
 ****************************************/
package com.xianjinxia.cashman.service.impl;

import com.lowagie.text.DocumentException;
import com.xianjinxia.cashman.domain.LoanContract;
import com.xianjinxia.cashman.domain.LoanOrder;
import com.xianjinxia.cashman.domain.contract.DepositAgreementContract;
import com.xianjinxia.cashman.domain.contract.LargeLoanAgreementContract;
import com.xianjinxia.cashman.domain.contract.PlatformAgreementContract;
import com.xianjinxia.cashman.dto.ContractDto;
import com.xianjinxia.cashman.dto.LoanOrderContractDto;
import com.xianjinxia.cashman.enums.ContractTypeEnum;
import com.xianjinxia.cashman.enums.ProductCategoryEnum;
import com.xianjinxia.cashman.mapper.ContractMapper;
import com.xianjinxia.cashman.mapper.LoanOrderMapper;
import com.xianjinxia.cashman.remote.OldCashmanRemoteService;
import com.xianjinxia.cashman.remote.TradeAppRemoteService;
import com.xianjinxia.cashman.request.ContractReq;
import com.xianjinxia.cashman.request.ContractUrlReq;
import com.xianjinxia.cashman.response.MerchantInfoResponse;
import com.xianjinxia.cashman.service.IContractService;
import com.xianjinxia.cashman.service.IDepositContractService;
import com.xianjinxia.cashman.service.ILoanContractService;
import com.xianjinxia.cashman.service.IPlatFormContractService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @title ContractServiceImpl.java
 *
 * @author fanmaowen
 * @version 1.0
 * @created 2017年10月13日
 */
@Service
public class ContractServiceImpl implements IContractService {

	@Autowired
	private ContractMapper contractMapper;

	@Autowired
	private LoanOrderMapper loanOrderMapper;
	@Autowired
	private TradeAppRemoteService tradeAppRemoteService;
	@Autowired
	private IPlatFormContractService platFormContractService;
	@Autowired
	private ILoanContractService loanContractService;
	@Autowired
	private IDepositContractService depositContractService;
	@Autowired
	private OldCashmanRemoteService oldCashmanRemoteService;
	@Override
	public List<ContractDto> selectByProductId(Long productId) {
		return contractMapper.selectByProductId(productId);
	}

	@Override
	public LargeLoanAgreementContract getBigAmountContractParams(ContractReq contractReq) {
		//0:审核中 ; -3:审核失败; 22:放款中 ;-10:放款失败;21:放款成功; 11:已还清;',
		//--放款成功后的状态才会带订单数据，--放款成功前的所有状态都是**
		//获取订单信息
		LoanOrderContractDto loanOrderContractDto = this.getLoanOrderContractDto(contractReq);
		//获取含有订单详细信息的数据
		return loanContractService.getLoanSuccessContractInfo(loanOrderContractDto);
	}


	@Override
	public PlatformAgreementContract getPlatformContractParams(ContractReq contractReq) {
		LoanOrderContractDto loanOrderContractDto = this.getLoanOrderContractDto(contractReq);
		//获取含有订单详细信息的数据
		return platFormContractService.getLoanSuccessContractInfo(loanOrderContractDto);
	}


	private LoanOrderContractDto getLoanOrderContractDto(ContractReq contractReq){

		LoanOrder loanOrder = loanOrderMapper.selectByTrdOrderId(contractReq.getTrdLoanOrderId());
		LoanOrderContractDto loanOrderDto = null;
		if (loanOrder.getProductCategory().intValue() == ProductCategoryEnum.PRODUCT_CATEGORY_SHOPPING.getCode().intValue()) {
			loanOrderDto = tradeAppRemoteService.getShoppingLoanOrderByOrderId(contractReq.getTrdLoanOrderId());
			loanOrderDto.setMerchantNo(loanOrder.getMerchantNo());
		}else{
            loanOrderDto = tradeAppRemoteService.getLoanOrderByOrderId(contractReq.getTrdLoanOrderId());
        }
		return loanOrderDto;
	}

	@Override
    @Transactional
	public void uploanLoanContract(LoanContract loanContract) throws IOException,DocumentException {
		loanContractService.uploanLoanContract(loanContract);
	}

	@Override
    @Transactional
	public void uploanPlatFormContract(LoanContract contractReq) throws IOException,DocumentException {
		platFormContractService.upPlatformContract(contractReq);

	}

	@Override
	public String getLoanContractUrl(String contractType, Long trdLoanOrderId) throws IOException, DocumentException {
		return loanContractService.getLoanContracctUrl(contractType,trdLoanOrderId);
	}

	@Override
	public String getPlatformContractUrl(String contractType, Long trdLoanOrderId) throws IOException, DocumentException {
		return platFormContractService.getPlatformContracctUrl(contractType,trdLoanOrderId);
	}

	@Override
	public List<ContractDto> getContracts(Long trdLoanOrderId) throws IOException, DocumentException {
		List<ContractDto> contractDtos = new ArrayList<>();
		contractDtos.add(new ContractDto(ContractTypeEnum.LOAN_AGREEMENT.getName(),loanContractService.getLoanContracctUrl(ContractTypeEnum.LOAN_AGREEMENT.getType(),trdLoanOrderId),ContractTypeEnum.LOAN_AGREEMENT.getType()));
		contractDtos.add(new ContractDto(ContractTypeEnum.PLATFORM_SERVICE_AGREEMENT.getName(),platFormContractService.getPlatformContracctUrl(ContractTypeEnum.PLATFORM_SERVICE_AGREEMENT.getType(),trdLoanOrderId),ContractTypeEnum.PLATFORM_SERVICE_AGREEMENT.getType()));
		return contractDtos;
	}

	@Override
	public List<ContractDto> getContractUrlForDetail(ContractUrlReq contractReq){
		//从合同表中查出来合同url和type
		List<ContractDto> contractDtos = contractMapper.selectByProductId(contractReq.getProductId());
		//拼接字符串 加上？loanId=""&userId =""
		if(CollectionUtils.isNotEmpty(contractDtos)){
			for(ContractDto contractDto:contractDtos){
				if(ContractTypeEnum.DEPOSITORY_AGREEMENT.getType().equals(contractDto.getContractType())){
					contractDto.setContractUrl(new StringBuilder(contractDto.getContractUrl()).append("?loanId=").append(contractReq.getTrdLoanOrderId()).append("&merchantNumber=").append(contractReq.getMerchantNo()).toString());
				}else {
					contractDto.setContractUrl(new StringBuilder(contractDto.getContractUrl()).append("?loanId=").append(contractReq.getTrdLoanOrderId()).append("&userId=").append(contractReq.getUserId()).append("&merchantNumber=").append(contractReq.getMerchantNo()).toString());
				}
			}
		}
		return contractDtos;
	}

	@Override
	public DepositAgreementContract getDepositContractParams(Long trdOrderId) {
		return depositContractService.getLoanSuccessContractInfo(trdOrderId);
	}

	@Override
	public MerchantInfoResponse getMerchantInfo(String merchantNo){
		List<MerchantInfoResponse> merchantInfoResponseList = oldCashmanRemoteService.getMerchantinfo(merchantNo);
		MerchantInfoResponse merchantInfo=null;
		if(CollectionUtils.isNotEmpty(merchantInfoResponseList)){
			merchantInfo=merchantInfoResponseList.get(0);
		}
		return merchantInfo;
	}
}
