package com.xianjinxia.cashman.controller;

import com.dianping.cat.common.EventInfo;
import com.dianping.cat.utils.CatUtils;
import com.xianjinxia.cashman.response.mqapp.ResultMsg;
import com.xianjinxia.cashman.schedule.*;
import com.xianjinxia.cashman.schedule.job.AbstractJob;
import com.xianjinxia.cashman.schedule.lock.MysqlLock;
import com.xianjinxia.cashman.utils.DateUtil;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 执行定时任务的controller
 * <p>
 * <pre>
 *      1.逾期数据扫描任务
 *      2.逾期费用计算任务
 *      3.定时代扣扫描任务
 *      4.定时代扣计算任务
 *      5.通知催收系统的任务
 *      6.删除超时锁的任务
 *
 * </pre>
 *
 * @author zhangyongjia  zyj@xianjinxia.com
 */
@Api(tags = "cashman_app job controller")
@RequestMapping("/service/job")
@RestController
public class JobController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(JobController.class);

    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;
    @Autowired
    private HandlerOverduePrepareJob handlerOverduePrepareJob;
    @Autowired
    private HandlerOverdueExecuteJob handlerOverdueExecuteJob;
    @Autowired
    private HandlerWithHoldPrepareJob handlerWithHoldPrepareJob;
    @Autowired
    private HandlerWithHoldExecuteJob handlerWithHoldExecuteJob;
    @Autowired
    private HandlerCollectionNotifyJob handlerCollectionNotifyJob;

    @Autowired
    private HandlerLoanContractUploadJob handlerLoanContractUploadJob;
    @Autowired
    private HandlerPlatformContractUploadJob handlerPlatformContractUploadJob;
    @Autowired
    private HandlerPaymentRequestTimeoutJob handlerPaymentRequestTimeoutJob;
    @Autowired
    private HandlerUnfreezeNoticeJob handlerUnfreezeNoticeJob;
    @Autowired
    private HandlerRiskControlPushExecuteJob handlerRiskControlPushExecuteJob;
    @Autowired
    private HandlerOverDueNoticeJob handlerOverDueNoticeJob;
    @Autowired
    private HandlerRepaymentRemindNoticeJob handlerRepaymentRemindNoticeJob;
    @Autowired
    private HandlerDueDateNoticeJob handlerDueDateNoticeJob;
    @Autowired
    private HandlerDepositContractUploadJob handlerDepositContractUploadJob;
    @Autowired
    private MysqlLock mysqlLock;

    @Autowired
    private HandlerCustodyLoanJob handlerCustodyLoanJob;
    @Autowired
    private HandlerLoanStatusPayFailJob handlerLoanStatusPayFailJob;

    @Autowired
    private HandlerStatisticRepaymentJob handlerStatisticRepaymentJob;
    @Autowired
    private HandlerDayBeforeDueNoticeJob handlerDayBeforeDueNoticeJob;
    @PostMapping("/withhold-scanner-job")
    public ResultMsg withholdScannerTask() {
        return this.executeJob(handlerWithHoldPrepareJob);
    }

    @PostMapping("/withhold-execute-job")
    public ResultMsg withholdTimerTask() {
        return this.executeJob(handlerWithHoldExecuteJob);
    }

    @PostMapping("/overdue-scanner-job")
    public ResultMsg overdueScannerTask() {
        return this.executeJob(handlerOverduePrepareJob);
    }

    @PostMapping("/overdue-calculate-job")
    public ResultMsg overdueCalculateTask() {
        return this.executeJob(handlerOverdueExecuteJob);
    }

    @PostMapping("/collection-notify-job")
    public ResultMsg collectionNotifyTask() {
        return this.executeJob(handlerCollectionNotifyJob);
    }

    @PostMapping("/upload-loan-contract")
    public ResultMsg uploadLoanContract(){
        return this.executeJob(handlerLoanContractUploadJob);
    }

    @PostMapping("/upload-platform-contract")
    public ResultMsg uploadPlatformContract(){
        return this.executeJob(handlerPlatformContractUploadJob);
    }

    @PostMapping("/upload-deposit-contract")
    public ResultMsg uploadDepositContract(){
        return this.executeJob(handlerDepositContractUploadJob);
    }

    @PostMapping("/payment-request-timeout-job")
    public ResultMsg handlerPaymentRequestTimeoutJob(){
        return this.executeJob(handlerPaymentRequestTimeoutJob);
    }

    @PostMapping("/send-unfreeze-notice-job")
    public ResultMsg handlerUnfreezeNoticeJob(){
        return this.executeJob(handlerUnfreezeNoticeJob);
    }
    @PostMapping("/send-over-due-notice-job")
    public ResultMsg handlerOverDueNoticeJob(){
        return this.executeJob(handlerOverDueNoticeJob);
    }
    @PostMapping("/send-temind-notice-job")
    public ResultMsg handlerTemindNoticeJob(){
        return this.executeJob(handlerRepaymentRemindNoticeJob);
    }
    @PostMapping("/send-due-notice-job")
    public ResultMsg handlerDueNoticeJob(){
        return this.executeJob(handlerDueDateNoticeJob);
    }

	@PostMapping("/loan-control-push-job")
	public ResultMsg riskControlPushJob() {
		return this.executeJob(handlerRiskControlPushExecuteJob);
	}

	@PostMapping("/custody-loan-push-job")
    public ResultMsg custodyLoanPushJob() {
        return this.executeJob(handlerCustodyLoanJob);
    }

    @PostMapping("/loan-status-pay-fail-job")
    public ResultMsg handlerStatusPayFailJob() {
        return this.executeJob(handlerLoanStatusPayFailJob);
    }

    @PostMapping("/statistic-repayment-job")
    public ResultMsg handlerStatisticRepaymentJob() {
        return this.executeJob(handlerStatisticRepaymentJob);
    }

    @PostMapping("/send-day-before-due-notice-job")
    public ResultMsg handlerDayBeforeDueNoticeJob() {
        return this.executeJob(handlerDayBeforeDueNoticeJob);
    }

    // template method
    private ResultMsg executeJob(AbstractJob abstractJob){
        // CAT日志系统无法把日志对应到具体的线程池中
        try {
            new Thread(new Runnable() {

                @Override
                public void run() {
                    EventInfo event = new EventInfo();
                    event.put("now", DateUtil.yyyyMMdd());
                    event.setEventType(abstractJob.getClass().getSimpleName());
                    CatUtils.info(event);
                    abstractJob.execute();
                }

            }).start();
        }catch (Exception exception) {
            logger.error("检查任务，请检查原因：1.调度过于频繁，导致ThreadPool拒收请求 2.JobLock锁未释放", exception);
            return innerErrorResp();
        }

//            threadPoolTaskExecutor.execute(new Runnable() {
//                @Override
//                public void run(){
//                    EventInfo event = new EventInfo();
//                    event.put("now", DateUtil.yyyyMMdd());
//                    event.setEventType(abstractJob.getClass().getSimpleName());
//                    CatUtils.info(event);
//                    abstractJob.execute();
//                }
//            });


        return successResp();
    }
}