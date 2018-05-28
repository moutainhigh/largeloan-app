-- 每日凌晨1点发送还款统计数据到BE
INSERT INTO `scheduler`.`tb_job_desc` (`job_name`, `job_desc`, `job_cron`, `job_url`, `request_encoding`, `request_method`, `call_type`, `create_time`, `version`,`job_status`, `is_handle`) VALUES (
'SendStatisticInfoJob', '定时发送还款统计数据到BE', '0 0 1 * * ?','http://CASHMAN-APP/service/job/statistic-repayment-job', 'UTF-8', 'POST', 'EUREKA', now(),'2', '0', '0');
