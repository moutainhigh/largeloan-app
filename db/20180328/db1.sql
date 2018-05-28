-- 第三方支付订单号
alter table cashman_payment_request add column third_order_no  VARCHAR(64) DEFAULT ''  COMMENT '第三方订单号';


-- 分期商城
INSERT INTO `products` (id, name, data_valid, slogan, product_category, min_amount, max_amount, term, term_type, min_periods, max_periods, quiet_period, interest_rate, overdue_rate, repay_method, updated_time, created_user, is_prepayment, is_renewal, start_valid_date, created_time, end_valid_date, is_depository, day_payment_max_amount) VALUES (2, '分期商城', 1, '分期商城描述', 3, 50000, 3000000, 30, '01', 1, 3, 30, null, null, '01', '2018-02-24 15:12:52', 'sys', 1, 0, null, '2018-01-24 13:39:20', null, '1', 0);

INSERT INTO `products_fee_config` (`id`,`data_valid`, `created_user`,`created_time`,`updated_user`,`updated_time`,`fee_name`,`fee_type`,`fee_rate`,`description`,`product_id`,`periods`) VALUES (10,'1', 'sys', '2017-12-22 18:05:22', 'sys', '2017-12-22 18:05:22', '利息费', 'interest_fee', '0.0300', '分期商城利息费（月利率）', '2', '1');
INSERT INTO `products_fee_config` (`id`,`data_valid`, `created_user`,`created_time`,`updated_user`,`updated_time`,`fee_name`,`fee_type`,`fee_rate`,`description`,`product_id`,`periods`) VALUES (11,'1', 'sys', '2017-12-22 18:05:22', 'sys', '2017-12-22 18:05:22', '利息费', 'interest_fee', '0.0300', '分期商城利息费（月利率）', '2', '2');
INSERT INTO `products_fee_config` (`id`,`data_valid`, `created_user`,`created_time`,`updated_user`,`updated_time`,`fee_name`,`fee_type`,`fee_rate`,`description`,`product_id`,`periods`) VALUES (12,'1', 'sys', '2017-12-22 18:05:22', 'sys', '2017-12-22 18:05:22', '利息费', 'interest_fee', '0.0300', '分期商城利息费（月利率）', '2', '3');
INSERT INTO `products_fee_config` (`id`,`data_valid`, `created_user`,`created_time`,`updated_user`,`updated_time`,`fee_name`,`fee_type`,`fee_rate`,`description`,`product_id`,`periods`) VALUES ('1', 'sys', '2017-12-22 18:05:22', 'sys', '2018-03-20 10:40:10', '逾期费', 'overdue_fee', '0.0060', '分期商城逾期费（日利率）', '2', '1');
INSERT INTO `products_fee_config` (`id`,`data_valid`, `created_user`,`created_time`,`updated_user`,`updated_time`,`fee_name`,`fee_type`,`fee_rate`,`description`,`product_id`,`periods`) VALUES ('1', 'sys', '2017-12-22 18:05:22', 'sys', '2018-03-20 10:40:10', '逾期费', 'overdue_fee', '0.0060', '分期商城逾期费（日利率）', '2', '2');
INSERT INTO `products_fee_config` (`id`,`data_valid`, `created_user`,`created_time`,`updated_user`,`updated_time`,`fee_name`,`fee_type`,`fee_rate`,`description`,`product_id`,`periods`) VALUES ('1', 'sys', '2017-12-22 18:05:22', 'sys', '2018-03-20 10:40:10', '逾期费', 'overdue_fee', '0.0060', '分期商城逾期费（日利率）', '2', '3');


