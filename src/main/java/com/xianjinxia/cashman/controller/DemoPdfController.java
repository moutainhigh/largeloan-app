package com.xianjinxia.cashman.controller;

import com.lowagie.text.DocumentException;
import com.xianjinxia.cashman.conf.ExtProperties;
import com.xianjinxia.cashman.domain.LoanContract;
import com.xianjinxia.cashman.dto.UserNameInfoDto;
import com.xianjinxia.cashman.enums.ContractTypeEnum;
import com.xianjinxia.cashman.remote.OldCashmanRemoteService;
import com.xianjinxia.cashman.response.MerchantInfoResponse;
import com.xianjinxia.cashman.service.*;
import com.xianjinxia.cashman.utils.FileUtil;
import com.xianjinxia.cashman.utils.StringUtil;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import com.xianjinxia.cashman.response.BaseResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by liquan on 2017/11/22.
 */
@Api(tags = "cashman_app pdf controller")
@RequestMapping("/service/demoPdf/")
@RestController
public class DemoPdfController {

    @Autowired
    private ILoanContractService loanContractService;
    @Autowired
    private IPlatFormContractService platFormContractService;
    @Autowired
    private IDepositContractService depositContractService;
    @Autowired
    private IPdfService pdfService;
    @Autowired
    private OldCashmanRemoteService oldCashmanRemoteService;
    @Autowired
    private ExtProperties extProperties;
    @Autowired
    private IContractService contractService;
    //@Autowired
    //PdfGenaratorUtil pdfGenaratorUtil;
    @GetMapping("/uploanLoanContract")
    public BaseResponse<String> getloanLoanContract(){
        BaseResponse<String> resp = new BaseResponse<>();
        try {

            LoanContract  contract =new LoanContract() ;
            contract.setId(326L);
            contract.setContractName("loan_contract");
            contract.setTrdLoanId(142821L);
            contract.setUserId(8226280L);
            contract.setContractType(ContractTypeEnum.LOAN_AGREEMENT.getType());
            loanContractService.uploanLoanContract(contract);
        }catch (Exception e){
            resp.systemError();
        };

        return  resp;
    }
    @GetMapping("/upDepositContract")
    public BaseResponse<String> getDepositContract(){
        BaseResponse<String> resp = new BaseResponse<>();
        try {

            LoanContract  contract =new LoanContract() ;
            contract.setId(328L);
            contract.setContractName("loan_contract");
            contract.setTrdLoanId(142821L);
            contract.setUserId(8226280L);
            contract.setContractType(ContractTypeEnum.DEPOSITORY_AGREEMENT.getType());
            depositContractService.upDepositContract(contract);
        }catch (Exception e){
            resp.systemError();
        };

        return  resp;
    }

    @GetMapping("/upPlatFormContract")
    public BaseResponse<String> getPlatformContract(){
        BaseResponse<String> resp = new BaseResponse<>();
        try {

            LoanContract  contract =new LoanContract() ;
            contract.setId(327L);
            contract.setTrdLoanId(142821L);
            contract.setUserId(8226280L);
            contract.setContractName("platform_contract");
            contract.setContractType(ContractTypeEnum.PLATFORM_SERVICE_AGREEMENT.getType());
            platFormContractService.upPlatformContract(contract);
        }catch (Exception e){
            resp.systemError();
        };

        return  resp;
    }
    @GetMapping("/getPath")
    public BaseResponse<String> getPath(){//HttpServletResponse response){
        BaseResponse<String> resp = new BaseResponse<>();
        InputStream is = null;
        BufferedReader in =null;
        try {
            //返回读取指定资源的输入流
            is=Thread.currentThread().getContextClassLoader().getResourceAsStream("/pdf/loan-protocol-large.html");
            String str ="";
            String s="";
            in = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            while ((str = in.readLine()) != null) {
                s+= str;
            }
            resp.setData(s);
        }catch (Exception e){
            resp.systemError();
        }finally {
            try{
                is.close();
                in.close();
            }catch (IOException e){
            }
        };

        return  resp;
    }
    @GetMapping("/getUserInfo")
    public BaseResponse<UserNameInfoDto> getUserInfo(Long userId){
        BaseResponse<UserNameInfoDto> resp = new BaseResponse<>();
        try {
            UserNameInfoDto userNameInfoDto =oldCashmanRemoteService.getBaseUserInfo(userId);
            if(null==userNameInfoDto){
               resp.systemError();
            }
            resp.setData(userNameInfoDto);
        }catch (Exception e){
            resp.systemError();
        };
        return resp;
    }

    @GetMapping("/getJarPath")
    public BaseResponse<String> getJarPath(){
        BaseResponse<String> resp = new BaseResponse<>();
        String is=this.getClass().getClassLoader().getResource("/simsun.ttc").getPath();
        resp.setData(is);
        return resp;
    }

    @GetMapping("/getSmsConfig")
    public BaseResponse<String> getSmsConfig(){
        BaseResponse<String> resp = new BaseResponse<>();
        resp.setData(extProperties.getSmsConfig().getUrl());
        return resp;
    }

    @GetMapping("/uploadStsTTC")
    public BaseResponse<String> uploadStsTTC(){
        BaseResponse<String> resp = new BaseResponse<>();
        try {
            InputStream is = new ByteArrayInputStream(FileUtil.getBytes("D:/simsun.ttc"));
            String result = pdfService.uploadFileToOSS("contract/bigloan/simsun.ttc",is );
            resp.setData(result);
        }catch (Exception e){
            resp.systemError();
        };

        return  resp;
    }

    @GetMapping("/getRootPaths")
    public BaseResponse<String> getSttPath(){

        BaseResponse<String> resp = new BaseResponse<>();
        try {
            resp.setData(StringUtil.getRootPath());
        }catch (Exception e){
            resp.systemError();
        };

        return  resp;
    }
    @GetMapping("/getUploadPathsToOss")
    public BaseResponse<List<String>> getUploadPathsToOss(Long trdLoanId)throws IOException,DocumentException{
        BaseResponse<List<String>> resp = new BaseResponse<>();
        List<String> pathKeys=new ArrayList<String>();
        pathKeys.add(depositContractService.getDepositContracctUrl(ContractTypeEnum.DEPOSITORY_AGREEMENT.getType(),trdLoanId));
        pathKeys.add(platFormContractService.getPlatformContracctUrl(ContractTypeEnum.PLATFORM_SERVICE_AGREEMENT.getType(),trdLoanId));
        pathKeys.add(loanContractService.getLoanContracctUrl(ContractTypeEnum.LOAN_AGREEMENT.getType(),trdLoanId));
        resp.setData(pathKeys);
        return  resp;
    }

    @GetMapping("/getMerchantNo")
    public BaseResponse<List<MerchantInfoResponse>> getMerchantNo(String merchantNo)throws IOException,DocumentException{
        BaseResponse<List<MerchantInfoResponse>> resp = new BaseResponse<>();
        List<MerchantInfoResponse> merchantInfoResponses=oldCashmanRemoteService.getMerchantinfo(merchantNo);
        resp.setData(merchantInfoResponses);
        return  resp;
    }

    @GetMapping("/getMerchantInfo")
    public BaseResponse<MerchantInfoResponse> getMerchantInfo(String merchantNo){
        BaseResponse<MerchantInfoResponse> resp = new BaseResponse<>();
        MerchantInfoResponse merchantInfoResponses=contractService.getMerchantInfo(merchantNo);
        resp.setData(merchantInfoResponses);
        return  resp;
    }
}
