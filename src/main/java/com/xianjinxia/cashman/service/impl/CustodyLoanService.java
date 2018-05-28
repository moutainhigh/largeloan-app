package com.xianjinxia.cashman.service.impl;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.xianjinxia.cashman.domain.Products;
import com.xianjinxia.cashman.enums.*;
import com.xianjinxia.cashman.exceptions.ServiceException;
import com.xianjinxia.cashman.constants.Constant;
import com.xianjinxia.cashman.constants.QueueConstants;
import com.xianjinxia.cashman.domain.LoanContract;
import com.xianjinxia.cashman.domain.LoanOrder;
import com.xianjinxia.cashman.domain.RepaymentPlan;
import com.xianjinxia.cashman.dto.*;
import com.xianjinxia.cashman.mapper.*;
import com.xianjinxia.cashman.remote.OldCashmanRemoteService;
import com.xianjinxia.cashman.response.LoanByMqCallbackDto;
import com.xianjinxia.cashman.service.ICustodyLoanService;
import com.xianjinxia.cashman.service.IMqMessageService;
import com.xianjinxia.cashman.service.ISmsService;
import com.xianjinxia.cashman.strategy.money.MoneyContext;
import com.xjx.mqclient.pojo.MqMessage;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Nullable;
import java.math.BigDecimal;
import java.util.*;

/**
 * @author whb
 * @date 2018/1/4.
 */
@Service
public class CustodyLoanService implements ICustodyLoanService {

    private static final Logger logger = LoggerFactory
    			.getLogger(CustodyLoanService.class);
    @Autowired
    IMqMessageService mqMessageService;
    @Autowired
    OldCashmanRemoteService oldCashmanRemoteService;
    @Autowired
    LoanOrderMapper loanOrderMapper;

    @Autowired
    RepaymentPlanMapper repaymentPlanMapper;
    @Autowired
    private ContractMapper contractMapper;
    @Autowired
    private LoanContractMapper loanContractMapper;

    @Autowired
    private ProductsMapper productsMapper;
    @Autowired
    private ISmsService smsService;

    @Autowired
    private MoneyContext moneyContext;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void uploadCustodyLoan(LoanOrder order) throws  Exception {
        Gson gson = new Gson();
        logger.info("推送支付中心放款,id="+order.getId()+" trdLoanId="+order.getTrdLoanOrderId());
        List<RepaymentPlan> repaymentPlans = repaymentPlanMapper.selectRepaymentPlanByLoanOrderId(order.getTrdLoanOrderId(), order.getProductId());
        if (repaymentPlans == null || repaymentPlans.size() == 0) {
            sendFailMsg(order,Constant.NONE_REPAYMENT_PLAN);
            return;
        }
        CustodyRequestParam param = buildParam(order,repaymentPlans);
        if (param == null) {
            sendFailMsg(order,Constant.NONE_USER_INFO);
            return;
        }
        MqMessage mqMessage = new MqMessage();
        mqMessage.setQueueName(QueueConstants.CUSTODY_LOAN_MQNAME);
        mqMessage.setMessage(gson.toJson(param));
        logger.info("存管放款推单待发送消息为 {}",gson.toJson(mqMessage));
        MqMessage tradeMsg = new MqMessage();
        tradeMsg.setQueueName(QueueConstants.CASHMANAPP_SYNC_TRD_ORDER_STAUTS_TO_TRADE);
        SyncLoanOrderDto SyncLoanOrderDto = new SyncLoanOrderDto();
        SyncLoanOrderDto.setLoanOrderId(order.getTrdLoanOrderId());
        SyncLoanOrderDto.setProductCategory(order.getProductCategory());
        SyncLoanOrderDto.setStatus(TrdLoanOrderStatusEnum.LOANING.getCode());
        logger.info("村管放款推单trade-app发送消息 {}",gson.toJson(SyncLoanOrderDto));
        tradeMsg.setMessage(gson.toJson(SyncLoanOrderDto));
        List<MqMessage> mqMessages = new ArrayList<>();
        mqMessages.add(mqMessage);
        mqMessages.add(tradeMsg);
        mqMessageService.sendMessageList(mqMessages);
        Map<String, Object> map = new HashMap<>();
        map.put("id", order.getId());
        map.put("prestatus", order.getStatus());
        map.put("status", TrdLoanOrderStatusEnum.LOANING.getCode());
        loanOrderMapper.updateselective(map);
    }

