package com.xianjinxia.cashman.service.impl;

import com.lowagie.text.DocumentException;
import com.timevale.esign.sdk.tech.bean.result.FileDigestSignResult;
import com.xianjinxia.cashman.conf.ExtProperties;
import com.xianjinxia.cashman.constants.AgreementConstants;
import com.xianjinxia.cashman.domain.*;
import com.xianjinxia.cashman.domain.contract.LargeLoanAgreementContract;
import com.xianjinxia.cashman.dto.LoanOrderContractDto;
import com.xianjinxia.cashman.dto.UserNameInfoDto;
import com.xianjinxia.cashman.enums.ContractTypeEnum;
import com.xianjinxia.cashman.enums.LoanContractStatusEnum;
import com.xianjinxia.cashman.enums.ProductCategoryEnum;
import com.xianjinxia.cashman.mapper.LoanContractMapper;
import com.xianjinxia.cashman.mapper.LoanOrderMapper;
import com.xianjinxia.cashman.remote.OldCashmanRemoteService;
import com.xianjinxia.cashman.remote.TradeAppRemoteService;
import com.xianjinxia.cashman.response.MerchantInfoResponse;
import com.xianjinxia.cashman.service.IContractService;
import com.xianjinxia.cashman.service.ILoanContractService;
import com.xianjinxia.cashman.service.IPdfService;
import com.xianjinxia.cashman.service.repay.IRepaymentPlanService;
import com.xianjinxia.cashman.utils.MoneyUtil;
import com.xianjinxia.cashman.utils.StringUtil;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;


@Service
public class LoanContractServiceImpl implements ILoanContractService {
    @Autowired
    private OldCashmanRemoteService oldCashmanRemoteService;
    @Autowired
    private IPdfService pdfService;
    @Autowired
    private IRepaymentPlanService repaymentPlanService;
    @Autowired
    private TradeAppRemoteService tradeAppRemoteService;
    @Autowired
    private LoanContractMapper loanContractMapper;
    @Autowired
    private ExtProperties extProperties;
    @Autowired
    private IContractService contractService;

    @Autowired
    private LoanOrderMapper loanOrderMapper;

    private static final Logger logger = LoggerFactory.getLogger(LoanContractServiceImpl.class);

