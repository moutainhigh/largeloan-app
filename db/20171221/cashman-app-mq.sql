-- cashman-app mq sql

-- 同一笔借款订单的所有还款计划都已还清，通知trade-app系统更改订单的状态
INSERT INTO `mqtask`.`tb_mq_consumer` (`consumer_name`, `consumer_desc`, `queue_name`, `consumer_url`, `url_params`, `request_encoding`, `request_method`, `call_type`, `retry_times`, `create_time`, `version`, `status`, `is_handle`)
VALUES ('tradeapp_loan_order_repaymented_queue', '借款已还清，由cashman-app通知trade-app修改借款单的状态', 'tradeapp_loan_order_repaymented_queue', 'http://TRADE_APP/service/loan/loanRepayStatus-confirm', NULL, 'UTF-8', 'POST', 'EUREKA', '7', NOW(),'2', '0', '0');

-- 逾期未还款，通知催收系统
INSERT INTO `mqtask`.`tb_mq_consumer` (`consumer_name`, `consumer_desc`, `queue_name`, `consumer_url`, `url_params`, `request_encoding`, `request_method`, `call_type`, `retry_times`, `create_time`,`version`,  `status`, `is_handle`)
VALUES ('collection_repayment_plan_overdue_notify', '用户逾期未完成还款，由cashman-app通知collection系统进行催收', 'collection_repayment_plan_overdue_notify', 'http://COLLECTION/service/loan/loanRepayStatus-confirm', NULL, 'UTF-8', 'POST', 'HTTP', '7', NOW(),'2', '0', '0');

-- 逾期已还款，通知催收系统关闭订单
INSERT INTO `mqtask`.`tb_mq_consumer` (`consumer_name`, `consumer_desc`, `queue_name`, `consumer_url`, `url_params`, `request_encoding`, `request_method`, `call_type`, `retry_times`, `create_time`, `version`, `status`, `is_handle`)
VALUES ('collection_repayment_plan_overdue_repay', '用户还款(针对逾期订单)成功，由cashman-app通知collection关闭催收订单', 'collection_repayment_plan_overdue_repay', 'http://COLLECTION/service/loan/loanRepayStatus-confirm', NULL, 'UTF-8', 'POST', 'HTTP', '7', NOW(),'2', '0', '0');


INSERT INTO `tb_mq_consumer` (`consumer_name`, `consumer_desc`, `queue_name`, `consumer_url`, `url_params`, `request_encoding`, `request_method`, `call_type`, `retry_times`, `create_time`,`version`, `status`, `is_handle`)
VALUES ('cashman_app_withholding_success_queue', '还款成功的回调通知', 'withholding_success_queue', 'http://CASHMAN-APP/service/event/repay-confirm', NULL, 'UTF-8', 'POST', 'EUREKA', '7', now(), '2', '0', '0');
INSERT INTO `tb_mq_consumer` (`consumer_name`, `consumer_desc`, `queue_name`, `consumer_url`, `url_params`, `request_encoding`, `request_method`, `call_type`, `retry_times`, `create_time`,`version`, `status`, `is_handle`)
VALUES ('cashman_app_withholding_failed_queue', '还款失败的回调通知', 'withholding_failed_queue', 'http://CASHMAN-APP/service/event/repay-confirm', NULL, 'UTF-8', 'POST', 'EUREKA', '7', now(), '2', '0', '0');
-- 还清后Trade-App修改借款单状态的通知
INSERT INTO `tb_mq_consumer` (`consumer_name`, `consumer_desc`, `queue_name`, `consumer_url`, `url_params`, `request_encoding`, `request_method`, `call_type`, `retry_times`, `create_time`,`version`, `status`, `is_handle`) 
VALUES ('trade_app_loan_order_repaymented_queue', '还清后修改借款状态的通知', 'trade_app_loan_order_repaymented_queue', 'http://TRADE-APP/service/event/loanRepaymentSuccess', NULL, 'UTF-8', 'POST', 'EUREKA', '7', now(), '2', '0', '0');
