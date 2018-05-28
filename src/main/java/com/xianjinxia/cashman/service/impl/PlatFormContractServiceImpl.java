package com.xianjinxia.cashman.service.impl;

import com.lowagie.text.DocumentException;
import com.timevale.esign.sdk.tech.bean.result.FileDigestSignResult;
import com.xianjinxia.cashman.conf.ExtProperties;
import com.xianjinxia.cashman.constants.AgreementConstants;
import com.xianjinxia.cashman.domain.LoanContract;
import com.xianjinxia.cashman.domain.LoanOrder;
import com.xianjinxia.cashman.domain.RepaymentPlan;
import com.xianjinxia.cashman.domain.contract.PlatformAgreementContract;
import com.xianjinxia.cashman.dto.LoanOrderContractDto;
import com.xianjinxia.cashman.dto.UserNameInfoDto;
import com.xianjinxia.cashman.enums.*;
import com.xianjinxia.cashman.mapper.LoanContractMapper;
import com.xianjinxia.cashman.mapper.LoanOrderMapper;
import com.xianjinxia.cashman.remote.OldCashmanRemoteService;
import com.xianjinxia.cashman.remote.TradeAppRemoteService;
import com.xianjinxia.cashman.response.MerchantInfoResponse;
import com.xianjinxia.cashman.service.*;
import com.xianjinxia.cashman.service.repay.IRepaymentPlanService;
import com.xianjinxia.cashman.utils.MoneyUtil;
import com.xianjinxia.cashman.utils.StringUtil;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

/**
 * Created by liquan on 2017/12/1.
 */
@Service
public class PlatFormContractServiceImpl implements IPlatFormContractService{
    @Autowired
    private OldCashmanRemoteService oldCashmanRemoteService;
    @Autowired
    private IRepaymentService repaymentService;
    @Autowired
    private ExtProperties extProperties;
    @Autowired
    private TradeAppRemoteService tradeAppRemoteService;
    @Autowired
    private IPdfService pdfService;
    @Autowired
    private LoanContractMapper loanContractMapper;
    @Autowired
    private IRepaymentPlanService repaymentPlanService;
    @Autowired
    private LoanOrderMapper loanOrderMapper;
    @Autowired
    private IContractService contractService;
    @Autowired
    private IProductsService productsService;
    private static final Logger logger = LoggerFactory.getLogger(PlatFormContractServiceImpl.class);
    /**
     * 订单放款成功情况下合同数据
     * @param loanOrder
     * @param companyName--主体公司名称
     * @return
     */
    @Override
    public PlatformAgreementContract getLoanSuccessContractInfo(LoanOrderContractDto loanOrder) {
        PlatformAgreementContract contract = new PlatformAgreementContract();
        //根据产品ID获取产品对应的逾期费率（默认使用首期的）
        contract.setOverdueRate(new StringBuilder(MoneyUtil.multiply(productsService.getProductsDto(loanOrder.getProductId(), ProductsFeeConfigEnum.OVERDUE_FEE,1).getFeeRate().toString(),"100")).append("%").toString());
        contract=fillUserInfo(contract,loanOrder.getUserId());
        contract =fillLoanOrderInfo(contract,loanOrder);
        //获取还款计划
        List<RepaymentPlan> repaymentPlans = repaymentPlanService.getRepaymentOrderListByLoanOrderId(loanOrder.getId(),false);
        contract.setSource(loanOrder.getSource());
        contract.setMerchantNo(loanOrder.getMerchantNo());
        contract = fillLoanTimeForLoanSuccess(contract,loanOrder,repaymentPlans);
        return getFixedDataForLoanSuccess(contract,loanOrder);
    }

    /**
     * 填充用户基础信息--名称和身份证号（掩码）
     * @param contract
     * @param userId
     */
    public PlatformAgreementContract fillUserInfo(PlatformAgreementContract contract,Long userId){
        UserNameInfoDto userNameInfoDto = oldCashmanRemoteService.getBaseUserInfo(userId);
        contract.setRealName(userNameInfoDto.getUserRealName());
        contract.setMaskIdCardNum(userNameInfoDto.getIndCardNumber());
        return contract;
    }

