package com.xianjinxia.cashman.service;

import com.xianjinxia.cashman.dto.AliPayDto;

public interface IAliPayService {

    AliPayDto singleTransfer(AliPayDto aliPayDto);
}
