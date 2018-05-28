package com.xianjinxia.cashman.service.impl;

import com.liquan.oss.OSSUpload;
import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.BaseFont;
import com.timevale.esign.sdk.tech.bean.OrganizeBean;
import com.timevale.esign.sdk.tech.bean.PosBean;
import com.timevale.esign.sdk.tech.bean.SignPDFStreamBean;
import com.timevale.esign.sdk.tech.bean.result.AddAccountResult;
import com.timevale.esign.sdk.tech.bean.result.AddSealResult;
import com.timevale.esign.sdk.tech.bean.result.FileDigestSignResult;
import com.timevale.esign.sdk.tech.bean.result.Result;
import com.timevale.esign.sdk.tech.bean.seal.OrganizeTemplateType;
import com.timevale.esign.sdk.tech.bean.seal.SealColor;
import com.timevale.esign.sdk.tech.impl.constants.OrganRegType;
import com.timevale.esign.sdk.tech.impl.constants.SignType;
import com.timevale.esign.sdk.tech.service.*;
import com.timevale.esign.sdk.tech.service.factory.*;
import com.timevale.tech.sdk.bean.HttpConnectionConfig;
import com.timevale.tech.sdk.bean.ProjectConfig;
import com.timevale.tech.sdk.bean.SignatureConfig;
import com.timevale.tech.sdk.constants.AlgorithmType;
import com.timevale.tech.sdk.constants.HttpType;
import com.xianjinxia.cashman.conf.ExtProperties;
import com.xianjinxia.cashman.constants.AgreementConstants;
import com.xianjinxia.cashman.service.IPdfService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xhtmlrenderer.pdf.ITextFontResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

import javax.annotation.PostConstruct;
import java.io.*;

@Service
public class PdfServiceImpl implements IPdfService {
    @Autowired
    private ExtProperties extProperties;

    private  static final Logger logger= LoggerFactory.getLogger(PdfServiceImpl.class);

    private static String accountId;

    private static String accountId_pj;

    /**
     * 获取生成模板及其填充数据的字节流
     * @param fontFilePath 支持中文的字体文件路径
     * @param content pdf内容
     * @return
     * @throws IOException
     * @throws DocumentException
     */
    @Override
    public ByteArrayOutputStream getPdfOutputStream(String fontFilePath,String content) throws IOException, DocumentException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ITextRenderer renderer = new ITextRenderer();
        renderer.setDocumentFromString(content);
        ITextFontResolver fontResolver = renderer.getFontResolver();
        //解决中文问题
        fontResolver.addFont(fontFilePath, BaseFont.IDENTITY_H,BaseFont.NOT_EMBEDDED);
        renderer.layout();
        renderer.createPDF(outputStream);
        renderer.finishPDF();
        return outputStream;
    }


    /***
     * 平台自身PDF摘要签署（文件流）； 盖章位置通过坐标定位；
     * 使用到接口：SelfSignServiceFactory.instance();
     * selfSignService.localSignPdf(signPDFStreamBean, posBean, sealId, SignType.Single);
     */
    @Override
    public FileDigestSignResult signPdfByStream(int sealId, ByteArrayOutputStream outputStream, String
            fileName, String posPage, int posX, int posY, int width){
        SignPDFStreamBean signPDFStreamBean = new SignPDFStreamBean();
		 /* 签署PDF文档信息: 待签署文档本地二进制数据*/

        signPDFStreamBean.setStream(outputStream.toByteArray());
		 /* 签署PDF文档信息: 文档名称，e签宝签署日志对应的文档名(可理解成签署后的PDF文件名)，若为空则取文档路径中的名称*/
        signPDFStreamBean.setFileName(fileName);
		 /* 签署PDF文档信息: 文档编辑密码，当目标PDF设置权限密码保护时必填*/
        signPDFStreamBean.setOwnerPassword(null);


        PosBean posBean = new PosBean();
		 /* 签章位置信息: 定位类型，0-坐标定位，1-关键字定位，默认0，若选择关键字定位，签署类型(signType)必须指定为关键字签署才会生效。*/
        posBean.setPosType(0);
		 /* 签章位置信息: 签署页码，若为多页签章，支持页码格式“1-3,5,8“，若为坐标定位时，不可空*/
        posBean.setPosPage(posPage);
		 /* 签章位置信息: 签署位置X坐标，若为关键字定位，相对于关键字的X坐标偏移量，默认0*/
        posBean.setPosX(posX);
		 /* 签章位置信息: 签署位置Y坐标，若为关键字定位，相对于关键字的Y坐标偏移量，默认0*/
        posBean.setPosY(posY);
		 /* 签章位置信息: 关键字，仅限关键字签章时有效，若为关键字定位时，不可空*/
        posBean.setKey(null);
		 /* 签章位置信息: 印章展现宽度，将以此宽度对印章图片做同比缩放。详细查阅接口文档的15 PosBean描述*/
        posBean.setWidth(width);

        SelfSignService selfSignService = SelfSignServiceFactory.instance();
	     /* SignType(签章类型):Single-单页签章、Multi-多页签章、Edges-签骑缝章、Key-关键字签章 */
        FileDigestSignResult fileDigestSignResult =
                selfSignService.localSignPdf(signPDFStreamBean, posBean, sealId, SignType.Single);


        if(fileDigestSignResult.getErrCode() == 0){
            logger.info("平台自身PDF摘要签署成功！msg:{},path:{}",fileDigestSignResult.getMsg(),signPDFStreamBean.getFileName());
        }
        else{

            logger.error("平台自身PDF摘要签署（文件流）失败，错误码:{},错误信息:{}",fileDigestSignResult.getMsg(),fileDigestSignResult.getErrCode());
        }
        return fileDigestSignResult;
    }


    @Override
    public String uploadFileToOSS(String key, InputStream inputStream) {
        OSSUpload ossUpload = new OSSUpload();
        ossUpload.sampleUploadWithInputStream(extProperties.getContractRelatedConfig().getCloudSpaceName(), key, inputStream);
        return ossUpload.sampleGetFileUrl(extProperties.getContractRelatedConfig().getCloudSpaceName(), key, extProperties.getContractRelatedConfig().getOssUrlExpire()).toString();
    }

    @Override
    public String getUploadFileUrl(String key) {
        OSSUpload ossUpload = new OSSUpload();
        return ossUpload.sampleGetFileUrl(extProperties.getContractRelatedConfig().getCloudSpaceName(), key, extProperties.getContractRelatedConfig().getOssUrlExpire()).toString();
    }

    /***
     * 项目初始化
     * 使用到的接口：sdk.init(proCfg, httpConCfg, sCfg);
     */
    @PostConstruct
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
        //初始化达旷企业账号--不再使用，统一使用普靖
