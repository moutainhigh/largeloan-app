package com.xianjinxia.cashman.service;

import com.lowagie.text.DocumentException;
import com.xianjinxia.cashman.domain.LoanContract;
import com.xianjinxia.cashman.domain.contract.DepositAgreementContract;

import java.io.IOException;

/**
 * Created by liquan on 2018/1/10.
 */
public interface IDepositContractService {

    /**
     * 获取订单放款成功后合同数据
     * @param trdOrderId
     * @return
     */
    DepositAgreementContract getLoanSuccessContractInfo(Long trdOrderId);

    /**
     * 生成借款协议PDF上传OSS
     * @param loanContract
     * @throws IOException
     * @throws DocumentException
     */
    Boolean upDepositContract(LoanContract loanContract) throws IOException,DocumentException;
    /**
     * 根据订单号和类型查询合同URL
     * @param contractType
     * @param trdLoanOrderId
     * @return
     */
    String getDepositContracctUrl(String contractType,Long trdLoanOrderId)throws IOException,DocumentException;

}
