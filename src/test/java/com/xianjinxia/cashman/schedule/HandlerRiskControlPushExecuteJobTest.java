package com.xianjinxia.cashman.schedule;

import com.xianjinxia.cashman.enums.PlatformInterfaceEnum;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.github.pagehelper.Page;
import com.xianjinxia.cashman.CashmanApplication;
import com.xianjinxia.cashman.domain.RiskControlPushData;
import com.xianjinxia.cashman.domain.ScheduleTaskRiskControlPush;
import com.xianjinxia.cashman.dto.UserDetailDto;
import com.xianjinxia.cashman.enums.TrdLoanOrderStatusEnum;
import com.xianjinxia.cashman.mapper.ScheduleTaskRiskControlPushMapper;
import com.xianjinxia.cashman.remote.OldCashmanRemoteService;
import com.xianjinxia.cashman.remote.RiskOpenApiRemoteService;

@RunWith(SpringRunner.class)
@SpringBootTest(
        classes = {CashmanApplication.class},
        properties = {"application.yml"},
        webEnvironment = SpringBootTest.WebEnvironment.MOCK
)
public class HandlerRiskControlPushExecuteJobTest {

	private static final Logger logger = LoggerFactory.getLogger(HandlerRiskControlPushExecuteJobTest.class);

	@Autowired
	private ScheduleTaskRiskControlPushMapper scheduleTaskRiskControlPushMapper;

	@Autowired
	private HandlerRiskControlPushExecuteJob handlerRiskControlPushExecuteJob;
	
	@Autowired
	private RiskOpenApiRemoteService riskOpenApiRemoteService;
	
	@Autowired
	private OldCashmanRemoteService oldCashmanRemoteService;

	//@Test
	public void fetchTest() {

		Page<ScheduleTaskRiskControlPush> pages = (Page<ScheduleTaskRiskControlPush>) scheduleTaskRiskControlPushMapper
				.selectRiskControlPushList(null,
						TrdLoanOrderStatusEnum.NEW.getCode());

		logger.info("新申请数据：{}", pages.getResult().size());
	}

	@Test
	public void riskOpenApiTest() {
		RiskControlPushData riskControlPushData = new RiskControlPushData();
		riskControlPushData.setFaceImg("http://xjx-files.oss-cn-hangzhou.aliyuncs.com/files/20170329/20170329154047_0eo7gd5bca_appTh.png?Expires=1515599087&OSSAccessKeyId=LTAIGtLs9U0rENY6&Signature=LsNgB%2FCNo4uud1cRZQ5pG8bo3SQ%3D");
		riskControlPushData.setIdCard("532129199301221312");
		riskControlPushData.setIdCardBackImg("http://xjx-files.oss-cn-hangzhou.aliyuncs.com/files/20170329/20170329154117_e4bubc7p00_appTh.png?Expires=1515599087&OSSAccessKeyId=LTAIGtLs9U0rENY6&Signature=yecOWBf5ZqBBeGQE1DTaT9bPKbA%3D");
		riskControlPushData.setIdCardFrontImg("http://xjx-files.oss-cn-hangzhou.aliyuncs.com/files/20170329/20170329154138_kxt9vgw14a_appTh.png?Expires=1515599087&OSSAccessKeyId=LTAIGtLs9U0rENY6&Signature=QS2FWsFN%2BhbiV22K8wvt40DBfWY%3D");
		riskControlPushData.setOrderId(2111L);
		riskControlPushData.setPhoneNumber("18849890727");
		riskControlPushData.setUserId(962149L);
		riskControlPushData.setUserName("18849890727");
		riskControlPushData.setUserType("NEW");
		riskOpenApiRemoteService.riskControlPushResponse(riskControlPushData, PlatformInterfaceEnum.CREDIT_RISK_PUSH);
	}
	
	//@Test
	public void getUserDetailTest() {
		UserDetailDto detail = oldCashmanRemoteService.getUserDetail(962149L);
		logger.info(detail.toString());
	}

	//@Test
	public void execute() {
		handlerRiskControlPushExecuteJob.execute();
	}
}
