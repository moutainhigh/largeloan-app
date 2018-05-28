package com.xianjinxia.cashman.schedule;

import java.util.List;

import com.alibaba.fastjson.JSON;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.github.pagehelper.Page;
import com.xianjinxia.cashman.domain.ScheduleTaskRiskControlPush;
import com.xianjinxia.cashman.dto.UserDetailDto;
import com.xianjinxia.cashman.enums.TrdLoanOrderStatusEnum;
import com.xianjinxia.cashman.mapper.ScheduleTaskRiskControlPushMapper;
import com.xianjinxia.cashman.remote.OldCashmanRemoteService;
import com.xianjinxia.cashman.schedule.job.PagebleScanCollectionJob;
import com.xianjinxia.cashman.service.IRiskControlPushService;

@Component
public class HandlerRiskControlPushExecuteJob extends PagebleScanCollectionJob<ScheduleTaskRiskControlPush, List<ScheduleTaskRiskControlPush>> {
	
    private static Logger logger = LoggerFactory.getLogger(HandlerRiskControlPushExecuteJob.class);

    @Autowired
    private ScheduleTaskRiskControlPushMapper scheduleTaskRiskControlPushMapper;
    
    @Autowired
    private IRiskControlPushService riskControlPushService;
    
    @Autowired
    private OldCashmanRemoteService oldCashmanRemoteService;

    @Override
    public void process(List<ScheduleTaskRiskControlPush> item) {
    	if(CollectionUtils.isEmpty(item)){
    		return;
    	}
        for (ScheduleTaskRiskControlPush scheduleTaskRiskControlPush : item) {
			try {
				UserDetailDto userDetailDto = oldCashmanRemoteService.getUserDetail(scheduleTaskRiskControlPush.getUserId());
                String bankCardNo = oldCashmanRemoteService.getBankCardNoById(scheduleTaskRiskControlPush.getUserBankCardId());
                logger.info("查询到用户银行卡号为：{}",bankCardNo);
                userDetailDto.setBankCard(bankCardNo);
                logger.info("推送风控参数的的json为:{}", JSON.toJSONString(userDetailDto));
				riskControlPushService.pushToRisk(scheduleTaskRiskControlPush, userDetailDto);
			} catch (Exception ex) {
				logger.error(String.format("风控推送订单异常,订单号=%s,用户编号=%s", scheduleTaskRiskControlPush.getId(), scheduleTaskRiskControlPush.getUserId()), ex);
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
    public Page<ScheduleTaskRiskControlPush> fetch() {
        Page<ScheduleTaskRiskControlPush> pages = (Page<ScheduleTaskRiskControlPush>) scheduleTaskRiskControlPushMapper.selectRiskControlPushList(null, TrdLoanOrderStatusEnum.NEW.getCode());
        return pages;
    }
}