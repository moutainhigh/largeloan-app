package com.xianjinxia.cashman.remote;

import com.xianjingxia.paymentclient.paycenter.params.UserInfo;
import com.xianjinxia.cashman.conf.ExtProperties;
import com.xianjinxia.cashman.dto.*;
import com.xianjinxia.cashman.exceptions.ServiceException;
import com.xianjinxia.cashman.request.UserCouponReq;
import com.xianjinxia.cashman.request.UserInfoReq;
import com.xianjinxia.cashman.response.BaseResponse;
import com.xianjinxia.cashman.response.MerchantInfoResponse;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OldCashmanRemoteService extends BaseRemoteService {

    private static Logger logger = LoggerFactory.getLogger(OldCashmanRemoteService.class);

    //private static final String GET_USER_INFO = "/refactor/repay/userinfo";

    private static final String REPAY = "/refactor/repayment/repay";

    private static final String USER_PAYMENT_BANK_CARD_INFO = "/refactor/repayment/userinfo";

    private static final String COUPON_SIZE = "/refactorcoupon/couponsize";

    private static final String GET_BASE_USER_INFO = "/refactoruser/getBaseUserInfo";

    private static final String GET_EXPORT_USER_INFO = "/export/user/userInfo";

    private static final String RISK_QUERY_USER_INFO = "/user/";

    private static final String GET_USER_INFO = "/user/";

    //查询小额的历史订单
    private static final String GET_SMALL_ORDER = "/refactorOrder/findOrderInfo";
    /**
     * 获取商户号对应的商户信息
     */
    private static final String GET_MERCHANTINFO="/refactoruser/getMerchantInfoList";

    //查询银行卡号
    private static final String GET_BANK_CARD_NO = "/refactoruser/findOneCardInfo";

    @Autowired
    private ExtProperties extProperties;

    @Override
    protected String getServiceName() {
        return extProperties.getOldCashmanServerAddressConfig().getServerAddress();
    }

    /**
     * 恢复额度的接口目前需要是同步返回的接口，这样业务层的事务一致性得到保障
     *
     * @param repaymentRequest
     */
    public void repaymentSuccess(RepaymentRequest repaymentRequest) {
        String url = super.buildUrl(REPAY);
        BaseResponse<Void> response = myRestTemplate.httpPostWithAbsoluteUrl(url, repaymentRequest, new ParameterizedTypeReference<BaseResponse<Void>>() {
        });
        if (!response.getCode().equals(BaseResponse.ResponseCode.SUCCESS.getValue())) {
            throw new ServiceException(response.getCode(), response.getMsg());
        }
    }

    public Integer getUsebleCouponSize(UserCouponReq userCouponReq) {
        String url = super.buildUrl(COUPON_SIZE);
        JsonResult<String> jsonResult = null;
        try {
            jsonResult = myRestTemplate.httpPostWithAbsoluteUrl(url, userCouponReq, new ParameterizedTypeReference<JsonResult<String>>() {
            });
        } catch (Exception e) {
            logger.error("调用失败", e);
            return 0;
        }
        if (!jsonResult.getCode().equals(JsonResult.SUCCESS)) {
            throw new ServiceException(jsonResult.getCode(), jsonResult.getMsg());
        }
        Integer couponSize = NumberUtils.toInt((String) jsonResult.getData());
        return couponSize;
    }

    public UserNameInfoDto getBaseUserInfo(Long userId) {
        String url = super.buildUrl(GET_BASE_USER_INFO + "?userId=" + userId);
        UserNameInfoDto userNameInfoDto = null;
        try {
            BaseResponse<UserNameInfoDto> response = myRestTemplate.httpGetWithAbsoluteUrl(url, null, new ParameterizedTypeReference<BaseResponse<UserNameInfoDto>>() {
            });
            if (response.getCode().equals(JsonResult.SUCCESS)) {
                userNameInfoDto = response.getData();
            }
            if (null == userNameInfoDto) {
                throw new ServiceException("用户不存在");
            }
        } catch (Exception e) {
            logger.error("调用失败", e);
        }

        return userNameInfoDto;
    }

    public UserDetailDto getUserDetail(Long userId) {
        String url = super.buildUrl(GET_EXPORT_USER_INFO);
        UserDetailDto userDetailDto = null;
        BaseResponse<UserDetailDto> response = null;
        try {
            response = myRestTemplate.httpPostWithAbsoluteUrl(url, new UserInfoReq(userId), new ParameterizedTypeReference<BaseResponse<UserDetailDto>>() {
            });
        } catch (Exception e) {
            logger.error("调用失败", e);
            throw new ServiceException("调用户信息查询接口失败");
        }
        if (null == response) {
            throw new ServiceException("调用户信息查询接口失败");
        }
        if (response.getCode().equals(JsonResult.USER_DETAIL_SUCCESS_CODE) || response.getCode().equals(JsonResult.USER_DETAIL_NOT_FIND_CODE)) {
            userDetailDto = response.getData();
        } else {
            throw new ServiceException(response.getCode(), response.getMsg());
        }

        if (response.getCode().equals(JsonResult.USER_DETAIL_NOT_FIND_CODE)) {
            logger.error("用户不存在, userId=" + userId);
            userDetailDto = null;
        }

        return userDetailDto;
    }

    public UserInfoQueryDto getRiskQueryUserInfo(Long userId) {
        String url = super.buildUrl(RISK_QUERY_USER_INFO + userId);
        logger.info("url====>"+url);
        UserInfoQueryDto infoDto = null;
        try {
            infoDto = myRestTemplate.httpGetWithAbsoluteUrl(url, null, new ParameterizedTypeReference<UserInfoQueryDto>() {
            });
        } catch (Exception e) {
            logger.error("风控查询自有信息出错", e);
        }
        return infoDto;
    }

    public UserInfo getUserInfo(Long userId) {
        String url = super.buildUrl(USER_PAYMENT_BANK_CARD_INFO + "?userId=" + userId);
        UserInfo userInfo = null;
        try {
            BaseResponse<UserInfo> response = myRestTemplate.httpGetWithAbsoluteUrl(url, null, new ParameterizedTypeReference<BaseResponse<UserInfo>>() {
            });
            if (response.getCode().equals(BaseResponse.ResponseCode.SUCCESS.getValue())) {
                userInfo = response.getData();
            }
            if (null == userInfo) {
                throw new ServiceException("用户不存在");
            }
        } catch (Exception e) {
            logger.error("调用失败", e);
            throw new ServiceException("查询用户代扣银行卡信息失败" + userId);
        }

        return userInfo;
    }


    /**
     * 查询小额历史订单
     * @param userId
     * @return
     */
    public List<OrderInfoDto> getSmallHistoryOrder(Long userId){
        String url = super.buildUrl(GET_SMALL_ORDER + "?userId=" + userId);
        List<OrderInfoDto> smallHistoryOrderList = new ArrayList<OrderInfoDto>();
        try {
            BaseResponse<List<OrderInfoDto>> response = myRestTemplate.httpGetWithAbsoluteUrl(url, null, new ParameterizedTypeReference<BaseResponse<List<OrderInfoDto>>>(){});
            if(BaseResponse.ResponseCode.SUCCESS.getValue().equals(response.getCode())){
                smallHistoryOrderList = response.getData();
            }
        } catch (Exception e) {
            logger.error("getSmallHistoryOrder调用失败,userId="+userId, e);
        }
        return smallHistoryOrderList;
    }

    public String getBankCardNoById(Long id) {
        if(id == null){
            return "";
        }
        String url = super.buildUrl(GET_BANK_CARD_NO);
        String bankCardNo = "";
        try {
            BaseResponse<BankCardResultDto> response = myRestTemplate.httpPostWithAbsoluteUrl(url, new BankCardQueryDto(id), new ParameterizedTypeReference<BaseResponse<BankCardResultDto>>(){});
            if ("0".equals(response.getCode())) {
                if(response.getData().getUserCardList().size()!=0) {
                    BankCardDto bankCardDto = response.getData().getUserCardList().get(0);
                    logger.info("查询银行卡信息为：{}",bankCardDto.toString());
                    bankCardNo = bankCardDto.getCard_no();
                    logger.info("银行卡号为：{}",bankCardNo);
                }
            }
        } catch (Exception e) {
            logger.error("getBankCardNoById调用失败,bankId=" + id, e);
        }
        return bankCardNo;
    }

    public List<MerchantInfoResponse> getMerchantinfo(String merchantName){
        String url = super.buildUrl(GET_MERCHANTINFO + "?merchantName=" + merchantName);
        List<MerchantInfoResponse> merchantInfoList = new ArrayList<MerchantInfoResponse>();
        BaseResponse<List<MerchantInfoResponse>> response =null;
        try {
            response = myRestTemplate.httpGetWithAbsoluteUrl(url, null, new ParameterizedTypeReference<BaseResponse<List<MerchantInfoResponse>>>(){}) ;
            if(BaseResponse.ResponseCode.SUCCESS.getValue().equals(response.getCode())){
                merchantInfoList = response.getData();
                if(CollectionUtils.isEmpty(merchantInfoList)){
                    logger.info("未查到对应商户详细信息,商户号为："+merchantName);
                    throw new ServiceException("未查到对应商户详细信息,商户号为："+merchantName);
                }

            }else{
               logger.info("查询商户号数据异常--/refactoruser/getMerchantInfoList调用oldcashman");
            }
        } catch (Exception e) {
                logger.error("查询商户号对应信息--/refactoruser/getMerchantInfoList调用oldcashman失败,merchantNo="+merchantName, e);
            throw new ServiceException("未查到对应商户详细信息调用oldcashman失败,商户号为："+merchantName);
        }

        return merchantInfoList;
    }
}