    /**
     * 填充用户订单信息
     * @param contract
     * @param loanOrder
     */
    public PlatformAgreementContract fillLoanOrderInfo(PlatformAgreementContract contract,LoanOrderContractDto loanOrder){
        BigDecimal interestMoney = new BigDecimal(null==loanOrder.getInterestAmount()?0:loanOrder.getInterestAmount());
        //利息金额-- 利息
        contract.setInterest(new StringBuilder(MoneyUtil.changeBigCentToYuan(interestMoney).toString()).append(AgreementConstants.CHINESE_MONETARY_UNIT).toString());
        //金额赋值
        contract.setMoneyAmount(new StringBuilder(MoneyUtil.changeCentToYuan(loanOrder.getOrderAmount()).toString()).append(AgreementConstants.CHINESE_MONETARY_UNIT).toString());
        //应还金额--大额为本金加上利息
        contract.setRepaymentAmount(new StringBuilder(MoneyUtil.changeBigCentToYuan(new BigDecimal(loanOrder.getOrderAmount()).add(interestMoney)).toString()).append(AgreementConstants.CHINESE_MONETARY_UNIT).toString());

        //订单ID赋值
        contract.setLoanOrderId(loanOrder.getId().toString());
        //状态
        contract.setStatus(AgreementConstants.PASS_STATUS);
        return contract;
    }

    /**
     * 根据订单数据和还款计划填充数据
     * @param contract
     * @param loanOrder
     * @param repaymentPlans
     * @return
     */
    private PlatformAgreementContract fillLoanTimeForLoanSuccess(PlatformAgreementContract contract,LoanOrderContractDto loanOrder,List<RepaymentPlan> repaymentPlans){
        contract.setPeriod(loanOrder.getPeriods().toString());
        //放款成功时间
        contract.setLoanTime(new SimpleDateFormat("yyyy-MM-dd").format(loanOrder.getCreatedTime()));
        //到期还款时间
        if(CollectionUtils.isEmpty(repaymentPlans)){
            logger.error("还款列表为空");
        }else {
            if (repaymentPlans.size() == 1) {
                contract.setLoanEndTime(new SimpleDateFormat("yyyy-MM-dd").format(repaymentPlans.get(0).getRepaymentPlanTime()));
            } else {
                //每期到期日：用公式代替实际还款日
                contract.setLoanEndTime(new StringBuilder(AgreementConstants.LOAN_END_TIME_FIRST_PERIOD_STR).append(new SimpleDateFormat("yyyy-MM-dd").format(repaymentPlans.get(0).getRepaymentPlanTime())).append(
                        AgreementConstants.SYMBOL_COMMA).append(AgreementConstants.LOAN_END_TIME_END_PERIOD_STR).append(new SimpleDateFormat("yyyy-MM-dd").format(repaymentPlans.get(repaymentPlans.size() - 1).getRepaymentPlanTime())).append(
                        AgreementConstants.SYMBOL_FULL_STOP).append(AgreementConstants.LOAN_END_TIME_FORMULA).toString());
            }
        }
        return contract;
    }

    /**
     * 填充固定数据--成功放款后
     * @param contract
     * @param companyName--主体公司名称
     */
    private PlatformAgreementContract getFixedDataForLoanSuccess(PlatformAgreementContract contract,LoanOrderContractDto loanOrder){
        //商户号相关信息
        MerchantInfoResponse merchantInfoResponses=contractService.getMerchantInfo(loanOrder.getMerchantNo());
        logger.info("merchantInfo is {}",merchantInfoResponses.toString());
        contract.setAppName(merchantInfoResponses.getDesc());
        contract.setCompanyTitle(merchantInfoResponses.getCompanyName());
        return contract;
    }

