package com.xianjinxia.cashman.service;

import com.lowagie.text.DocumentException;
import com.xianjinxia.cashman.domain.LoanContract;
import com.xianjinxia.cashman.domain.contract.LargeLoanAgreementContract;
import com.xianjinxia.cashman.dto.LoanOrderContractDto;

import java.io.IOException;

/**
 * 大额借款协议
 */
public interface ILoanContractService {
    /**
     *生成借款协议PDF上传OSS
     * @param loanContract
     * @throws IOException
     * @throws DocumentException
     */
    Boolean uploanLoanContract(LoanContract loanContract) throws IOException,DocumentException;
    /**
     * 获取订单放款成功后合同数据
     * @param loanOrder
     * @return
     */
    LargeLoanAgreementContract getLoanSuccessContractInfo(LoanOrderContractDto loanOrder);

    /**
     * 根据订单号和类型查询合同URL
     * @param contractType
     * @param trdLoanOrderId
     * @return
     */
    String getLoanContracctUrl(String contractType,Long trdLoanOrderId)throws IOException,DocumentException;

}
