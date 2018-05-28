package com.xianjinxia.cashman.service.impl;

import com.xianjinxia.cashman.conf.ExtProperties;
import com.xianjinxia.cashman.conf.MyRestTemplate;
import com.xianjinxia.cashman.dto.SmsDto;
import com.xianjinxia.cashman.service.ISmsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class SmsServiceImpl implements ISmsService{

    private Logger logger= LoggerFactory.getLogger(SmsServiceImpl.class);

    @Autowired
    private MyRestTemplate myRestTemplate;

    @Autowired
    private ExtProperties extProperties;

    @Override
    public Boolean sendSms(SmsDto smsDto) {
        try {
            myRestTemplate.httpPostWithAbsoluteUrl(extProperties.getSmsConfig().getUrl(),
                   smsDto, new ParameterizedTypeReference<Map>(){});
            return  true;
        } catch (Exception e) {
            logger.error("send sms fail,templateCode:{},telephone:{}",smsDto.getMsgTemplateCode(),smsDto.getTelephone(),e);
            return  false;
        }
    }
}