//        accountId = addOrganizeAccountDK();
        //初始化普靖企业账号
        accountId_pj =addOrganizeAccountPJ();
    }

    /**
     * 创建达旷的企业账号ID
     * @return
     */
    private static String addOrganizeAccountDK(){

        AccountService accountService = AccountServiceFactory.instance();

        OrganizeBean organizeBean = new OrganizeBean();
  		/* 企业名称 */
        organizeBean.setName(AgreementConstants.COMPANY_TITLE_JQY);//"上海达旷金融信息服务有限公司");
  		/* 单位类型，0-普通企业，1-社会团体，2-事业单位，3-民办非企业单位，4-党政及国家机构，默认0 */
        organizeBean.setOrganType(0);
		/* 企业注册类型，含组织机构代码号、多证合一或工商注册码，默认组织机构代码号 */
        organizeBean.setRegType(OrganRegType.MERGE); //NORMAL:组织机构代码号; MERGE：多证合一，传递社会信用代码号;REGCODE:企业工商注册码;//达旷的为NORMAL
  		/* 组织机构代码号、社会信用代码号或工商注册号 D3504563BFBTTR6354*/ //C3359812B2XFKUHQPA//达旷的组织机构代码号为：MA1K32MN-7
        organizeBean.setOrganCode(AgreementConstants.DK_ORGAN_CODE);
  		/* 注册类型，1-代理人注册，2-法人注册，默认1 */
        organizeBean.setUserType(0);
  		/* 法定代表人归属地,0-大陆，1-香港，2-澳门，3-台湾，4-外籍，默认0 */
        organizeBean.setLegalArea(0);
        logger.info("----开始创建达旷企业账户...");
        AddAccountResult addAccountResult = accountService.addAccount(organizeBean);

        if (0 != addAccountResult.getErrCode()) {
            logger.warn("创建达旷企业账户失败，错误码=" + addAccountResult.getErrCode() + ",错误信息=" + addAccountResult.getMsg());
        }
        else{

            logger.info("----创建达旷企业账户成功！达旷企业账户accountId=" + addAccountResult.getAccountId());
        }
        return addAccountResult.getAccountId();

    }

    /**
     * 创建普靖的企业账号ID
     * @return
     */
    private static String addOrganizeAccountPJ(){

        AccountService accountService = AccountServiceFactory.instance();

        OrganizeBean organizeBean = new OrganizeBean();
  		/* 企业名称 */
        organizeBean.setName(AgreementConstants.COMPANY_TITLE_JSXJX_NEW);//"上海达旷金融信息服务有限公司");
  		/* 单位类型，0-普通企业，1-社会团体，2-事业单位，3-民办非企业单位，4-党政及国家机构，默认0 */
        organizeBean.setOrganType(0);
		/* 企业注册类型，含组织机构代码号、多证合一或工商注册码，默认组织机构代码号 */
        organizeBean.setRegType(OrganRegType.MERGE); //NORMAL:组织机构代码号; MERGE：多证合一，传递社会信用代码号;REGCODE:企业工商注册码;//达旷的为NORMAL
  		/* 组织机构代码号、社会信用代码号或工商注册号 D3504563BFBTTR6354*/ //C3359812B2XFKUHQPA//达旷的组织机构代码号为：MA1K32MN-7
        organizeBean.setOrganCode(AgreementConstants.PJ_ORGAN_CODE);
  		/* 注册类型，1-代理人注册，2-法人注册，默认1 */
        organizeBean.setUserType(0);
  		/* 法定代表人归属地,0-大陆，1-香港，2-澳门，3-台湾，4-外籍，默认0 */
        organizeBean.setLegalArea(0);
        logger.info("----开始创建普靖企业账户...");
        AddAccountResult addAccountResult = accountService.addAccount(organizeBean);

        if (0 != addAccountResult.getErrCode()) {
            logger.warn("创建普靖企业账户失败，错误码=" + addAccountResult.getErrCode() + ",错误信息=" + addAccountResult.getMsg());
        }
        else{

            logger.info("----创建普靖企业账户成功！普靖企业账户accountId=" + addAccountResult.getAccountId());
        }
        return addAccountResult.getAccountId();

    }
    /***
     * 创建达旷企业账户的印章,该企业账户印章是一个相对概念。可以理解成是你们公司的客户企业印章；（无下玄文）
     * 使用到接口：sealService.addTemplateSeal(accountId, OrganizeTemplateType.STAR, SealColor.RED, "合同专用章", "下弦文");
     */
    private static AddSealResult addOrganizeTemplateSeal(String accountId){

        System.out.println("----开始创建企业账户的印章...");
        SealService sealService = SealServiceFactory.instance();
		/* hText 生成印章中的横向文内容 如“合同专用章、财务专用章”
		 * qText 生成印章中的下弦文内容  公章防伪码（一串13位数字） 如91010086135601*/
        AddSealResult addSealResult = sealService.addTemplateSeal(accountId, OrganizeTemplateType.STAR, SealColor.RED, "合同专用章", "");
        if (0 != addSealResult.getErrCode()) {
            logger.warn("创建企业模板印章失败，错误码=" + addSealResult.getErrCode() + ",错误信息=" + addSealResult.getMsg());
        }
        else{
  			/* sealData 最终生成的电子印章图片Base64数据 */
            logger.info("----创建企业模板印章成功！sealData:" + addSealResult.getSealData());
        }
        return addSealResult;

    }

    @Override
    public FileDigestSignResult signPdfByStreamDK(int sealId, ByteArrayOutputStream outputStream, String fileName, String posPage, int posX, int posY, int width) {

        AddSealResult addOrganizeSealResult = addOrganizeTemplateSeal(accountId);

        SignPDFStreamBean signPDFStreamBean = new SignPDFStreamBean();
		 /* 签署PDF文档信息: 待签署文档本地二进制数据*/

        signPDFStreamBean.setStream(outputStream.toByteArray());
		 /* 签署PDF文档信息: 文档名称，e签宝签署日志对应的文档名(可理解成签署后的PDF文件名)，若为空则取文档路径中的名称*/
        signPDFStreamBean.setFileName(fileName);
		 /* 签署PDF文档信息: 文档编辑密码，当目标PDF设置权限密码保护时必填*/
        signPDFStreamBean.setOwnerPassword(null);

        PosBean posBean = new PosBean();
		 /* 签章位置信息: 定位类型，0-坐标定位，1-关键字定位，默认0，若选择关键字定位，签署类型(signType)必须指定为关键字签署才会生效。*/
        posBean.setPosType(0);
		 /* 签章位置信息: 签署页码，若为多页签章，支持页码格式“1-3,5,8“，若为坐标定位时，不可空*/
        posBean.setPosPage(posPage);
		 /* 签章位置信息: 签署位置X坐标，若为关键字定位，相对于关键字的X坐标偏移量，默认0*/
        posBean.setPosX(posX);
		 /* 签章位置信息: 签署位置Y坐标，若为关键字定位，相对于关键字的Y坐标偏移量，默认0*/
        posBean.setPosY(posY);
		 /* 签章位置信息: 关键字，仅限关键字签章时有效，若为关键字定位时，不可空*/
        posBean.setKey(null);
		 /* 签章位置信息: 印章展现宽度，将以此宽度对印章图片做同比缩放。详细查阅接口文档的15 PosBean描述*/
        posBean.setWidth(width);

        logger.info("----开始平台下的PDF摘要签署...");
        UserSignService userSignService = UserSignServiceFactory.instance();
	     /* SignType(签章类型):Single-单页签章、Multi-多页签章、Edges-签骑缝章、Key-关键字签章 */
        FileDigestSignResult fileDigestSignResult = userSignService.localSignPDF(accountId,addOrganizeSealResult.getSealData(), signPDFStreamBean, posBean, SignType.Single);
        if(fileDigestSignResult.getErrCode() == 0){
            logger.info("达旷平台自身PDF摘要签署成功！msg:{},path:{}",fileDigestSignResult.getMsg(),signPDFStreamBean.getFileName());
        }
        else{

            logger.error("达旷平台自身PDF摘要签署（文件流）失败，错误码:{},错误信息:{}",fileDigestSignResult.getMsg(),fileDigestSignResult.getErrCode());
        }
        return fileDigestSignResult;
    }

    @Override
    public FileDigestSignResult signPdfByStreamPJ(int sealId, ByteArrayOutputStream outputStream, String fileName, String posPage, int posX, int posY, int width) {

        AddSealResult addOrganizeSealResult = addOrganizeTemplateSeal(accountId_pj);

        SignPDFStreamBean signPDFStreamBean = new SignPDFStreamBean();
		 /* 签署PDF文档信息: 待签署文档本地二进制数据*/

        signPDFStreamBean.setStream(outputStream.toByteArray());
		 /* 签署PDF文档信息: 文档名称，e签宝签署日志对应的文档名(可理解成签署后的PDF文件名)，若为空则取文档路径中的名称*/
        signPDFStreamBean.setFileName(fileName);
		 /* 签署PDF文档信息: 文档编辑密码，当目标PDF设置权限密码保护时必填*/
        signPDFStreamBean.setOwnerPassword(null);

        PosBean posBean = new PosBean();
		 /* 签章位置信息: 定位类型，0-坐标定位，1-关键字定位，默认0，若选择关键字定位，签署类型(signType)必须指定为关键字签署才会生效。*/
        posBean.setPosType(0);
		 /* 签章位置信息: 签署页码，若为多页签章，支持页码格式“1-3,5,8“，若为坐标定位时，不可空*/
        posBean.setPosPage(posPage);
		 /* 签章位置信息: 签署位置X坐标，若为关键字定位，相对于关键字的X坐标偏移量，默认0*/
        posBean.setPosX(posX);
		 /* 签章位置信息: 签署位置Y坐标，若为关键字定位，相对于关键字的Y坐标偏移量，默认0*/
        posBean.setPosY(posY);
		 /* 签章位置信息: 关键字，仅限关键字签章时有效，若为关键字定位时，不可空*/
        posBean.setKey(null);
		 /* 签章位置信息: 印章展现宽度，将以此宽度对印章图片做同比缩放。详细查阅接口文档的15 PosBean描述*/
        posBean.setWidth(width);

        logger.info("----开始普靖金融下的PDF摘要签署...");
        UserSignService userSignService = UserSignServiceFactory.instance();
	     /* SignType(签章类型):Single-单页签章、Multi-多页签章、Edges-签骑缝章、Key-关键字签章 */
        FileDigestSignResult fileDigestSignResult = userSignService.localSignPDF(accountId_pj,addOrganizeSealResult.getSealData(), signPDFStreamBean, posBean, SignType.Single);
        if(fileDigestSignResult.getErrCode() == 0){
            logger.info("普靖金融自身PDF摘要签署成功！msg:{},path:{}",fileDigestSignResult.getMsg(),signPDFStreamBean.getFileName());
        }
        else{

            logger.error("普靖金融自身PDF摘要签署（文件流）失败，错误码:{},错误信息:{}",fileDigestSignResult.getMsg(),fileDigestSignResult.getErrCode());
        }
        return fileDigestSignResult;
    }

    @Override
    public String creatAccountIndependent(String compayName, String companyOrgCode) {

        AccountService accountService = AccountServiceFactory.instance();

        OrganizeBean organizeBean = new OrganizeBean();
  		/* 企业名称 */
        organizeBean.setName(compayName);
  		/* 单位类型，0-普通企业，1-社会团体，2-事业单位，3-民办非企业单位，4-党政及国家机构，默认0 */
        organizeBean.setOrganType(0);
		/* 企业注册类型，含组织机构代码号、多证合一或工商注册码，默认组织机构代码号 */
        organizeBean.setRegType(OrganRegType.MERGE); //NORMAL:组织机构代码号; MERGE：多证合一，传递社会信用代码号;REGCODE:企业工商注册码;//达旷的为NORMAL
  		/* 组织机构代码号、社会信用代码号或工商注册号 D3504563BFBTTR6354*/ //C3359812B2XFKUHQPA//达旷的组织机构代码号为：MA1K32MN-7
        organizeBean.setOrganCode(companyOrgCode);
  		/* 注册类型，1-代理人注册，2-法人注册，默认1 */
        organizeBean.setUserType(0);
  		/* 法定代表人归属地,0-大陆，1-香港，2-澳门，3-台湾，4-外籍，默认0 */
        organizeBean.setLegalArea(0);
        logger.info("----开始创建"+compayName+"企业账户...");
        AddAccountResult addAccountResult = accountService.addAccount(organizeBean);

        if (0 != addAccountResult.getErrCode()) {
            logger.warn("创建达"+compayName+"业账户失败，错误码=" + addAccountResult.getErrCode() + ",错误信息=" + addAccountResult.getMsg());
        }
        else{

            logger.info("----创建"+compayName+"企业账户成功！企业账户accountId=" + addAccountResult.getAccountId());
        }
        return addAccountResult.getAccountId();
    }

    @Override
    public FileDigestSignResult signPdfByStreamIndependent(String accountId, int sealId, ByteArrayOutputStream outputStream, String fileName, String posPage, int posX, int posY, int width) {
        AddSealResult addOrganizeSealResult = addOrganizeTemplateSeal(accountId);

        SignPDFStreamBean signPDFStreamBean = new SignPDFStreamBean();
		 /* 签署PDF文档信息: 待签署文档本地二进制数据*/
        signPDFStreamBean.setStream(outputStream.toByteArray());
		 /* 签署PDF文档信息: 文档名称，e签宝签署日志对应的文档名(可理解成签署后的PDF文件名)，若为空则取文档路径中的名称*/
        signPDFStreamBean.setFileName(fileName);
		 /* 签署PDF文档信息: 文档编辑密码，当目标PDF设置权限密码保护时必填*/
        signPDFStreamBean.setOwnerPassword(null);

        PosBean posBean = new PosBean();
		 /* 签章位置信息: 定位类型，0-坐标定位，1-关键字定位，默认0，若选择关键字定位，签署类型(signType)必须指定为关键字签署才会生效。*/
        posBean.setPosType(0);
		 /* 签章位置信息: 签署页码，若为多页签章，支持页码格式“1-3,5,8“，若为坐标定位时，不可空*/
        posBean.setPosPage(posPage);
		 /* 签章位置信息: 签署位置X坐标，若为关键字定位，相对于关键字的X坐标偏移量，默认0*/
        posBean.setPosX(posX);
		 /* 签章位置信息: 签署位置Y坐标，若为关键字定位，相对于关键字的Y坐标偏移量，默认0*/
        posBean.setPosY(posY);
		 /* 签章位置信息: 关键字，仅限关键字签章时有效，若为关键字定位时，不可空*/
        posBean.setKey(null);
		 /* 签章位置信息: 印章展现宽度，将以此宽度对印章图片做同比缩放。详细查阅接口文档的15 PosBean描述*/
        posBean.setWidth(width);

        logger.info("----开始企业的PDF摘要签署...");
        UserSignService userSignService = UserSignServiceFactory.instance();
	     /* SignType(签章类型):Single-单页签章、Multi-多页签章、Edges-签骑缝章、Key-关键字签章 */
        FileDigestSignResult fileDigestSignResult = userSignService.localSignPDF(accountId_pj,addOrganizeSealResult.getSealData(), signPDFStreamBean, posBean, SignType.Single);
        if(fileDigestSignResult.getErrCode() == 0){
            logger.info("企业自身PDF摘要签署成功！msg:{},path:{}",fileDigestSignResult.getMsg(),signPDFStreamBean.getFileName());
        }
        else{

            logger.error("企业自身PDF摘要签署（文件流）失败，错误码:{},错误信息:{}",fileDigestSignResult.getMsg(),fileDigestSignResult.getErrCode());
        }
        return fileDigestSignResult;
    }
}
