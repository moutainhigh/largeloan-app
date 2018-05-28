package com.xianjinxia.cashman.schedule;

import com.github.pagehelper.Page;
import com.xianjinxia.cashman.domain.PaymentRequest;
import com.xianjinxia.cashman.enums.PaymentBizTypeEnum;
import com.xianjinxia.cashman.enums.PaymentRequestStatusEnum;
import com.xianjinxia.cashman.mapper.PaymentRequestMapper;
import com.xianjinxia.cashman.schedule.job.PagebleScanCollectionJob;
import com.xianjinxia.cashman.service.IRepaymentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 处理超时的订单（不含定时代扣类型的订单），只处理用户主动触发的还款
 */
@Service
public class HandlerPaymentRequestTimeoutJob extends PagebleScanCollectionJob<PaymentRequest, List<PaymentRequest>> {

    private static Logger logger = LoggerFactory.getLogger(HandlerPaymentRequestTimeoutJob.class);

    @Autowired
    private IRepaymentService repaymentService;

    @Autowired
    private PaymentRequestMapper paymentRequestMapper;


    @Override
    public void process(List<PaymentRequest> item) {
        for (PaymentRequest paymentRequest : item) {
            try {
                repaymentService.repayCheckWithPayCenter(paymentRequest.getId());
            } catch (Exception e) {
                logger.error("检查订单超时，并对超时订单进行数据回滚失败: PaymentRequest[{}]", paymentRequest.getId(), e);
            }
        }
    }

    @Override
    public int pageSize() {
        return 500;
    }

    @Override
    public int threshold() {
        return 3000;
    }

    @Override
    public Page<PaymentRequest> fetch() {
        Page<PaymentRequest> pages = (Page<PaymentRequest>) paymentRequestMapper.selectExpiredPaymentRequest(PaymentRequestStatusEnum.NEW.getCode(), new Date(), PaymentBizTypeEnum.REPAYMENT.getCode());
        return pages;
    }
}