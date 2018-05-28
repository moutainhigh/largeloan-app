INSERT INTO `tb_job_desc` (`job_name`, `job_desc`, `job_cron`, `job_url`, `url_params`, `request_encoding`, `request_method`, `call_type`, `create_time`, `last_update_time`,
`version`, `job_status`, `is_handle`)
VALUES ( 'loanStatusPayFailJob', '提速卡支付失败更改订单状态', '0 0/30 * * * ?', 'http://CASHMAN-APP/service/job/loan-status-pay-fail-job', '', 'UTF-8', 'POST', 'EUREKA', now(), now(), 2, 0, 0);

--产品要求每天发两次短信，需要添加两个一样的job
INSERT INTO `scheduler`.`tb_job_desc` (`job_name`, `job_desc`, `job_cron`, `job_url`, `request_encoding`, `request_method`, `call_type`, `create_time`, `version`,`job_status`, `is_handle`) VALUES (
'SendTemindNotice-2', '定时发送每期到期前2日还款提醒短信请求', '0 30 13 * * ?','http://CASHMAN-APP/service/job/send-temind-notice-job', 'UTF-8', 'POST', 'EUREKA', now(),'2', '0', '0');

INSERT INTO `scheduler`.`tb_job_desc` (`job_name`, `job_desc`, `job_cron`, `job_url`, `request_encoding`, `request_method`, `call_type`, `create_time`, `version`,`job_status`, `is_handle`) VALUES (
'SendDueNotice-2', '定时发送每期到期日还款提醒短信请求', '0 30 14 * * ?', 'http://CASHMAN-APP/service/job/send-due-notice-job', 'UTF-8', 'POST', 'EUREKA', now(), '2','0', '0');

INSERT INTO `scheduler`.`tb_job_desc` (`job_name`, `job_desc`, `job_cron`, `job_url`, `request_encoding`, `request_method`, `call_type`, `create_time`, `version`,`job_status`, `is_handle`) VALUES (
'SendOverDueNotice-2', '定时发送逾期首日短信请求', '0 30 12 * * ?', 'http://CASHMAN-APP/service/job/send-over-due-notice-job', 'UTF-8', 'POST', 'EUREKA', now(), '2','0', '0');


insert into `tb_mq_consumer` ( `consumer_name`, `consumer_desc`, `queue_name`, `consumer_url`, `url_params`, `request_encoding`, `request_method`, `call_type`, `retry_times`, `create_time`, `last_update_time`, `version`, `status`, `is_handle`)
values('cashmanapp_speed_card_result_queue','提速卡扣款结果通知','cashmanapp_speed_card_result_queue','http://CASHMAN-APP/service/event/speed-card-callback',NULL,'UTF-8','POST','EUREKA','7',now(),now(),'2','0','0');
