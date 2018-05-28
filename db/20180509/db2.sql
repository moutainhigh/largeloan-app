--还款当日job要跑3次--问题（原来是10:30和14:30现在要求为08：30和13:30和20:30怎么调整这个时间）
--还款前一日跑两次（08:30,13:30,20:30）
--还款日前两天（原来是9:30和13:30，现在要求08:30,13:30,20:30）

INSERT INTO `scheduler`.`tb_job_desc` (`job_name`, `job_desc`, `job_cron`, `job_url`, `request_encoding`, `request_method`, `call_type`, `create_time`, `version`,`job_status`, `is_handle`) VALUES (
'SendDayBeforeDueNotice-1', '定时发送还款前一日短信请求', '0 30 8 * * ?', 'http://CASHMAN-APP/service/job/send-day-before-due-notice-job', 'UTF-8', 'POST', 'EUREKA', now(), '2','0', '0');

INSERT INTO `scheduler`.`tb_job_desc` (`job_name`, `job_desc`, `job_cron`, `job_url`, `request_encoding`, `request_method`, `call_type`, `create_time`, `version`,`job_status`, `is_handle`) VALUES (
'SendDayBeforeDueNotice-2', '定时发送还款前一日短信请求', '0 30 13 * * ?', 'http://CASHMAN-APP/service/job/send-day-before-due-notice-job', 'UTF-8', 'POST', 'EUREKA', now(), '2','0', '0');

INSERT INTO `scheduler`.`tb_job_desc` (`job_name`, `job_desc`, `job_cron`, `job_url`, `request_encoding`, `request_method`, `call_type`, `create_time`, `version`,`job_status`, `is_handle`) VALUES (
'SendDayBeforeDueNotice-3', '定时发送还款前一日短信请求', '0 30 20 * * ?', 'http://CASHMAN-APP/service/job/send-day-before-due-notice-job', 'UTF-8', 'POST', 'EUREKA', now(), '2','0', '0');

INSERT INTO `scheduler`.`tb_job_desc` (`job_name`, `job_desc`, `job_cron`, `job_url`, `request_encoding`, `request_method`, `call_type`, `create_time`, `version`,`job_status`, `is_handle`) VALUES (
'SendDueNotice-3', '定时发送每期到期日还款提醒短信请求', '0 30 20 * * ?', 'http://CASHMAN-APP/service/job/send-due-notice-job', 'UTF-8', 'POST', 'EUREKA', now(), '2','0', '0');

INSERT INTO `scheduler`.`tb_job_desc` (`job_name`, `job_desc`, `job_cron`, `job_url`, `request_encoding`, `request_method`, `call_type`, `create_time`, `version`,`job_status`, `is_handle`) VALUES (
'SendTemindNotice-3', '定时发送每期到期前2日还款提醒短信请求', '0 30 20 * * ?','http://CASHMAN-APP/service/job/send-temind-notice-job', 'UTF-8', 'POST', 'EUREKA', now(),'2', '0', '0');

--定时发送每期到期前2日还款提醒短信请求
update tb_job_desc set `job_cron`='0 30 8 * * ?' ,job_status=1,is_handle=0 where job_name ='SendTemindNotice-1';
--到期前2日还款提醒只要一个10点钟的，其他不用了，删掉该任务
update tb_job_desc set `job_cron`='0 30 13 * * ?' ,job_status=1,is_handle=0 where job_name ='SendTemindNotice-2';
--定时发送每期到期日还款提醒短信请求
update tb_job_desc set `job_cron`='0 30 8 * * ?' ,job_status=1,is_handle=0 where job_name ='SendDueNotice-1';
update tb_job_desc set `job_cron`='0 30 13 * * ?' ,job_status=1,is_handle=0 where job_name ='SendDueNotice-2';

