package com.xianjinxia.cashman.service;

import com.xianjinxia.cashman.domain.ScheduleTaskRiskControlPush;
import com.xianjinxia.cashman.dto.UserDetailDto;

/**
 * 风控推送service
 * 
 * @author chunliny
 *
 */
public interface IRiskControlPushService {
	void pushToRisk(ScheduleTaskRiskControlPush scheduleTaskRiskControlPush, UserDetailDto userDetailDto);
}
