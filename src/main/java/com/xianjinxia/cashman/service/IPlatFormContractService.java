package com.xianjinxia.cashman.service;

import com.lowagie.text.DocumentException;
import com.xianjinxia.cashman.domain.LoanContract;
import com.xianjinxia.cashman.domain.contract.PlatformAgreementContract;
import com.xianjinxia.cashman.dto.LoanOrderContractDto;

import java.io.IOException;

/**
 * Created by liquan on 2017/12/1.
 * 平台协议
 */
public interface IPlatFormContractService {

    /**
     * 获取订单放款成功后合同数据
     * @param loanOrder
     * @return
     */
    PlatformAgreementContract getLoanSuccessContractInfo(LoanOrderContractDto loanOrder);

    /**
     * 生成借款协议PDF上传OSS
     * @param loanContract
     * @throws IOException
     * @throws DocumentException
     */
    Boolean upPlatformContract(LoanContract loanContract) throws IOException,DocumentException;
    /**
     * 根据订单号和类型查询合同URL
     * @param contractType
     * @param trdLoanOrderId
     * @return
     */
    String getPlatformContracctUrl(String contractType,Long trdLoanOrderId)throws IOException,DocumentException;
}
