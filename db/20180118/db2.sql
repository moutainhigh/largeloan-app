insert into `tb_mq_consumer` ( `consumer_name`, `consumer_desc`, `queue_name`, `consumer_url`, `url_params`, `request_encoding`, `request_method`, `call_type`, `retry_times`, `create_time`, `last_update_time`, `version`, `status`, `is_handle`)
values('cashmanapp_sync_order_status','同步订单状态','cashmanapp_sync_orderstatus_to_trade_queue','http://TRADE-APP//service/event/sync-order-status',NULL,'UTF-8','POST','EUREKA','7',now(),now(),'2','0','0');



INSERT  INTO tb_mq_consumer(consumer_name,consumer_desc,queue_name,consumer_url,request_encoding,request_method,call_type,status,is_handle,version)
VALUES ('cashmanapp-custodyloan-push','存管放款订单推送队列','cashmanapp_paycenter_custodyloan_queue','http://PAYMENTCENTER/api/kdcustody/custodyloan','UTF-8','POST','EUREKA',0,0,2);


INSERT  INTO tb_mq_consumer(consumer_name,consumer_desc,queue_name,consumer_url,request_encoding,request_method,call_type,status,is_handle,version)
VALUES ('cashmanapp-custodyloan-confirm','存管放款订单推送回调队列','custody_loan_push_queue','http://CASHMAN-APP/service/event/custodycallback','UTF-8','POST','EUREKA',0,0,2);

INSERT  INTO tb_mq_consumer(consumer_name,consumer_desc,queue_name,consumer_url,request_encoding,request_method,call_type,status,is_handle,version)
VALUES ('trade-custodyloan-confirm','存管放款订单推送回调trade队列','custody_loan_push_queue','http://TRADE-APP/service/event/pay-callback','UTF-8','POST','EUREKA',0,0,2);



INSERT INTO `tb_job_desc` (`job_name`, `job_desc`, `job_cron`, `job_url`, `url_params`, `request_encoding`, `request_method`, `call_type`, `create_time`, `last_update_time`,
`version`, `job_status`, `is_handle`)
VALUES ( 'custodyLoanPushJob', '存管推单', '0 0/2 * * * ?', 'http://CASHMAN-APP/service/job/custody-loan-push-job', '', 'UTF-8', 'POST', 'EUREKA', now(), now(), 2, 0, 0);


--job定时任务
INSERT INTO `scheduler`.`tb_job_desc` (`job_name`, `job_desc`, `job_cron`, `job_url`, `request_encoding`, `request_method`, `call_type`, `create_time`,`version`, `job_status`, `is_handle`) VALUES (
'SendUnfreezeNotice-1', '定时发送订单解冻短信请求', '0 30 9 * * ?', 'http://CASHMAN-APP/service/job/send-unfreeze-notice-job', 'UTF-8', 'POST', 'EUREKA', now(),'2', '0', '0');

INSERT INTO `scheduler`.`tb_job_desc` (`job_name`, `job_desc`, `job_cron`, `job_url`, `request_encoding`, `request_method`, `call_type`, `create_time`,`version`, `job_status`, `is_handle`) VALUES (
'RiskControlPush-1', '风控推送订单审核请求', '0 0/1 * * * ?', 'http://CASHMAN-APP/service/job/loan-control-push-job', 'UTF-8', 'POST', 'EUREKA', now(), 2, '0', '0');

INSERT INTO `scheduler`.`tb_job_desc` (`job_name`, `job_desc`, `job_cron`, `job_url`, `request_encoding`, `request_method`, `call_type`, `create_time`, `version`,`job_status`, `is_handle`) VALUES (
'SendTemindNotice-1', '定时发送每期到期前2日还款提醒短信请求', '0 30 9 * * ?','http://CASHMAN-APP/service/job/send-temind-notice-job', 'UTF-8', 'POST', 'EUREKA', now(),'2', '0', '0');

INSERT INTO `scheduler`.`tb_job_desc` (`job_name`, `job_desc`, `job_cron`, `job_url`, `request_encoding`, `request_method`, `call_type`, `create_time`, `version`,`job_status`, `is_handle`) VALUES (
'SendDueNotice-1', '定时发送每期到期日还款提醒短信请求', '0 30 10 * * ?', 'http://CASHMAN-APP/service/job/send-due-notice-job', 'UTF-8', 'POST', 'EUREKA', now(), '2','0', '0');

INSERT INTO `scheduler`.`tb_job_desc` (`job_name`, `job_desc`, `job_cron`, `job_url`, `request_encoding`, `request_method`, `call_type`, `create_time`, `version`,`job_status`, `is_handle`) VALUES (
'SendOverDueNotice-1', '定时发送逾期首日短信请求', '0 30 9 * * ?', 'http://CASHMAN-APP/service/job/send-over-due-notice-job', 'UTF-8', 'POST', 'EUREKA', now(), '2','0', '0');

INSERT INTO `scheduler`.`tb_job_desc` (`job_name`, `job_desc`, `job_cron`, `job_url`, `request_encoding`, `request_method`, `call_type`, `create_time`,`version`, `job_status`, `is_handle`) VALUES (
'UploadDepositContract-1', '生成存管协议并上传OSS请求', '0 0/5 * * * ?', 'http://CASHMAN-APP/service/job/upload-deposit-contract', 'UTF-8', 'POST', 'EUREKA', now(),'2', '0', '0');

INSERT INTO `scheduler`.`tb_job_desc` (`job_name`, `job_desc`, `job_cron`, `job_url`, `request_encoding`, `request_method`, `call_type`, `create_time`,`version`, `job_status`, `is_handle`) VALUES (
'PaymentRequestTimeOut-1', '取消过期的还款请求', '0/5 * * * * ?', 'http://CASHMAN-APP/service/job/payment-request-timeout-job', 'UTF-8', 'POST', 'EUREKA', now(),'2', '0', '0');


update tb_job_desc set job_url='http://CASHMAN-APP/service/job/upload-platform-contract',version=2,job_status=1,is_handle=0 where job_name ='UploadPlatFormContract-1';

update tb_job_desc set job_url='http://CASHMAN-APP/service/job/upload-loan-contract',version=2,job_status=1,is_handle=0 where job_name ='UploadLoanContract-1';

---添加定时代扣推支付中心（版本为1，因为传数据--String类型）
INSERT  INTO tb_mq_consumer(consumer_name,consumer_desc,queue_name,consumer_url,request_encoding,request_method,call_type,status,is_handle,version)
VALUES ('cashmanapp-paycenter-withhold-push','定时代扣推送支付中心队列','cashmanapp_paycenter_withhold_queue','http://PAYMENTCENTER/api/payment/mqRepayment','UTF-8','POST','EUREKA',0,0,1);
