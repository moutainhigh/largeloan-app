package com.xianjinxia.cashman.service.repay;

import com.xianjinxia.cashman.domain.PaymentRequestConfig;
import com.xianjinxia.cashman.enums.PaymentChannelEnum;
import com.xianjinxia.cashman.enums.PaymentRequestStatusEnum;
import com.xianjinxia.cashman.exceptions.ServiceException;
import com.xianjinxia.cashman.constants.Constant;
import com.xianjinxia.cashman.domain.PaymentRequest;
import com.xianjinxia.cashman.enums.PaymentBizTypeEnum;
import com.xianjinxia.cashman.mapper.PaymentRequestConfigMapper;
import com.xianjinxia.cashman.mapper.PaymentRequestMapper;
import com.xianjinxia.cashman.mapper.ProductsMapper;
import com.xianjinxia.cashman.service.impl.RepaymentServiceImpl;
import com.xianjinxia.cashman.utils.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class PaymentRequestServiceImpl implements IPaymentRequestService {

    private static final Logger logger = LoggerFactory.getLogger(RepaymentServiceImpl.class);
    private static final Integer DEFAULT_EXPIRED_TIME_MIN = 10;
    private static final String DEFAULT_ORDER_NO = "-1";

    @Autowired
    private IRepaymentPlanService repaymentOrderService;

    @Autowired
    private PaymentRequestMapper paymentRequestMapper;

    @Autowired
    private ProductsMapper productsMapper;

    @Autowired
    private PaymentRequestConfigMapper paymentRequestConfigMapper;

    @Override
    @Transactional
    public PaymentRequest createPaymentRequest(Long userId, Integer amount, PaymentRequestStatusEnum paymentRequestStatusEnum, PaymentBizTypeEnum paymentBizTypeEnum) {
        PaymentRequest paymentRequest = new PaymentRequest();
        paymentRequest.setUserId(userId);
        //计算过期时间
        PaymentRequestConfig paymentRequestConfig = paymentRequestConfigMapper.selectOne();
        Date now = new Date();
        Date expiredTime = DateUtil.minutesAfter(now, paymentRequestConfig.getExpireMinutes());
        paymentRequest.setExpiredAt(expiredTime);
        paymentRequest.setCreatedAt(now);
        paymentRequest.setUpdatedAt(now);
        paymentRequest.setStatus(paymentRequestStatusEnum.getCode());
        paymentRequest.setRespOrderId("-1");
        paymentRequest.setRespMsg("-1");
        paymentRequest.setRespTime(new Date());
        paymentRequest.setAmount(amount);
        paymentRequest.setPaymentType(paymentBizTypeEnum.getCode());
        paymentRequest.setRemark(paymentBizTypeEnum.getText());
        paymentRequest.setPaymentChannel("");

        int count = paymentRequestMapper.insert(paymentRequest);

        if (count != 1) {
            throw new ServiceException(Constant.DB_INSERT_ERROR);
        }
        

        logger.info("创建支付请求:{}", paymentRequest);
        return paymentRequest;
    }

    @Override
    public PaymentRequest createPaymentRequestByCmsIncome(Long userId, Integer amount, PaymentRequestStatusEnum paymentRequestStatusEnum, PaymentBizTypeEnum paymentBizTypeEnum, String thirdOrderNo, String incomeAccount, Date respTime) {
        PaymentRequest paymentRequest = new PaymentRequest();
        paymentRequest.setUserId(userId);
        //计算过期时间
        PaymentRequestConfig paymentRequestConfig = paymentRequestConfigMapper.selectOne();
        Date now = new Date();
        Date expiredTime = DateUtil.minutesAfter(now, paymentRequestConfig.getExpireMinutes());
        paymentRequest.setExpiredAt(expiredTime);
        paymentRequest.setCreatedAt(now);
        paymentRequest.setUpdatedAt(now);
        paymentRequest.setStatus(paymentRequestStatusEnum.getCode());
        paymentRequest.setRespMsg(paymentBizTypeEnum.getText());
        paymentRequest.setAmount(amount);
        paymentRequest.setPaymentType(paymentBizTypeEnum.getCode());
        paymentRequest.setPaymentChannel(PaymentChannelEnum.OTHER_CHANNEL.getName());
        paymentRequest.setRemark(paymentBizTypeEnum.getText());
        paymentRequest.setRespOrderId(thirdOrderNo);
        paymentRequest.setThirdOrderNo(thirdOrderNo);
        paymentRequest.setRespTime(respTime);

        int count = paymentRequestMapper.insert(paymentRequest);

        if (count != 1) {
            throw new ServiceException(Constant.DB_INSERT_ERROR);
        }

        logger.info("创建支付请求:{}", paymentRequest);
        return paymentRequest;
    }

    @Override public PaymentRequest createPaymentRequest(Long userId, Integer amount, PaymentRequestStatusEnum paymentRequestStatusEnum,
                                                         PaymentBizTypeEnum paymentBizTypeEnum, PaymentChannelEnum paymentChannelEnum) {
        PaymentRequest paymentRequest = new PaymentRequest();
        paymentRequest.setUserId(userId);
        Date now = new Date();
        paymentRequest.setExpiredAt(DateUtil.minutesAfter(now, DEFAULT_EXPIRED_TIME_MIN));
        paymentRequest.setCreatedAt(now);
        paymentRequest.setUpdatedAt(now);
        paymentRequest.setStatus(paymentRequestStatusEnum.getCode());
        paymentRequest.setRespMsg(paymentBizTypeEnum.getText());
        paymentRequest.setRespTime(now);
        paymentRequest.setAmount(amount);
        paymentRequest.setPaymentType(paymentBizTypeEnum.getCode());
        paymentRequest.setPaymentChannel(paymentChannelEnum.getName());
        paymentRequest.setRemark(paymentBizTypeEnum.getText());
        paymentRequest.setRespOrderId(DEFAULT_ORDER_NO);
        paymentRequest.setThirdOrderNo(DEFAULT_ORDER_NO);

        int count = paymentRequestMapper.insert(paymentRequest);

        if (count != 1) {
            throw new ServiceException(Constant.DB_INSERT_ERROR);
        }

        logger.info("创建支付请求:{}", paymentRequest);
        return paymentRequest;
    }


    @Override
    @Transactional
    public void updatePaymentRequestStatusToSuccess(Long id, String respOrderId, String respMsg,String paymentChannel,String thirdOrderNo) {
        PaymentRequest paymentRequest = new PaymentRequest();
        paymentRequest.setId(id);
        paymentRequest.setRespOrderId(respOrderId);
        paymentRequest.setRespTime(new Date());
        paymentRequest.setRespMsg(respMsg);
        paymentRequest.setStatus(PaymentRequestStatusEnum.SUCCESS.getCode());
        paymentRequest.setPaymentChannel(paymentChannel);
        paymentRequest.setThirdOrderNo(thirdOrderNo);
        int count = paymentRequestMapper.updateByPrimaryKeySelective(paymentRequest);

        if (count != 1) {
            throw new ServiceException(Constant.DB_UPDATE_ERROR);
        }
        logger.info("修改支付请求PaymentRequest[{}]的状态为{}", id, PaymentRequestStatusEnum.SUCCESS.getText());
    }

    @Override
    @Transactional
    public void updatePaymentRequestStatusToFailure(Long id, String respOrderId, String respMsg,String paymentChannel,String thirdOrderNo) {
        PaymentRequest paymentRequest = new PaymentRequest();
        paymentRequest.setId(id);
        paymentRequest.setRespOrderId(respOrderId);
        paymentRequest.setRespTime(new Date());
        paymentRequest.setRespMsg(respMsg);
        paymentRequest.setStatus(PaymentRequestStatusEnum.FAILURE.getCode());
        paymentRequest.setPaymentChannel(paymentChannel);
        paymentRequest.setThirdOrderNo(thirdOrderNo);
        int count = paymentRequestMapper.updateByPrimaryKeySelective(paymentRequest);

        if (count != 1) {
            throw new ServiceException(Constant.DB_UPDATE_ERROR);
        }
        logger.info("修改支付请求PaymentRequest[{}]的状态为{}", id, PaymentRequestStatusEnum.FAILURE.getText());
    }

    @Override
    @Transactional
    public void updatePaymentRequestStatusToCancel(Long id) {
        PaymentRequest paymentRequest = new PaymentRequest();
        paymentRequest.setId(id);
        paymentRequest.setStatus(PaymentRequestStatusEnum.CANCEL.getCode());
        paymentRequest.setUpdatedAt(new Date());
        int count = paymentRequestMapper.updateByPrimaryKeySelective(paymentRequest);

        if (count != 1) {
            throw new ServiceException(Constant.DB_UPDATE_ERROR);
        }
        logger.info("修改支付请求PaymentRequest[{}]的状态为{}", id, PaymentRequestStatusEnum.CANCEL.getText());
    }


    @Override
    @Transactional
    public PaymentRequest getPaymentRequest(Long paymentRequestId) {
        PaymentRequest paymentRequest = paymentRequestMapper.selectByPrimaryKey(paymentRequestId);
        return paymentRequest;
    }

    @Override
    public PaymentRequest getPaymentRequestByUserIdAndStatus(Long userId, PaymentRequestStatusEnum paymentRequestStatusEnum) {
        PaymentRequest paymentRequest = paymentRequestMapper.selectByUserIdAndStatus(userId, paymentRequestStatusEnum.getCode());
        return paymentRequest;
    }

    @Override
    public int countByUserIdAndStatus(Long userId, PaymentRequestStatusEnum paymentRequestStatusEnum) {
        int count = paymentRequestMapper.countByUserIdAndStatus(userId, paymentRequestStatusEnum.getCode());
        return count;
    }

    @Override
    public PaymentRequest getPaymentRequestByRespOrderId(String thirdOrderNo) {
        return paymentRequestMapper.getPaymentRequestByRespOrderId(thirdOrderNo);
    }

}
