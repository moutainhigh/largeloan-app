package com.xianjinxia.cashman.mapper;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.xianjinxia.cashman.CashmanApplication;
import com.xianjinxia.cashman.domain.ScheduleTaskRiskControlPush;
import com.xianjinxia.cashman.enums.TrdLoanOrderStatusEnum;

@RunWith(SpringRunner.class)
@SpringBootTest(
        classes = {CashmanApplication.class},
        properties = {"application.yml"},
        webEnvironment = SpringBootTest.WebEnvironment.MOCK
)
public class ScheduleTaskRiskControlPushMapperTest {
	@Autowired
	private ScheduleTaskRiskControlPushMapper scheduleTaskRiskControlPushMapper;
	
	@Test
    @Transactional
    @Rollback(false) 
    public void updateOrderStatus() throws Exception {
		List<ScheduleTaskRiskControlPush> list = scheduleTaskRiskControlPushMapper.selectRiskControlPushList(null, TrdLoanOrderStatusEnum.NEW.getCode());
		if(CollectionUtils.isEmpty(list)){
			return;
		}
		ScheduleTaskRiskControlPush scheduleTaskRiskControlPush = list.get(0);
		if(null == scheduleTaskRiskControlPush){
			return;
		}
		scheduleTaskRiskControlPushMapper.updateStatus(scheduleTaskRiskControlPush.getId(), TrdLoanOrderStatusEnum.NEW_PUSH_SUCCESS.getCode(), TrdLoanOrderStatusEnum.NEW.getCode(), "test");
	}
}