    @Override
   @Transactional
    public Boolean uploanLoanContract(LoanContract loanContract) throws IOException, DocumentException {
        Boolean upResult = false;
        //获取订单信息
        LoanOrder cashmanAppLoanOrder  = loanOrderMapper.selectByTrdOrderId(loanContract.getTrdLoanId());
        logger.info("loan order:{}", cashmanAppLoanOrder);
        LoanOrderContractDto loanOrder = null;
        if(cashmanAppLoanOrder.getProductCategory().intValue() == ProductCategoryEnum.PRODUCT_CATEGORY_SHOPPING.getCode().intValue()){
            loanOrder = tradeAppRemoteService.getShoppingLoanOrderByOrderId(loanContract.getTrdLoanId());
        }else{
            loanOrder = tradeAppRemoteService.getLoanOrderByOrderId(loanContract.getTrdLoanId());
        }
            //商户号相关信息
        MerchantInfoResponse merchantInfoResponses=contractService.getMerchantInfo(loanOrder.getMerchantNo());
        logger.info("merchanInfo is {}",merchantInfoResponses.toString());
        String accountId=pdfService.creatAccountIndependent(merchantInfoResponses.getCompanyName(),merchantInfoResponses.getOrganCode());

        //获取到合同流的字符串（对应合同模板）
        String templateStr = "";
        //获取到合同流的字符串（对应合同模板）--快借钱包使用不同的模板
        if(loanOrder.getMerchantNo().equals(AgreementConstants.MERCHANT_NO_KJQB)) {
            templateStr = StringUtil.getFileTemplateNew(new StringBuilder(AgreementConstants.PATH_BORDER).append(AgreementConstants.PDF_FOLDER_PATH).append(AgreementConstants.PATH_BORDER).append(AgreementConstants.TEMPLATE_FOLDER_KJQB).append(AgreementConstants.PATH_BORDER).append(AgreementConstants.KJQB_LOAN_PROTOCOL_TEPLATE_PATH).toString());
        }else{
            templateStr = StringUtil.getFileTemplateNew(new StringBuilder(AgreementConstants.PATH_BORDER).append(AgreementConstants.PDF_FOLDER_PATH).append(AgreementConstants.PATH_BORDER).append(AgreementConstants.LOAN_PROTOCOL_TEPLATE_PATH).toString());
        }
        LargeLoanAgreementContract contract= getLoanSuccessContractInfo(loanOrder);

        //获取待填充的数据集合
        Map<String,String> fillUpMap = contract.getMapResult();
        //替换参数
        String newTemplateStr= StringUtil.fillData(templateStr,fillUpMap);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            out=pdfService.getPdfOutputStream(
                    new StringBuilder(StringUtil.getJarRootPath()).append(AgreementConstants.PATH_BORDER).append(AgreementConstants.FONT_SIM_SUN).toString(),
                    newTemplateStr);
            logger.info("借款合同pdf文件转换成功！");

            if(null==loanOrder.getSource()||"".equals(loanOrder.getSource())){
                logger.error("来源不清楚，source为空");
            }
            FileDigestSignResult fileDigestSignResult = new FileDigestSignResult();
            if(loanOrder.getMerchantNo().equals(AgreementConstants.MERCHANT_NO_KJQB)) {//快借钱包签章位置不一致
                //借钱易和极速现金侠不再区分对应主体公司，全部使用普靖
                fileDigestSignResult = pdfService.signPdfByStreamIndependent(accountId, AgreementConstants.LOAN_CONTRACT_SEALID, out, "",
                            AgreementConstants.LOAN_CONTRACT_POSPAGE, AgreementConstants.LOAN_CONTRACT_POSX,
                            contract.getPeriod().equals("1") ? AgreementConstants.KJQB_LOAN_CONTRACT_POSY_SINGLE : AgreementConstants.KJQB_LOAN_CONTRACT_POSY_MULTIPLE,
                            AgreementConstants.LOAN_CONTRACT_WIDTH);
            }else{
                //借钱易和极速现金侠不再区分对应主体公司，全部使用普靖
                fileDigestSignResult = pdfService.signPdfByStreamIndependent(accountId, AgreementConstants.LOAN_CONTRACT_SEALID, out, "",
                        AgreementConstants.LOAN_CONTRACT_POSPAGE, AgreementConstants.LOAN_CONTRACT_POSX,
                        contract.getPeriod().equals("1") ? AgreementConstants.LOAN_CONTRACT_POSY_SINGLE : AgreementConstants.LOAN_CONTRACT_POSY_MULTIPLE,
                        AgreementConstants.LOAN_CONTRACT_WIDTH);
            }

            logger.info("借款合同pdf文件调用E签宝签名成功！");
            String key = getFileName(loanContract.getTrdLoanId());
            String result = pdfService.uploadFileToOSS(key,new ByteArrayInputStream(fileDigestSignResult.getStream()) );
            logger.info("借款合同pdf文件上传OSS成功,文件上传路径为：{}"+result);
            //更新表状态
            loanContractMapper.updateStatusAndcontractPathById(loanContract.getId(), LoanContractStatusEnum.UPLOAN_SUCCESS.getCode(),key);
            upResult = true;
            logger.info("更新合同状态！");
        } finally {
            out.flush();
            out.close();
        }
        return upResult;
    }


    @Override
    public LargeLoanAgreementContract getLoanSuccessContractInfo(LoanOrderContractDto loanOrder) {
        LargeLoanAgreementContract contract = new LargeLoanAgreementContract();
        //填充用户基本数据
        contract = fillUserInfo(contract, loanOrder.getUserId());
        //填充订单数据
        contract = fillLoanOrderInfo(contract, loanOrder);
        //获取还款计划
        List<RepaymentPlan> repaymentPlans = repaymentPlanService.getRepaymentOrderListByLoanOrderId(loanOrder.getId(),false);
        //填充放款时间和到期时间--已放款成功的订单需要填充数据
        contract = fillLoanTimeForLoanSuccess(contract, loanOrder, repaymentPlans);
        //填充固定数据
        contract = getFixedDataForLoanSuccess(contract, loanOrder);
        contract.setMerchantNo(loanOrder.getMerchantNo());
        contract.setSource(loanOrder.getSource());
        //填充合作方信息
        return fillcapitalInfo(contract,loanOrder.getId());
    }

    /**
     * 填充用户基础信息--名称和身份证号（掩码）
     *
     * @param contract
     * @param userId
     */
    public LargeLoanAgreementContract fillUserInfo(LargeLoanAgreementContract contract, Long userId) {
        UserNameInfoDto userNameInfoDto = oldCashmanRemoteService.getBaseUserInfo(userId);
        contract.setRealName(userNameInfoDto.getUserRealName());
        contract.setMaskIdCardNum(userNameInfoDto.getIndCardNumber());
        return contract;
    }

    /**
     * 填充用户订单信息
     *
     * @param contract
     * @param loanOrder
     */
    public LargeLoanAgreementContract fillLoanOrderInfo(LargeLoanAgreementContract contract, LoanOrderContractDto loanOrder) {

        BigDecimal interestMoney = new BigDecimal(null == loanOrder.getInterestAmount() ? 0 : loanOrder.getInterestAmount());
        //利息金额-- **元
        contract.setInterest(new StringBuilder(MoneyUtil.changeBigCentToYuan(interestMoney).toString()).append(AgreementConstants.CHINESE_MONETARY_UNIT ).toString());
        //金额赋值
        contract.setMoneyAmount(new StringBuilder(MoneyUtil.changeCentToYuan(loanOrder.getOrderAmount()).toString()).append(AgreementConstants.CHINESE_MONETARY_UNIT).toString());
        //中文大写
        contract.setCnMoneyAmount(MoneyUtil.change(loanOrder.getOrderAmount()));
        //订单ID赋值
        contract.setLoanOrderId(loanOrder.getId().toString());
        //期数
        contract.setPeriod(loanOrder.getPeriods().toString());
        //借款用途
        contract.setLoanPurpose(null==loanOrder.getLoanUsage()?AgreementConstants.LOAN_PURPOSE_PERSONAL:loanOrder.getLoanUsage());
        return contract;
    }

    /**
     * 根据订单数据和还款计划填充数据
     *
     * @param contract
     * @param loanOrder
     * @param repaymentPlans
     * @return
     */
    private LargeLoanAgreementContract fillLoanTimeForLoanSuccess(LargeLoanAgreementContract contract, LoanOrderContractDto loanOrder, List<RepaymentPlan> repaymentPlans) {

        //放款成功时间
        contract.setLoanTime(new SimpleDateFormat("yyyy-MM-dd").format(loanOrder.getCreatedTime()));
        if(CollectionUtils.isEmpty(repaymentPlans)){
            logger.error("还款列表为空");
        }else{
            //到期还款时间
            if (repaymentPlans.size() == 1) {
                //单期情况下还款方式为"一次还清"
                contract.setPaymentMethod(AgreementConstants.PAYMENT_METHOD_SMALLAMOUNT);
                contract.setLoanEndTime(new SimpleDateFormat("yyyy-MM-dd").format(repaymentPlans.get(0).getRepaymentPlanTime()));
            } else {
                //多期还款方式为"分期还款"
                contract.setPaymentMethod(AgreementConstants.PAYMENT_METHOD_BIGAMOUNT);
                //每期到期日：用公式代替实际还款日
                contract.setLoanEndTime(new StringBuilder(AgreementConstants.LOAN_END_TIME_FIRST_PERIOD_STR ).append( new SimpleDateFormat("yyyy-MM-dd").format(repaymentPlans.get(0).getRepaymentPlanTime())).append(
                        AgreementConstants.SYMBOL_COMMA ).append( AgreementConstants.LOAN_END_TIME_END_PERIOD_STR ).append( new SimpleDateFormat("yyyy-MM-dd").format(repaymentPlans.get(repaymentPlans.size() - 1).getRepaymentPlanTime()) ).append(
                        AgreementConstants.SYMBOL_FULL_STOP ).append( AgreementConstants.LOAN_END_TIME_FORMULA).toString());
            }
        }
        return contract;
    }

    /**
     * 填充固定数据--成功放款后
     *
     * @param contract
     */
    private LargeLoanAgreementContract getFixedDataForLoanSuccess(LargeLoanAgreementContract contract,LoanOrderContractDto loanOrder) {
        //商户号相关信息
        MerchantInfoResponse merchantInfoResponses=contractService.getMerchantInfo(loanOrder.getMerchantNo());
        contract.setCompanyTitle(merchantInfoResponses.getCompanyName());
        contract.setCompanyShortTitle(merchantInfoResponses.getCompanyShortName());
        contract.setCompanyCity(merchantInfoResponses.getCompanyCity());
        return contract;
    }

    /**
     * 填充出资方信息--填充固定数据
     *
     * @param trdLoanOrderId
     * @return
     */
    private LargeLoanAgreementContract fillcapitalInfo(LargeLoanAgreementContract contract, Long trdLoanOrderId) {
        //--看具体放款的卡号是哪个公司，由于合同不涉及到用户的卡号，随机生成（鱼耀还是招财猫。随机显示的。）
        Integer random=Math.random()>0.5?1:0;
        if (random == 0) {
            contract.setCapitalName(AgreementConstants.CAPITAL_NAME_SH);
            contract.setCapitalCity(AgreementConstants.CAPITAL_CITY_SH);

        } else {
            contract.setCapitalName(AgreementConstants.CAPITAL_NAME_HZ);
            contract.setCapitalCity(AgreementConstants.CAPITAL_CITY_HZ);
        }
        contract.setPartBRealName(AgreementConstants.PARTB_CAPITAL_PAYERS);
        return contract;
    }

    /**
     * 文件上 传路径+名称，类似于contract/bigloan/1018_LOAN_AGREEMENT.pdf
     * @param trdLoanOrderId
     * @return
     */
    public String getFileName(Long trdLoanOrderId){
        return new StringBuilder(extProperties.getContractRelatedConfig().getContractUploadFolder()).append(trdLoanOrderId).append(AgreementConstants.SYMBOL_UNDERLINE).append(ContractTypeEnum.LOAN_AGREEMENT.getType()).append(AgreementConstants.PDF_SUFFIXAL).toString();
    }

    @Override
    public String getLoanContracctUrl(String contractType, Long trdLoanOrderId) throws IOException,DocumentException{
        //文件名
        String key =getFileName(trdLoanOrderId);
        LoanContract loanContract = loanContractMapper.selectByTrdNoAndType(trdLoanOrderId,contractType);
        if(null==loanContract){
            logger.error("没有查到该协议记录，请确认放款成功后是否有添加合同记录");
            return "";
        }
        if(null==loanContract.getStatus()||(!LoanContractStatusEnum.UPLOAN_SUCCESS.getCode().equals(loanContract.getStatus()))){
            //如果之前上传状态为失败则重新上传合同并生成URL
            uploanLoanContract(loanContract);
        }
        return pdfService.getUploadFileUrl(key);
    }


}
