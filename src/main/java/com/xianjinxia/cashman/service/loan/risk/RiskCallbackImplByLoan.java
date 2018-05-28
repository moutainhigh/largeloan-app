package com.xianjinxia.cashman.service.loan.risk;

import com.alibaba.fastjson.JSON;
import com.xianjinxia.cashman.constants.Constant;
import com.xianjinxia.cashman.constants.QueueConstants;
import com.xianjinxia.cashman.domain.LoanOrder;
import com.xianjinxia.cashman.dto.SmsDto;
import com.xianjinxia.cashman.dto.SpeedCardDto;
import com.xianjinxia.cashman.dto.SyncLoanOrderDto;
import com.xianjinxia.cashman.enums.*;
import com.xianjinxia.cashman.mapper.LoanOrderMapper;
import com.xianjinxia.cashman.mapper.RepaymentPlanMapper;
import com.xianjinxia.cashman.service.IMqMessageService;
import com.xianjinxia.cashman.service.ISmsService;
import com.xjx.mqclient.pojo.MqMessage;
import com.xjx.mqclient.service.MqClient;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class RiskCallbackImplByLoan extends RiskCallbackAbstract {

    private static final Logger logger = LoggerFactory.getLogger(RiskCallbackImplByLoan.class);

    @Autowired
    private LoanOrderMapper loanOrderMapper;

    @Autowired
    private RepaymentPlanMapper repaymentPlanMapper;

    @Autowired
    private MqClient mqClient;

    @Autowired
    private IMqMessageService mqMessageService;
    @Autowired
    private ISmsService smsService;
    @Override
    @Transactional
    public RiskCallbackResult callback(LoanOrder loanOrder, GroupRiskResultEnum groupRiskResultEnum) {
        int shoppingProductCategory = ProductCategoryEnum.PRODUCT_CATEGORY_SHOPPING.getCode().intValue();
        if (loanOrder.getProductCategory().intValue() == shoppingProductCategory){
            return new RiskCallbackResult(false);
        }

        TrdLoanOrderStatusEnum orderStatusEnum = groupRiskResultEnum.getOrderStatus();
        String updatedToStatus = orderStatusEnum.getCode();
        super.updateLoanOrderStatus(loanOrder.getId(), loanOrder.getTrdLoanOrderId(), loanOrder.getStatus(), updatedToStatus);

        List<MqMessage> messageList = new ArrayList<>();
        //使用提速卡,风控审核成功或者失败,通知cashman，扣提速卡
        if(StringUtils.isNotEmpty(loanOrder.getSpeedCardId())&&(groupRiskResultEnum.getCode()==GroupRiskResultEnum.REFUSED.getCode()||groupRiskResultEnum.getCode()==GroupRiskResultEnum.APPROVED.getCode())){
            logger.info("发送Mq消息给cashman,扣款提速卡,bizSeqNo为:"+loanOrder.getBizSeqNo()+" id为:"+loanOrder.getId());
            SpeedCardDto speedCardDto = new SpeedCardDto();
            speedCardDto.setUserId(loanOrder.getUserId());
            speedCardDto.setOrderNo(loanOrder.getId());
            speedCardDto.setSpeedCardId(loanOrder.getSpeedCardId());
            //风控审核拒绝
            if(groupRiskResultEnum.getCode()==GroupRiskResultEnum.REFUSED.getCode()){
                speedCardDto.setRiskResult(Constant.NO);
                //风控审核通过
            }else if(groupRiskResultEnum.getCode()==GroupRiskResultEnum.APPROVED.getCode()){
                speedCardDto.setRiskResult(Constant.YES);
                //把提速卡状态改成支付中
                loanOrderMapper.updateSpeedCardPayStatus(loanOrder.getId(), SpeedCardPayStatusEnum.SPEED_CARD_PAYING.getCode());
            }
            logger.info("风控审核后和支付中心交互的参数为:{}",JSON.toJSONString(speedCardDto));
            MqMessage speedCardMessage = new MqMessage(JSON.toJSONString(speedCardDto), QueueConstants.CASHMANAPP_SPEED_CARD_WITHHOLD);
            messageList.add(speedCardMessage);
        }

        // 发送MQ到trade-app， 同步订单的状态
        SyncLoanOrderDto syncLoanOrderDto = new SyncLoanOrderDto();
        syncLoanOrderDto.setLoanOrderId(loanOrder.getTrdLoanOrderId());
        syncLoanOrderDto.setStatus(updatedToStatus);
        syncLoanOrderDto.setProductCategory(loanOrder.getProductCategory());
        MqMessage syncLoanOrderMessage = new MqMessage(JSON.toJSONString(syncLoanOrderDto), QueueConstants.CASHMANAPP_SYNC_TRD_ORDER_STAUTS_TO_TRADE);
        messageList.add(syncLoanOrderMessage);
        mqMessageService.sendMessageList(messageList);

        //短信通知风控审核失败
        if(groupRiskResultEnum.getCode()==GroupRiskResultEnum.REFUSED.getCode()) {
            Boolean isSendShortMsgSuccess =false;
            //快借钱包的短信模板--目前和大额分期不一致
            if(null!=loanOrder.getMerchantNo()&&(MerchantNoEnum.MERCHANT_KJQB.getMerchantNo().equals(loanOrder.getMerchantNo()))){
                logger.info("商户号为{}，快借钱包发送审核失败短信",loanOrder.getMerchantNo());
                isSendShortMsgSuccess= smsService.sendSms(new SmsDto(loanOrder.getBizSeqNo(), loanOrder.getUserPhone(), new StringBuilder(MerchantNoEnum.MERCHANT_KJQB.getMerchantNoticePrex()).append(Constant.SMS_RISK_FAILED_ID).toString(),
                        "", null == loanOrder.getMerchantNo() ? "" : loanOrder.getMerchantNo()));
            }else{
                logger.info("非快借钱包发送审核失败短信");
                isSendShortMsgSuccess= smsService.sendSms(new SmsDto(loanOrder.getBizSeqNo(), loanOrder.getUserPhone(), Constant.SMS_RISK_FAILED_ID,
                        "", null == loanOrder.getMerchantNo() ? "" : loanOrder.getMerchantNo()));
            }
            if (!isSendShortMsgSuccess) {
                logger.warn("Send short msg for loan-check-fail failed,bizSeqNo is " + loanOrder.getBizSeqNo());
            }
        }
        return new RiskCallbackResult(true);
    }
}
