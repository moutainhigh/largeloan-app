insert into `tb_mq_consumer` ( `consumer_name`, `consumer_desc`, `queue_name`, `consumer_url`, `url_params`, `request_encoding`, `request_method`, `call_type`, `retry_times`, `create_time`, `last_update_time`, `version`, `status`, `is_handle`)
values('cashmanapp_speed_card_result_queue','提速卡扣款结果通知','cashmanapp_speed_card_result_queue','http://CASHMAN-APP/service/event/speed-card-callback',NULL,'UTF-8','POST','EUREKA','7',now(),now(),'2','0','0');