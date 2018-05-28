package com.xianjinxia.cashman.service.impl;

import com.xianjinxia.cashman.constants.QueueConstants;
import com.xianjinxia.cashman.domain.LoanOrder;
import com.xianjinxia.cashman.dto.QuotaGiveBackDto;
import com.xianjinxia.cashman.dto.SyncLoanOrderDto;
import com.xianjinxia.cashman.enums.*;
import com.alibaba.fastjson.JSON;
import com.xianjinxia.cashman.mapper.LoanOrderMapper;
import com.xianjinxia.cashman.mapper.RepaymentPlanMapper;
import com.xjx.mqclient.pojo.MqMessage;
import com.xjx.mqclient.service.MqClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xianjinxia.cashman.domain.RiskControlPushData;
import com.xianjinxia.cashman.domain.ScheduleTaskRiskControlPush;
import com.xianjinxia.cashman.dto.UserDetailDto;
import com.xianjinxia.cashman.mapper.ScheduleTaskRiskControlPushMapper;
import com.xianjinxia.cashman.remote.RiskOpenApiRemoteService;
import com.xianjinxia.cashman.response.OpenApiBaseResponse;
import com.xianjinxia.cashman.service.ILoanOrderService;
import com.xianjinxia.cashman.service.IRiskControlPushService;

import java.util.ArrayList;
import java.util.List;

@Service
public class RiskControlPushServiceImpl implements IRiskControlPushService {
	
    private static final Logger logger = LoggerFactory.getLogger(RiskControlPushServiceImpl.class);
	
    @Autowired
    private ScheduleTaskRiskControlPushMapper scheduleTaskRiskControlPushMapper;

    @Autowired
    private RiskOpenApiRemoteService riskOpenApiRemoteService;
    
    @Autowired
    private ILoanOrderService loanOrderService;

    @Autowired
    private LoanOrderMapper loanOrderMapper;

	@Autowired
    private RepaymentPlanMapper repaymentPlanMapper;

    @Autowired
    private MqClient mqClient;

