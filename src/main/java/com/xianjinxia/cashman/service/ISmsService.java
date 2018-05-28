package com.xianjinxia.cashman.service;

import com.xianjinxia.cashman.dto.SmsDto;

public interface ISmsService {

    Boolean sendSms(SmsDto smsDto);
}