    private void sendFailMsg(LoanOrder order,String msg) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", order.getId());
        map.put("prestatus", order.getStatus());
        map.put("status", TrdLoanOrderStatusEnum.FAIL.getCode());
        map.put("remark", msg);
        loanOrderMapper.updateselective(map);
        MqMessage mqMessage = new MqMessage();
        SyncLoanOrderDto SyncLoanOrderDto = new SyncLoanOrderDto();
        SyncLoanOrderDto.setLoanOrderId(order.getTrdLoanOrderId());
        SyncLoanOrderDto.setProductCategory(order.getProductCategory());
        SyncLoanOrderDto.setStatus(TrdLoanOrderStatusEnum.FAIL.getCode());
        mqMessage.setMessage(new Gson().toJson(SyncLoanOrderDto));
        mqMessage.setQueueName(QueueConstants.CASHMANAPP_SYNC_TRD_ORDER_STAUTS_TO_TRADE);
        mqMessageService.sendMessage(mqMessage);
    }


    @Override
    public void resolveCallback(LoanByMqCallbackDto payCenterCallbackDto) {
        String bizSeqNo = payCenterCallbackDto.getPaymentOrderSeqNo();
        if (StringUtils.isBlank(bizSeqNo)) {
            logger.error("存管推单mq回调参数错误,业务id错误");
            throw new ServiceException("存管推单mq回调参数错误,订单号为空");
        }
        LoanOrder loanOrder = loanOrderMapper.selectByBizSeqNo(bizSeqNo);
        if (loanOrder == null) {
            logger.error("存管推单mq回调错误,未找到匹配的借款信息,biz_seq_no = {}",bizSeqNo);
            throw new ServiceException("存管推单mq回调错误,未找到匹配的借款信息,biz_seq_no = "+bizSeqNo);
        }
        logger.info("存管推单mq回调返回状态为 {}",payCenterCallbackDto.getCode());
        Map<String, Object> map = new HashMap<>();
        map.put("id",loanOrder.getId());
        map.put("prestatus", TrdLoanOrderStatusEnum.LOANING.getCode());
        if (payCenterCallbackDto.getBankPayTime() != null) {
            map.put("loanTime", payCenterCallbackDto.getBankPayTime());
        }
        Boolean isSendShortMsgSuccess=false;
        if (Constant.CUSTODYLOAN_SUCCESS.equals(payCenterCallbackDto.getCode())) {
            //放款成功,修改还款计划的时间为实际放款时间
            List<RepaymentPlan> list = moneyContext.calcPeriodData(loanOrder.getProductId(),loanOrder.getOrderAmount(), loanOrder.getPeriods(), new Date());
            for(int i=1;i<=loanOrder.getPeriods();i++) {
                RepaymentPlan repaymentPlan = list.get(i-1);
                logger.info("实际放款时重新计算的还款计划时间为:{},订单id为{}，期数为:{},用户id为:{}",repaymentPlan.getRepaymentPlanTime(),repaymentPlan.getLoanOrderId(),i,repaymentPlan.getUserId());
                repaymentPlanMapper.updateRepaymentPlanTime(loanOrder.getTrdLoanOrderId(),repaymentPlan.getRepaymentPlanTime(),i);
            }

            //放款成功,使用了提速卡,通知cashman更改提速卡状态
            if(StringUtils.isNotEmpty(loanOrder.getSpeedCardId())){
                SpeedCardNotifyDto speedCardNotifyDto = new SpeedCardNotifyDto();
                speedCardNotifyDto.setUserId(loanOrder.getUserId());
                speedCardNotifyDto.setOrderId(loanOrder.getId());
                speedCardNotifyDto.setSpeedCardId(Integer.parseInt(loanOrder.getSpeedCardId()));
                speedCardNotifyDto.setLoanResult(Constant.YES);
                logger.info(loanOrder.getId()+"放款成功通知cashman,参数为:{}",speedCardNotifyDto);
                MqMessage mqMessage = new MqMessage(JSON.toJSONString(speedCardNotifyDto), QueueConstants.CASHMANAPP_PAY_RESULT_QUEUE);
                mqMessageService.sendMessage(mqMessage);
            }
            //放款成功
            map.put("status", TrdLoanOrderStatusEnum.LOAN_SUCCESS.getCode());
            //添加数据--用户的合同数据
            List<ContractDto> contracts=contractMapper.selectByProductId(loanOrder.getProductId());
            for(ContractDto contract:contracts){
                LoanContract loanContract=new LoanContract(contract.getContractName(),
                        contract.getContractType(),loanOrder.getTrdLoanOrderId(),
                        loanOrder.getUserId(), LoanContractStatusEnum.NOT_HANDLE.getCode());

                loanContractMapper.insert(loanContract);
            }
            //短信通知放款成功
            //快借钱包的短信模板--目前和大额分期不一致
            if(null!=loanOrder.getMerchantNo()&&(MerchantNoEnum.MERCHANT_KJQB.getMerchantNo().equals(loanOrder.getMerchantNo()))){
                logger.info("商户号为{}，快借钱包发送放款成功短信",loanOrder.getMerchantNo());
                isSendShortMsgSuccess =smsService.sendSms(new SmsDto(loanOrder.getBizSeqNo(), loanOrder.getUserPhone(), new StringBuilder(MerchantNoEnum.MERCHANT_KJQB.getMerchantNoticePrex()).append(Constant.SMS_LOAN_SUCCESS_ID).toString(),
                        loanOrder.getLastFourBankCardNo(),null==loanOrder.getMerchantNo()?"":loanOrder.getMerchantNo()));
            }else {
                logger.info("非快借钱包发送放款成功短信");
                isSendShortMsgSuccess =smsService.sendSms(new SmsDto(loanOrder.getBizSeqNo(), loanOrder.getUserPhone(), Constant.SMS_LOAN_SUCCESS_ID,
                        loanOrder.getLastFourBankCardNo(),null==loanOrder.getMerchantNo()?"":loanOrder.getMerchantNo()));
            }
            if(!isSendShortMsgSuccess){
                logger.error("Send short msg for loan-success failed,bizSeqNo is "+loanOrder.getBizSeqNo());
            }
        } else {
            //放款失败,使用了提速卡,通知cashman更改提速卡状态
            if(StringUtils.isNotEmpty(loanOrder.getSpeedCardId())){
                SpeedCardNotifyDto speedCardNotifyDto = new SpeedCardNotifyDto();
                speedCardNotifyDto.setUserId(loanOrder.getUserId());
                speedCardNotifyDto.setOrderId(loanOrder.getId());
                speedCardNotifyDto.setSpeedCardId(Integer.parseInt(loanOrder.getSpeedCardId()));
                speedCardNotifyDto.setLoanResult(Constant.NO);
                logger.info(loanOrder.getId()+"放款失败通知cashman,参数为:{}",speedCardNotifyDto);
                MqMessage mqMessage = new MqMessage(JSON.toJSONString(speedCardNotifyDto), QueueConstants.CASHMANAPP_PAY_RESULT_QUEUE);
                mqMessageService.sendMessage(mqMessage);
            }

            //放款失败
            map.put("status", TrdLoanOrderStatusEnum.LOAN_FAIL.getCode());

            //放款失败把还款计划订单状态设置成已取消
            int repaymentNum = repaymentPlanMapper.updateStatus(loanOrder.getTrdLoanOrderId(), RepaymentPlanStatusEnum.Canceled.getCode());
            if(repaymentNum == 0){
                logger.error("repaymentNum=0,loanOrderId={}",loanOrder.getTrdLoanOrderId());
            }
            //短信通知放款失败
            //快借钱包的短信模板--目前和大额分期不一致
            if(null!=loanOrder.getMerchantNo()&&(MerchantNoEnum.MERCHANT_KJQB.getMerchantNo().equals(loanOrder.getMerchantNo()))){
                logger.info("快借钱包发送放款失败短信，商户号为",loanOrder.getMerchantNo());
                isSendShortMsgSuccess =smsService.sendSms(new SmsDto(loanOrder.getBizSeqNo(), loanOrder.getUserPhone(), new StringBuilder(MerchantNoEnum.MERCHANT_KJQB.getMerchantNoticePrex()).append(Constant.SMS_LOAN_FAILED_ID).toString(),
                        loanOrder.getLastFourBankCardNo(),null==loanOrder.getMerchantNo()?"":loanOrder.getMerchantNo()));
            }else {
                logger.info("非快借钱包发送放款成功短信");
                isSendShortMsgSuccess = smsService.sendSms(new SmsDto(loanOrder.getBizSeqNo(), loanOrder.getUserPhone(), Constant.SMS_LOAN_FAILED_ID,
                        String.valueOf(loanOrder.getOrderAmount()), null == loanOrder.getMerchantNo() ? "" : loanOrder.getMerchantNo()));
            }
            if(!isSendShortMsgSuccess){
                logger.error("Send short msg for loan-failed failed,bizSeqNo is "+loanOrder.getBizSeqNo());
            }
        }
        loanOrderMapper.updateselective(map);

    }


    private CustodyRequestParam buildParam(LoanOrder order,List<RepaymentPlan> list) {
        CustodyRequestParam param = new CustodyRequestParam();
        CustodyLoanUserInfo custodyLoanUserInfo = new CustodyLoanUserInfo();
        //获取用户信息（身份证号）
        UserDetailDto userInfo = oldCashmanRemoteService.getUserDetail(order.getUserId());
        if (userInfo == null) {
           logger.error("存管推单获取用户信息为空");
           return null;
        }
        custodyLoanUserInfo.setIdCardNo(userInfo.getIdNumber());
        CustodyRepayScheduleInfo custodyRepayScheduleInfo = new CustodyRepayScheduleInfo();
        custodyRepayScheduleInfo.setPeriod(order.getPeriods());
        custodyRepayScheduleInfo.setRepaymentAmount(order.getRepaymentAmount());
        custodyRepayScheduleInfo.setRepaymentInterest(order.getInterestAmount());
        custodyRepayScheduleInfo.setRepaymentPrincipal(order.getRepaymentAmount()-order.getInterestAmount());
//        List<CustodyRepayScheduleDtailInfo> repaymentPlans = order.getRepaymentPlans();
        if (list.size()==1) {
            // 获取最迟还款时间(最后一笔还款计划的应还时间)
            custodyRepayScheduleInfo.setRepaymentTime(list.get(0).getRepaymentPlanTime().getTime());
        } else {
            Collections.sort(list, new Comparator<RepaymentPlan>() {
                @Override
                public int compare(RepaymentPlan o1, RepaymentPlan o2) {
                    return o1.getPeriod()-o2.getPeriod();
                }
            });
            RepaymentPlan plan = list.get(list.size() - 1);
            custodyRepayScheduleInfo.setRepaymentTime(plan.getRepaymentPlanTime().getTime());
        }
        List<CustodyRepayScheduleDtailInfo> details = new ArrayList<>();
        details.addAll(Lists.transform(list, new Function<RepaymentPlan, CustodyRepayScheduleDtailInfo>() {
            @Nullable
            @Override
            public CustodyRepayScheduleDtailInfo apply(@Nullable RepaymentPlan input) {
                CustodyRepayScheduleDtailInfo detail = new CustodyRepayScheduleDtailInfo(order.getTermRate().multiply(new BigDecimal(100)));
                detail.setPeriod(String.valueOf(input.getPeriod()));
                detail.setPlanRepaymentTime(input.getRepaymentPlanTime().getTime());
                detail.setPlanRepaymentAmount(input.getRepaymentOriginAmount());
                detail.setPlanRepaymentPrincipal(input.getRepaymentPrincipalAmount());
                detail.setPlanRepaymentInterest(input.getRepaymentInterestAmount());
                return detail;
            }
        }));

        custodyRepayScheduleInfo.setRepaymentType(0);
        param.setBizId(order.getBizSeqNo());
        param.setBizType(Constant.BIZ_TYPE);
        param.setUserInfo(custodyLoanUserInfo);
        param.setRepaymentScheduleDetail(details);
        param.setRepaymentSchedule(custodyRepayScheduleInfo);
        param.setRouteStrategy(Constant.ROUTE_STRATEGY);
        param.setRequestSource(Constant.APPLICATION_PAYMENT_SOURCE);
        param.setOrderTime(order.getCreatedTime().getTime());
        // 传入的是单期利率 不是年利率
        param.setPerAnnumRate(order.getTermRate().multiply(new BigDecimal(100)));
        param.setLoanAmount(order.getPaymentAmount());
        Products products = productsMapper.selectById(order.getProductId());
        if (ProductTermTypeEnum.DAY.getCode().equals(order.getTermUnit())) {
            //按天
            param.setLoanMethod(Constant.LOAN_METHOD_DAY);
            param.setLoanTerm(products.getTerm());
        } else {
            //按月
            param.setLoanMethod(Constant.LOAN_METHOD_MONTH);
            param.setLoanTerm(order.getPeriods());
        }

        param.setLoanInterest(order.getInterestAmount());
        param.setMerchant(order.getMerchantNo());
        return param;
    }
}
