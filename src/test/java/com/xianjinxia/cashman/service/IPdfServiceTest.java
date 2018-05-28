package com.xianjinxia.cashman.service;

import com.lowagie.text.DocumentException;
import com.timevale.esign.sdk.tech.bean.OrganizeBean;
import com.timevale.esign.sdk.tech.bean.result.AddAccountResult;
import com.timevale.esign.sdk.tech.bean.result.FileDigestSignResult;
import com.timevale.esign.sdk.tech.bean.result.Result;
import com.timevale.esign.sdk.tech.impl.constants.OrganRegType;
import com.timevale.esign.sdk.tech.service.AccountService;
import com.timevale.esign.sdk.tech.service.EsignsdkService;
import com.timevale.esign.sdk.tech.service.factory.AccountServiceFactory;
import com.timevale.esign.sdk.tech.service.factory.EsignsdkServiceFactory;
import com.timevale.tech.sdk.bean.HttpConnectionConfig;
import com.timevale.tech.sdk.bean.ProjectConfig;
import com.timevale.tech.sdk.bean.SignatureConfig;
import com.timevale.tech.sdk.constants.AlgorithmType;
import com.timevale.tech.sdk.constants.HttpType;
import com.xianjinxia.cashman.conf.ExtProperties;
import com.xianjinxia.cashman.constants.AgreementConstants;
import com.xianjinxia.cashman.constants.Constant;
import com.xianjinxia.cashman.enums.LoanContractStatusEnum;
import com.xianjinxia.cashman.service.impl.PlatFormContractServiceImpl;
import com.xianjinxia.cashman.utils.FileUtil;
import com.xianjinxia.cashman.utils.StringUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.ClassUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by liquan on 2017/12/7.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class IPdfServiceTest {

    @Autowired
    private IPdfService pdfService;
    @Autowired
    private ExtProperties extProperties;

    private static final Logger logger = LoggerFactory.getLogger(PlatFormContractServiceImpl.class);

    private String templateStr ="";

    private static String accountId;
    @Before
    public void init() {
        //加载html文件
        try {
            initProject();
            this.templateStr = StringUtil.getFileTemplate(new StringBuilder(StringUtil.getRootPath()).append(AgreementConstants.PDF_FOLDER_PATH).append(AgreementConstants.PATH_BORDER).append(AgreementConstants.BORROW_PLATFORM_TEPLATE_PATH).toString());
        }catch (IOException e){
            logger.info("读取文件模板失败");
        }
    }

    @Test
    public void getPdfOutputStream(){
        //转换pdf流
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try{
            out=pdfService.getPdfOutputStream(
                new StringBuilder(ClassUtils.getDefaultClassLoader().getResource("").getPath()).append(AgreementConstants.PDF_FOLDER_PATH).append(AgreementConstants.PATH_BORDER).append(AgreementConstants.FONT_SIM_SUN).toString(),
                templateStr);
            FileUtil.getFile(out.toByteArray(),"D:","D:/test.pdf");
        }catch (IOException e) {
            logger.info("IO流转换失败！");
        } catch (DocumentException d) {
            logger.info("itext转换pdf失败！");
        } finally {
            try{
                out.flush();
                out.close();
            }catch (IOException e){
                logger.info("IO流关闭失败！");
            }
        }
    }


    @Test
    public void fileDigestSignResultForDk() {
        //测试签名流
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try{
            out.write(FileUtil.getBytes("D:/test.pdf"));
            FileDigestSignResult fileDigestSignResult = pdfService.signPdfByStreamDK(AgreementConstants.PLATFORM_SEALID, out,"",
                    AgreementConstants.PLATFORM_POSPAGE, AgreementConstants.PLATFORM_POSX, AgreementConstants.PLATFORM_POSY_SINGLE,AgreementConstants.PLATFORM_WIDTH);

            FileUtil.getFile(fileDigestSignResult.getStream(),"D:","D:/signTest.pdf");
        }catch (IOException e){
            logger.info("IO异常");
        }/*catch (DocumentException d) {
            logger.info("itext转换pdf失败！");
        }*/ finally {
            try{
                out.flush();
                out.close();
            }catch (IOException e){
                logger.info("IO流关闭失败！");
            }
        }
    }


    @Test
    public void uploadFileToOSS(){
        //测试上传--到文件夹contract/bigloan/，文件名为PLATFORM_SERVICE_AGREEMENT_1018.pdf
        InputStream is = new ByteArrayInputStream(FileUtil.getBytes("D:/PLATFORM_SERVICE_AGREEMENT_1018.pdf"));
        String result = pdfService.uploadFileToOSS("contract/bigloan/PLATFORM_SERVICE_AGREEMENT_1018.pdf",is );
        logger.info("上传路径为："+result);//http://xjx-files.oss-cn-hangzhou.aliyuncs.com/files/platformServiceTemplate_bigAmount/test001.pdf?Expires=1512717527&OSSAccessKeyId=LTAIGtLs9U0rENY6&Signature=QfQQwoE11SwgFFUu3NyNGSXqfY0%3D
    }

    @Test
    public void getUploadFileUrl(){
        String url = pdfService.getUploadFileUrl("contract/bigloan/PLATFORM_SERVICE_AGREEMENT_1018.pdf");
        logger.info("文件路径为："+url);
    }

    /***
     * 项目初始化--调用e签宝签名之前需要初始化
     * 使用到的接口：sdk.init(proCfg, httpConCfg, sCfg);
     */
    public void initProject(){

        HttpConnectionConfig httpConCfg = new HttpConnectionConfig();

        ProjectConfig proCfg = new ProjectConfig();

		/* 项目ID(应用ID) */
        proCfg.setProjectId(extProperties.getSdkInitConfig().getProjectAppId());//Constant.PROJECT_APP_ID);   //1111563517
  	    /* 项目Secret(应用Secret) */
        proCfg.setProjectSecret(extProperties.getSdkInitConfig().getProjectAppSecret());//Constant.PROJECT_APP_SECRET);   // 95439b0863c241c63a861b87d1e647b7

  		/* 开放平台地址 "http://121.40.164.61:8080/tgmonitor/rest/app!getAPIInfo2"*/
        proCfg.setItsmApiUrl(extProperties.getSdkInitConfig().getProjectUrl());//Constant.Project_URL);

  		/* 协议类型，默认https */
        httpConCfg.setHttpType(HttpType.HTTPS);
  		/* 代理服务IP地址 */
        httpConCfg.setProxyIp(null);
  		/* 代理服务端口 */
        httpConCfg.setProxyPort(new Integer(0));
  		/* 请求失败重试次数，默认5次 */
        httpConCfg.setRetry(new Integer(5));

        SignatureConfig sCfg = new SignatureConfig();

  		/* 算法类型，默认HMAC-SHA256 */
        sCfg.setAlgorithm(AlgorithmType.HMACSHA256); //可选RSA
		/* e签宝公钥，可以从开放平台获取。若算法类型为RSA，此项必填 */
        sCfg.setEsignPublicKey("");
		/* 平台私钥，可以从开放平台下载密钥生成工具生成。若算法类型为RSA，此项必填 */
        sCfg.setPrivateKey("");

        logger.info("----开始项目初始化...");

        EsignsdkService sdk = EsignsdkServiceFactory.instance();
        Result result = sdk.init(proCfg, httpConCfg, sCfg);

        if (0 != result.getErrCode()) {
            logger.info("项目初始化失败，错误码=" + result.getErrCode() + ",错误信息=" + result.getMsg());
        }else{
            logger.info("----项目初始化成功！" );
        }


    }

    /**
     * 创建达旷的企业账号ID
     * @return
     */
    private static String addOrganizeAccountDK(){

        AccountService accountService = AccountServiceFactory.instance();

        OrganizeBean organizeBean = new OrganizeBean();
  		/* 企业名称 */
        organizeBean.setName("上海达旷金融信息服务有限公司");
  		/* 单位类型，0-普通企业，1-社会团体，2-事业单位，3-民办非企业单位，4-党政及国家机构，默认0 */
        organizeBean.setOrganType(0);
		/* 企业注册类型，含组织机构代码号、多证合一或工商注册码，默认组织机构代码号 */
        organizeBean.setRegType(OrganRegType.MERGE); //NORMAL:组织机构代码号; MERGE：多证合一，传递社会信用代码号;REGCODE:企业工商注册码;//达旷的为NORMAL
  		/* 组织机构代码号、社会信用代码号或工商注册号 D3504563BFBTTR6354*/ //C3359812B2XFKUHQPA//达旷的组织机构代码号为：MA1K32MN-7
        organizeBean.setOrganCode("C3359812B2XFKUHQPA");
  		/* 注册类型，1-代理人注册，2-法人注册，默认1 */
        organizeBean.setUserType(0);
  		/* 法定代表人归属地,0-大陆，1-香港，2-澳门，3-台湾，4-外籍，默认0 */
        organizeBean.setLegalArea(0);
        System.out.println("----开始创建达旷企业账户...");
        AddAccountResult addAccountResult = accountService.addAccount(organizeBean);

        if (0 != addAccountResult.getErrCode()) {
            System.out.println("创建达旷企业账户失败，错误码=" + addAccountResult.getErrCode() + ",错误信息=" + addAccountResult.getMsg());
        }
        else{

            System.out.println("----创建达旷企业账户成功！达旷企业账户accountId=" + addAccountResult.getAccountId());
            System.out.println("");
        }
        return addAccountResult.getAccountId();

    }
}
