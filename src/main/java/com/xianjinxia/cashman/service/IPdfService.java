package com.xianjinxia.cashman.service;

import com.lowagie.text.DocumentException;
import com.timevale.esign.sdk.tech.bean.result.FileDigestSignResult;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public interface IPdfService {

    /**
     * 根据字符串内容生成PDF的输出流
     * @param fontFilePath 支持中文的字体文件路径
     * @param content pdf内容
     * @return ByteArrayOutputStream
     * @throws IOException
     */
    ByteArrayOutputStream getPdfOutputStream(String fontFilePath,String content) throws IOException, DocumentException;

    /**
     * pdf签章
     * @param sealId
     * @param outputStream
     * @param fileName
     * @param posPage
     * @param posX
     * @param posY
     * @param width
     * @return
     */
    FileDigestSignResult signPdfByStream(int sealId, ByteArrayOutputStream outputStream, String
            fileName, String posPage, int posX, int posY, int width);

    /**
     * 将合同上传到OSS
     * @param key
     * @param inputStream
     * @return
     * @throws Exception
     */
    String uploadFileToOSS(String key, InputStream inputStream);

    /**
     * 根据上传到OSS的文件名获取文件url
     * @param key
     * @return
     */
    String getUploadFileUrl(String key);

    /**
     * 达旷
     * pdf签章
     * @param sealId
     * @param outputStream
     * @param fileName
     * @param posPage
     * @param posX
     * @param posY
     * @param width
     * @return
     */
    FileDigestSignResult signPdfByStreamDK(int sealId, ByteArrayOutputStream outputStream, String
            fileName, String posPage, int posX, int posY, int width);

    /**
     * 普靖
     * pdf签章
     * @param sealId
     * @param outputStream
     * @param fileName
     * @param posPage
     * @param posX
     * @param posY
     * @param width
     * @return
     */
    FileDigestSignResult signPdfByStreamPJ(int sealId, ByteArrayOutputStream outputStream, String
            fileName, String posPage, int posX, int posY, int width);

    /**
     * 自主生成对应公司的accountId
     * @param compayName
     * @param companyOrgCode
     * @return
     */
    String creatAccountIndependent(String compayName,String companyOrgCode);
    /**
     * 自主签章
     * @param accountId
     * @param sealId
     * @param outputStream
     * @param fileName
     * @param posPage
     * @param posX
     * @param posY
     * @param width
     * @return
     */
    FileDigestSignResult signPdfByStreamIndependent(String accountId,int sealId, ByteArrayOutputStream outputStream, String
            fileName, String posPage, int posX, int posY, int width);
}
