-- cashman-app schedule jobs sql


INSERT INTO `scheduler`.`tb_job_desc` (`job_name`, `job_desc`, `job_cron`, `job_url`, `request_encoding`, `request_method`, `call_type`, `create_time`,`version`, `job_status`, `is_handle`) VALUES (
'WithholdScannerRequest-1', '定时代扣数据扫描请求', '0 0/5 * * * ?', 'http://CASHMAN-APP/service/job/withhold-scanner-job', 'UTF-8', 'POST', 'EUREKA', now(),'2', '0', '0');


INSERT INTO `scheduler`.`tb_job_desc` (`job_name`, `job_desc`, `job_cron`, `job_url`, `request_encoding`, `request_method`, `call_type`, `create_time`,`version`, `job_status`, `is_handle`) VALUES (
'WithholdExecuteRequest-1', '定时代扣数据执行请求', '0 0/5 * * * ?', 'http://CASHMAN-APP/service/job/withhold-execute-job', 'UTF-8', 'POST', 'EUREKA', now(),'2', '0', '0');


INSERT INTO `scheduler`.`tb_job_desc` (`job_name`, `job_desc`, `job_cron`, `job_url`, `request_encoding`, `request_method`, `call_type`, `create_time`, `version`,`job_status`, `is_handle`) VALUES (
'OverdueScannerRequest-1', '逾期数据扫描请求', '0 0/5 * * * ?', 'http://CASHMAN-APP/service/job/overdue-scanner-job', 'UTF-8', 'POST', 'EUREKA', now(), '2','0', '0');


INSERT INTO `scheduler`.`tb_job_desc` (`job_name`, `job_desc`, `job_cron`, `job_url`, `request_encoding`, `request_method`, `call_type`, `create_time`,`version`, `job_status`, `is_handle`) VALUES (
'OverdueCalculateRequest-1', '逾期费用计算请求', '0 0/5 * * * ?', 'http://CASHMAN-APP/service/job/overdue-calculate-job', 'UTF-8', 'POST', 'EUREKA', now(),'2', '0', '0');


INSERT INTO `scheduler`.`tb_job_desc` (`job_name`, `job_desc`, `job_cron`, `job_url`, `request_encoding`, `request_method`, `call_type`, `create_time`,`version`, `job_status`, `is_handle`) VALUES (
'CollectionNotifyRequest-1', '逾期订单通知催收请求', '0 0/5 * * * ?', 'http://CASHMAN-APP/service/job/collection-notify-job', 'UTF-8', 'POST', 'EUREKA', now(),'2','0', '0');


INSERT INTO `scheduler`.`tb_job_desc` (`job_name`, `job_desc`, `job_cron`, `job_url`, `request_encoding`, `request_method`, `call_type`, `create_time`,`version`, `job_status`, `is_handle`) VALUES (
'WatchAndDelTimeoutLock-1', '删除超时锁请求', '0 0/5 * * * ?', 'http://CASHMAN-APP/service/job/watch-lock', 'UTF-8', 'POST', 'EUREKA', now(),'2','0', '0');


INSERT INTO `scheduler`.`tb_job_desc` (`job_name`, `job_desc`, `job_cron`, `job_url`, `request_encoding`, `request_method`, `call_type`, `create_time`,`version`, `job_status`, `is_handle`) VALUES (
'UploadPlatFormContract-1', '生成平台合同并上传OSS请求', '0 0/5 * * * ?', 'http://CASHMAN-APP/service/job/upload-platform-contract', 'UTF-8', 'POST', 'EUREKA', now(),'2', '0', '0');


INSERT INTO `scheduler`.`tb_job_desc` (`job_name`, `job_desc`, `job_cron`, `job_url`, `request_encoding`, `request_method`, `call_type`, `create_time`,`version`, `job_status`, `is_handle`) VALUES (
'UploadLoanContract-1', '生成借款合同并上传OSS请求', '0 0/5 * * * ?', 'http://CASHMAN-APP/service/job/upload-loan-contract', 'UTF-8', 'POST', 'EUREKA', now(),'2','0', '0');
