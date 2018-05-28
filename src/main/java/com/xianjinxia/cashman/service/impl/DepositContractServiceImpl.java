package com.xianjinxia.cashman.service.impl;

import com.lowagie.text.DocumentException;
import com.xianjinxia.cashman.conf.ExtProperties;
import com.xianjinxia.cashman.constants.AgreementConstants;
import com.xianjinxia.cashman.domain.LoanContract;
import com.xianjinxia.cashman.domain.contract.DepositAgreementContract;
import com.xianjinxia.cashman.dto.LoanOrderContractDto;
import com.xianjinxia.cashman.dto.UserNameInfoDto;
import com.xianjinxia.cashman.enums.ContractTypeEnum;
import com.xianjinxia.cashman.enums.LoanContractStatusEnum;
import com.xianjinxia.cashman.mapper.LoanContractMapper;
import com.xianjinxia.cashman.remote.OldCashmanRemoteService;
import com.xianjinxia.cashman.remote.TradeAppRemoteService;
import com.xianjinxia.cashman.response.MerchantInfoResponse;
import com.xianjinxia.cashman.service.IContractService;
import com.xianjinxia.cashman.service.IDepositContractService;
import com.xianjinxia.cashman.service.IPdfService;
import com.xianjinxia.cashman.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

/**
 * Created by liquan on 2018/1/10.
 */
@Service
public class DepositContractServiceImpl implements IDepositContractService{
    @Autowired
    private OldCashmanRemoteService oldCashmanRemoteService;
    @Autowired
    private TradeAppRemoteService tradeAppRemoteService;
    @Autowired
    private IContractService contractService;
    private static final Logger logger = LoggerFactory.getLogger(DepositContractServiceImpl.class);
    @Autowired
    private IPdfService pdfService;
    @Autowired
    private ExtProperties extProperties;
    @Autowired
    private LoanContractMapper loanContractMapper;
    @Override
    public DepositAgreementContract getLoanSuccessContractInfo(Long trdOrderId) {
        //获取订单信息
        LoanOrderContractDto loanOrder =tradeAppRemoteService.getLoanOrderByOrderId(trdOrderId);
        //商户号相关信息
        MerchantInfoResponse merchantInfoResponses=contractService.getMerchantInfo(loanOrder.getMerchantNo());
        logger.info("merchant is {}"+merchantInfoResponses.toString());
        //获取用户名称-realName
        DepositAgreementContract depositAgreementContract = new DepositAgreementContract(merchantInfoResponses.getCompanyName(),AgreementConstants.DEPOSIT_PARTC);
        UserNameInfoDto userNameInfoDto = oldCashmanRemoteService.getBaseUserInfo(loanOrder.getUserId());
        depositAgreementContract.setRealName(userNameInfoDto.getUserRealName());
        depositAgreementContract.setSource(loanOrder.getSource());
        depositAgreementContract.setMerchantNo(loanOrder.getMerchantNo());
        return depositAgreementContract;
    }

    @Override
    @Transactional
    public Boolean upDepositContract(LoanContract loanContract) throws IOException, DocumentException {
        Boolean upResult = false;
        //获取到合同流的字符串（对应合同模板）
       String templateStr = StringUtil.getFileTemplateNew(new StringBuilder(AgreementConstants.PATH_BORDER).append(AgreementConstants.PDF_FOLDER_PATH).append(AgreementConstants.PATH_BORDER).append(AgreementConstants.DEPOSIT_CONTRACT_TEPLATE_PATH).toString());
        //获取平台合同填充数据
        DepositAgreementContract contract =getLoanSuccessContractInfo(loanContract.getTrdLoanId());
        //获取待填充的数据集合
        Map<String,String> fillUpMap = contract.getMapResult();
        //替换参数
        String newTemplateStr= StringUtil.fillData(templateStr,fillUpMap);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            out=pdfService.getPdfOutputStream(
                    new StringBuilder(StringUtil.getJarRootPath()).append(AgreementConstants.PATH_BORDER).append(AgreementConstants.FONT_SIM_SUN).toString(),
                    newTemplateStr);
            logger.info("存管协议pdf文件转换成功！");

            String key = getFileName(loanContract.getTrdLoanId());
            String result = pdfService.uploadFileToOSS(key, new ByteArrayInputStream(out.toByteArray()) );
            logger.info("存管协议pdf文件上传OSS成功,路径为："+result);
            //更新表状态--合同生效--上传成功
            loanContractMapper.updateStatusAndcontractPathById(loanContract.getId(), LoanContractStatusEnum.UPLOAN_SUCCESS.getCode(), key);
            upResult = true;
            logger.info("更新合同状态！");
        }
        finally {
            out.flush();
            out.close();
        }
        return  upResult;
    }

    //这个方法预留
    @Override
    public String getDepositContracctUrl(String contractType, Long trdLoanOrderId) throws IOException, DocumentException {
        //文件名
        String key =getFileName(trdLoanOrderId);
        LoanContract loanContract = loanContractMapper.selectByTrdNoAndType(trdLoanOrderId,contractType);
        if(null==loanContract){
            logger.error("没有查到该协议记录，请确认放款成功后是否有添加合同记录");
            return "";
        }
        if(null==loanContract.getStatus()||(!LoanContractStatusEnum.UPLOAN_SUCCESS.getCode().equals(loanContract.getStatus()))){
            //如果之前上传状态为失败则重新上传合同并生成URL
            upDepositContract(loanContract);
        }
        return pdfService.getUploadFileUrl(key);
    }

    /**
     * 文件上 传路径+名称，类似于contract/bigloan/1018_PLATFORM_SERVICE_AGREEMENT.pdf
     * @param trdLoanOrderId
     * @return
     */
    public String getFileName(Long trdLoanOrderId){
        return new StringBuilder(extProperties.getContractRelatedConfig().getContractUploadFolder()).append(trdLoanOrderId).append(AgreementConstants.SYMBOL_UNDERLINE).append(ContractTypeEnum.DEPOSITORY_AGREEMENT.getType()).append(AgreementConstants.PDF_SUFFIXAL).toString();
    }
}
