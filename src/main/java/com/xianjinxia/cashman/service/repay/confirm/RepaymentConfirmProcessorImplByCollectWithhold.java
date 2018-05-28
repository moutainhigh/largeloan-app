package com.xianjinxia.cashman.service.repay.confirm;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.ImmutableMap;
import com.xianjinxia.cashman.constants.QueueConstants;
import com.xianjinxia.cashman.domain.CollectRequest;
import com.xianjinxia.cashman.domain.PaymentRequest;
import com.xianjinxia.cashman.enums.CollectWithholdEnum;
import com.xianjinxia.cashman.enums.PaymentBizTypeEnum;
import com.xianjinxia.cashman.mapper.CollectRequestMapper;
import com.xianjinxia.cashman.utils.StringUtil;
import com.xjx.mqclient.pojo.MqMessage;
import com.xjx.mqclient.utils.common.FastJson;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * 催收代扣回调
 *
 * @author ganminghui
 */
@Service("RepaymentConfirmProcessorImplByCollectWithhold")
public class RepaymentConfirmProcessorImplByCollectWithhold extends RepaymentConfirmProcessorAbstract {
    private static final Logger LOGGER = LoggerFactory.getLogger(RepaymentConfirmProcessorImplByCollectWithhold.class);

    @Autowired CollectRequestMapper collectRequestMapper;

    @Override public RepaymentConfirmProcessResult process(RepaymentConfirmProcessParam repaymentConfirmProcessParam) {
        PaymentRequest paymentRequest = repaymentConfirmProcessParam.getPaymentRequest();
        if (!paymentRequest.getPaymentType().equals(PaymentBizTypeEnum.COLLECT_WITHHOLD.getCode())) { return new RepaymentConfirmProcessResult(false); }

        LOGGER.info("催收代扣回调开始执行!!!!");

        /** 执行还款的逻辑 */
        super.processRepayment(repaymentConfirmProcessParam);

        return new RepaymentConfirmProcessResult(true);
    }

    /**
     * 催收代扣支付中心成功后回调
     * <p>
     *     1. 修改催收请求状态
     *     2. 通知催收系统结果
     * </p>
     */
    @Override public void paymentCenterCallbackSuccAction(RepaymentConfirmProcessParam repaymentConfirmProcessParam) {
        String extData = Optional.ofNullable(repaymentConfirmProcessParam.getPayCenterCallbackReq().getExextData()).orElse(StringUtil.NULL_CHARACTER_STRING);
        CollectRequest request = StringUtils.isNotBlank(extData)?JSON.parseObject(extData, CollectRequest.class) : null;
        String payCenterRespMsg = Optional.ofNullable(repaymentConfirmProcessParam.getPayCenterCallbackReq().getMsg()).orElse(StringUtil.NULL_CHARACTER_STRING);
        LOGGER.info("催收代扣回调接收到支付中心成功代扣通知,extData:{},payCenterRespMsg:{}",extData,payCenterRespMsg);

        if(Objects.nonNull(request)){
            /** 更新请求状态 */
            int count = collectRequestMapper.updateStatusById(request.getId(), CollectWithholdEnum.STATUS_SUCCESS.getType(),payCenterRespMsg);
            LOGGER.info("催收代扣回调接收到支付中心成功通知并更新催收请求成功，影响行数:{}行",count);

            /** 构造请求参数,推送给催收系统
             *  <p>
             *      由于上层已经使用了mqclient.sendMessage,这里开启额外的线程发送mq,但是没有一致性保证.
             *  </p>
             * */
            CompletableFuture.runAsync(()->{
                String messageBodyJsonStr = FastJson.toJson(ImmutableMap.of("msg",payCenterRespMsg,"result",true,"uuid",request.getUuid()));
                mqMessageService.sendMessage(new MqMessage(messageBodyJsonStr, QueueConstants.CUISHOU_WITHHOLD_QUEUE_BIG));
                LOGGER.info("催收代扣通知催收系统成功,通知消息体:{}",messageBodyJsonStr);
            });
        }else {
            LOGGER.error("催收代扣回调收到支付中心成功通知,但未传回extData,因此不能修改请求状态、推送催收系统结果!!!");
        }
    }

    @Override public void paymentCenterCallbackFailAction(RepaymentConfirmProcessParam repaymentConfirmProcessParam) {
        String extData = Optional.ofNullable(repaymentConfirmProcessParam.getPayCenterCallbackReq().getExextData()).orElse(StringUtil.NULL_CHARACTER_STRING);
        CollectRequest request = StringUtils.isNotBlank(extData)?JSON.parseObject(extData, CollectRequest.class) : null;
        String payCenterRespMsg = Optional.ofNullable(repaymentConfirmProcessParam.getPayCenterCallbackReq().getMsg()).orElse(StringUtil.NULL_CHARACTER_STRING);
        LOGGER.info("催收代扣回调接收到支付中心失败代扣通知,extData:{},payCenterRespMsg:{}",extData,payCenterRespMsg);

        if(Objects.nonNull(request)){
            /** 更新请求状态 */
            int count = collectRequestMapper.updateStatusById(request.getId(), CollectWithholdEnum.STATUS_FAITURE.getType(),payCenterRespMsg);
            LOGGER.info("催收代扣回调接收到支付中心失败通知并更新催收请求成功，影响行数:{}行",count);

            /** 构造请求参数,推送给催收系统 */
            String messageBodyJsonStr = FastJson.toJson(ImmutableMap.of("msg",payCenterRespMsg,"result",false,"uuid",request.getUuid()));
            mqMessageService.sendMessage(new MqMessage(messageBodyJsonStr, QueueConstants.CUISHOU_WITHHOLD_QUEUE_BIG));
            LOGGER.info("催收代扣通知催收系统成功,通知消息体:{}",messageBodyJsonStr);
        }else {
            LOGGER.error("催收代扣回调收到支付中心失败通知,但未传回extData,因此不能修改请求状态、推送催收系统结果!!!");
        }
    }
}