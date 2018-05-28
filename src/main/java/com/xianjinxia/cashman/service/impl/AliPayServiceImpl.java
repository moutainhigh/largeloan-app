package com.xianjinxia.cashman.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayFundTransToaccountTransferRequest;
import com.alipay.api.response.AlipayFundTransToaccountTransferResponse;
import com.xianjinxia.cashman.conf.AliPayProperties;
import com.xianjinxia.cashman.conf.ExtProperties;
import com.xianjinxia.cashman.dto.AliPayAccount;
import com.xianjinxia.cashman.dto.AliPayDto;
import com.xianjinxia.cashman.service.IAliPayService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.LinkedHashMap;

@Service
public class AliPayServiceImpl implements IAliPayService {

    private static final String encoding = "UTF-8";
    @Autowired
    private AliPayProperties aliPayProperties;

    @Override
    public AliPayDto singleTransfer(AliPayDto aliPayDto) {
        LinkedHashMap<String, AliPayAccount> aliPayAccountConfig = aliPayProperties.getConfig();
        //根据订单区分阿里账号
        AliPayAccount aliPayAccount = aliPayAccountConfig.get("pujing");
        if (aliPayAccount == null) {
            return null;
        }

        String privateKey = aliPayAccount.getPrivateKey();
        if (StringUtils.isBlank(privateKey)) {
            privateKey = getSecurityKey(aliPayAccount.getPrivateKeyPath());
            aliPayAccount.setPrivateKey(privateKey);
        }

        String aliPayPublicKey = aliPayAccount.getAliPayPublicKey();
        if (StringUtils.isBlank(aliPayPublicKey)) {
            aliPayPublicKey = getSecurityKey(aliPayAccount.getAliPayPublicKeyPath());
            aliPayAccount.setAliPayPublicKey(aliPayPublicKey);
        }

        AlipayClient alipayClient = new DefaultAlipayClient(aliPayAccount.getServerUrl(),
                aliPayAccount.getAppId(),
                privateKey,
                "json",
                encoding,
                aliPayPublicKey,
                "RSA2");
        AlipayFundTransToaccountTransferRequest request = new AlipayFundTransToaccountTransferRequest();
        JSONObject bizContent = new JSONObject();
        bizContent.put("out_biz_no", "testOrder"+ Calendar.getInstance().getTimeInMillis());
        bizContent.put("payee_type", aliPayAccount.getPayeeType());
        bizContent.put("payee_account", aliPayDto.getAccount());
        BigDecimal bigDecimal = new BigDecimal(aliPayDto.getAmount());
        bizContent.put("amount", bigDecimal.divide(new BigDecimal("100"), 2, BigDecimal.ROUND_HALF_DOWN));
        bizContent.put("payer_show_name", aliPayDto.getUserName());
        bizContent.put("payee_real_name", aliPayDto.getUserRealName());
        bizContent.put("remark", aliPayDto.getRemark());
        request.setBizContent(bizContent.toJSONString());
        try {
            AlipayFundTransToaccountTransferResponse response = alipayClient.execute(request);
            System.out.println("阿里返回结果:"+ JSONObject.toJSONString(response));
        } catch (AlipayApiException e) {
            e.printStackTrace();
            return null;
        }
        return aliPayDto;
    }

    private String getSecurityKey(String keyFilePath) {
        if (StringUtils.isBlank(keyFilePath)) {
            return null;
        }
        StringBuffer keyText = new StringBuffer();
        byte[] filecontent = new byte[1024];
        InputStream in = null;
        try {
            in = AliPayServiceImpl.class.getResourceAsStream(keyFilePath);
            int charIn;
            while ((charIn=in.read(filecontent)) != -1) {
                keyText.append(new String(filecontent,encoding));
                filecontent = new byte[1024];
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return keyText.toString();
    }
}