	@Transactional
	public void pushToRisk(ScheduleTaskRiskControlPush scheduleTaskRiskControlPush, UserDetailDto userDetailDto ) {
		
		Integer productCategory = scheduleTaskRiskControlPush.getProductCategory();
		Long id = scheduleTaskRiskControlPush.getId();
		LoanOrder loanOrder = loanOrderMapper.selectByPrimaryKey(id);


		Long trdLoanOrderId = scheduleTaskRiskControlPush.getTrdLoanOrderId();
		if(null == userDetailDto){
			scheduleTaskRiskControlPushMapper.updateStatus(id, TrdLoanOrderStatusEnum.FAIL.getCode(), TrdLoanOrderStatusEnum.NEW.getCode(), "后台风控推送job调用OldCashman获取用户信息时,返回数据为null,无效的用户");
			loanOrderService.sendMessage(trdLoanOrderId, TrdLoanOrderStatusEnum.FAIL.getCode(), productCategory);
			return;
		}
		
		RiskControlPushData riskControlPushData = getRiskControlPushData(scheduleTaskRiskControlPush, userDetailDto);

		OpenApiBaseResponse<Void> baseResponse = null;
		if (scheduleTaskRiskControlPush.getProductCategory().intValue() == ProductCategoryEnum.PRODUCT_CATEGORY_SHOPPING.getCode().intValue()){
			baseResponse = riskOpenApiRemoteService.riskControlPushResponse(riskControlPushData, PlatformInterfaceEnum.CREDIT_SHOPPING_RISK_PUSH);
		}
		if (scheduleTaskRiskControlPush.getProductCategory().intValue() == ProductCategoryEnum.PRODUCT_CATEGORY_BIG.getCode().intValue()){
			baseResponse = riskOpenApiRemoteService.riskControlPushResponse(riskControlPushData, PlatformInterfaceEnum.CREDIT_RISK_PUSH);
		}

		
		//响应对象为null,不作任何处理,调度后续再次推送
		if(null == baseResponse || baseResponse.getCode().equals(OpenApiBaseResponse.ResponseCode.SYS_ERROR_NEED_RETRY.getValue())){
			logger.error("订单需要重新推送,openAPI返回重新推送状态,订单编号:"+ trdLoanOrderId);
			return;
		}
		
		boolean isSuccess = baseResponse.getCode().equals(OpenApiBaseResponse.ResponseCode.SUCCESS.getValue());
		
		String remark = String.format("后台风控推送job,推送订单给集团风控,推送成功,code:%s,msg:%s", baseResponse.getCode(), baseResponse.getMsg());
		
		String status = TrdLoanOrderStatusEnum.NEW_PUSH_SUCCESS.getCode();

		List<MqMessage> messageList = new ArrayList<>();

		if(!isSuccess){
			status = TrdLoanOrderStatusEnum.PUSH_FAIL.getCode();
			remark = String.format("后台风控推送job,风控推送失败,code:%s,msg:%s", baseResponse.getCode(), baseResponse.getMsg());
            logger.info("remark:{}",remark);

			int count  = repaymentPlanMapper.updateStatus(loanOrder.getTrdLoanOrderId(), RepaymentPlanStatusEnum.Canceled.getCode());
			logger.info("更新还款计划状态为40，count={},loanOrderId={}",count,loanOrder.getTrdLoanOrderId());
			// 归还用户额度
			QuotaGiveBackDto quotaGiveBackDto = new QuotaGiveBackDto(loanOrder.getUserId(), loanOrder.getOrderAmount(),loanOrder.getTraceNo(),ProductCategoryEnum.PRODUCT_CATEGORY_BIG.getCode());
			logger.info("支付回调：发送消息给cashman，恢复用户额度：{}", quotaGiveBackDto.toString());
			MqMessage quotaGiveBackMessage = new MqMessage(JSON.toJSONString(quotaGiveBackDto), QueueConstants.USER_AVAILABLE_AMOUNT_MQ_NAME);
			messageList.add(quotaGiveBackMessage);

		}
		
		logger.info(remark);
		scheduleTaskRiskControlPushMapper.updateStatus(id, status, TrdLoanOrderStatusEnum.NEW.getCode(), remark);

		SyncLoanOrderDto syncLoanOrderDto = new SyncLoanOrderDto();
		syncLoanOrderDto.setLoanOrderId(trdLoanOrderId);
		syncLoanOrderDto.setStatus(status);
		syncLoanOrderDto.setProductCategory(productCategory);
		MqMessage message = new MqMessage(JSON.toJSONString(syncLoanOrderDto), QueueConstants.CASHMANAPP_SYNC_TRD_ORDER_STAUTS_TO_TRADE);



		messageList.add(message);

		mqClient.sendMessagesByCommit(messageList, true);
	}

	private RiskControlPushData getRiskControlPushData(ScheduleTaskRiskControlPush scheduleTaskRiskControlPush, UserDetailDto userDetailDto) {
		RiskControlPushData riskControlPushData = new RiskControlPushData();
		riskControlPushData.setFaceImg(userDetailDto.getHeadPortrait());//人脸识别图片
		riskControlPushData.setIdCard(userDetailDto.getIdNumber());
		riskControlPushData.setIdCardFrontImg(userDetailDto.getIdcardImgZ());//身份证正面照片
		riskControlPushData.setIdCardBackImg(userDetailDto.getIdcardImgF());//身份证反面照片
		riskControlPushData.setOrderId(scheduleTaskRiskControlPush.getId());
		riskControlPushData.setPhoneNumber(userDetailDto.getUserPhone());
		riskControlPushData.setUserId(scheduleTaskRiskControlPush.getUserId());
		riskControlPushData.setUserName(userDetailDto.getRealname());
		riskControlPushData.setUserType(scheduleTaskRiskControlPush.getUserType() ? UserTypeEnum.OLD.getName() : UserTypeEnum.NEW.getName());// 0、新用户；1；老用户
		riskControlPushData.setBankCard(userDetailDto.getBankCard());
		return riskControlPushData;
	}

}