        @Override
        @Transactional
        public Boolean upPlatformContract(LoanContract loanContract) throws IOException,DocumentException {
            Boolean upResult = false;
           //获取订单信息
            LoanOrder cashmanAppLoanOrder  = loanOrderMapper.selectByTrdOrderId(loanContract.getTrdLoanId());
            logger.info("loan order:{}", cashmanAppLoanOrder);
            LoanOrderContractDto loanOrder = null;
            if(cashmanAppLoanOrder.getProductCategory().intValue() == ProductCategoryEnum.PRODUCT_CATEGORY_SHOPPING.getCode().intValue()){
                loanOrder = tradeAppRemoteService.getShoppingLoanOrderByOrderId(loanContract.getTrdLoanId());
                loanOrder.setMerchantNo(cashmanAppLoanOrder.getMerchantNo());
            }else{
                loanOrder = tradeAppRemoteService.getLoanOrderByOrderId(loanContract.getTrdLoanId());
            }
            //商户号相关信息
            MerchantInfoResponse merchantInfoResponses=contractService.getMerchantInfo(loanOrder.getMerchantNo());
            String accountId=pdfService.creatAccountIndependent(merchantInfoResponses.getCompanyName(),merchantInfoResponses.getOrganCode());
            String templateStr="";
            //获取到合同流的字符串（对应合同模板）--快借钱包使用不同的模板
            if(loanOrder.getMerchantNo().equals(AgreementConstants.MERCHANT_NO_KJQB)){
                templateStr = StringUtil.getFileTemplateNew(new StringBuilder(AgreementConstants.PATH_BORDER).append(AgreementConstants.PDF_FOLDER_PATH).append(AgreementConstants.PATH_BORDER).append(AgreementConstants.TEMPLATE_FOLDER_KJQB).append(AgreementConstants.PATH_BORDER).append(AgreementConstants.KJQB_BORROW_PLATFORM_TEPLATE_PATH).toString());
            }else{
                templateStr = StringUtil.getFileTemplateNew(new StringBuilder(AgreementConstants.PATH_BORDER).append(AgreementConstants.PDF_FOLDER_PATH).append(AgreementConstants.PATH_BORDER).append(AgreementConstants.BORROW_PLATFORM_TEPLATE_PATH).toString());
            }
            //获取平台合同填充数据
            PlatformAgreementContract contract =getLoanSuccessContractInfo(loanOrder);
            //获取待填充的数据集合
            Map<String,String> fillUpMap = contract.getMapResult();
            //替换参数
            String newTemplateStr = StringUtil.fillData(templateStr,fillUpMap);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            try {
                out=pdfService.getPdfOutputStream(
                        new StringBuilder(StringUtil.getJarRootPath()).append(AgreementConstants.PATH_BORDER).append(AgreementConstants.FONT_SIM_SUN).toString(),
                       newTemplateStr);
                logger.info("平台合同pdf文件转换成功！");

                if(null==loanOrder.getSource()||"".equals(loanOrder.getSource())){
                    logger.error("来源不清楚，source为空");
                }
                FileDigestSignResult fileDigestSignResult = new FileDigestSignResult();
                if(loanOrder.getMerchantNo().equals(AgreementConstants.MERCHANT_NO_KJQB)){//快借钱包签章位置不一致
                    fileDigestSignResult= pdfService.signPdfByStreamIndependent(accountId,AgreementConstants.PLATFORM_SEALID,out,"",
                            AgreementConstants.PLATFORM_POSPAGE, AgreementConstants.PLATFORM_POSX,
                            "1".equals(contract.getPeriod())?AgreementConstants.KJQB_PLATFORM_POSY_SINGLE:AgreementConstants.KJQB_PLATFORM_POSY_MULTIPLE,
                            AgreementConstants.PLATFORM_WIDTH);
                }else{
                    fileDigestSignResult= pdfService.signPdfByStreamIndependent(accountId,AgreementConstants.PLATFORM_SEALID,out,"",
                            AgreementConstants.PLATFORM_POSPAGE, AgreementConstants.PLATFORM_POSX,
                            "1".equals(contract.getPeriod())?AgreementConstants.PLATFORM_POSY_SINGLE:AgreementConstants.PLATFORM_POSY_MULTIPLE,
                            AgreementConstants.PLATFORM_WIDTH);
                }

                logger.info("平台合同pdf文件调用E签宝签名成功！");
                String key = getFileName(loanContract.getTrdLoanId());
                String result = pdfService.uploadFileToOSS(key, new ByteArrayInputStream(fileDigestSignResult.getStream()) );
                logger.info("平台合同pdf文件上传OSS成功,路径为："+result);
                //更新表状态--合同生效--上传成功
                loanContractMapper.updateStatusAndcontractPathById(loanContract.getId(),LoanContractStatusEnum.UPLOAN_SUCCESS.getCode(), key);
                upResult = true;
                logger.info("更新合同状态！");
            }
            finally {
                out.flush();
                out.close();
            }
            return  upResult;
        }

    /**
     * 文件上 传路径+名称，类似于contract/bigloan/1018_PLATFORM_SERVICE_AGREEMENT.pdf
     * @param trdLoanOrderId
     * @return
     */
    public String getFileName(Long trdLoanOrderId){
            return new StringBuilder(extProperties.getContractRelatedConfig().getContractUploadFolder()).append(trdLoanOrderId).append(AgreementConstants.SYMBOL_UNDERLINE).append(ContractTypeEnum.PLATFORM_SERVICE_AGREEMENT.getType()).append(AgreementConstants.PDF_SUFFIXAL).toString();
        }
    @Override
    public String getPlatformContracctUrl(String contractType, Long trdLoanOrderId) throws IOException,DocumentException{
        //文件名
        String key =getFileName(trdLoanOrderId);
        LoanContract loanContract = loanContractMapper.selectByTrdNoAndType(trdLoanOrderId,contractType);
        if(null==loanContract){
            logger.error("没有查到该平台协议记录，请确认放款成功后是否有添加合同记录");
            return "";
        }
        if(null==loanContract.getStatus()||(!LoanContractStatusEnum.UPLOAN_SUCCESS.getCode().equals(loanContract.getStatus()))){
            //如果之前上传状态为失败则重新上传合同并生成URL
            upPlatformContract(loanContract);
        }
        return pdfService.getUploadFileUrl(key);
    }
}
