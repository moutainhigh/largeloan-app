package com.xianjinxia.cashman.controller;

import com.alibaba.fastjson.JSONObject;
import com.xianjinxia.cashman.constants.Constant;
import com.xianjinxia.cashman.dto.AliPayDto;
import com.xianjinxia.cashman.dto.PaymentParamDto;
import com.xianjinxia.cashman.request.RepaymentReq;
import com.xianjinxia.cashman.response.BaseResponse;
import com.xianjinxia.cashman.service.IAliPayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/service/alipay")
public class AliPayController extends BaseController {

    @Autowired
    private IAliPayService aliPayService;

    //支付宝商家向支付宝账户转账接口
    @PostMapping("/singleTransfer")
    public BaseResponse<PaymentParamDto> singleTransfer(@RequestBody AliPayDto aliPayDto) {
        BaseResponse<PaymentParamDto> baseResponse = new BaseResponse<>();
        aliPayDto = aliPayService.singleTransfer(aliPayDto);
        if(aliPayDto == null){
            baseResponse.setCode(BaseResponse.ResponseCode.BIZ_CHECK_FAIL.getValue());
            baseResponse.setMsg("请求失败");
        }else{
            baseResponse.setCode(BaseResponse.ResponseCode.SUCCESS.getValue());
        }
        return baseResponse;
    }

    @GetMapping("/aliPayMerchantUrl")
    public BaseResponse<JSONObject> aliPayMerchantUrl(String merchantNumber) {
        BaseResponse<JSONObject> baseResponse = new BaseResponse<>();
        baseResponse.setCode(BaseResponse.ResponseCode.SUCCESS.getValue());
        if(Constant.MERCHANT_NUMBER_CJXJX.equals(merchantNumber)){
            baseResponse.setData(Constant.ALIPAY_MERCHANT_INFO_CJXJX);
        }else if(Constant.MERCHANT_NUMBER_KJQB.equals(merchantNumber)){
            baseResponse.setData(Constant.ALIPAY_MERCHANT_INFO_KJQB);
        }else{
            baseResponse.setCode(BaseResponse.ResponseCode.BIZ_CHECK_FAIL.getValue());
            baseResponse.setMsg("商户号对应的地址不存在!");
        }
        return baseResponse;
    }
}
