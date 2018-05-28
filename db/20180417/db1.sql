--还款计划表：新增字段:最近一次更新逾期费用时间、初始化本金、初始化利息
alter table cashman_repayment_plan add column overdue_cal_time DATE DEFAULT NULL  COMMENT '最近一次更新逾期费用时间' AFTER overdue_fee_amount;
alter table cashman_repayment_plan add column repayment_origin_principal_amount INT(10) unsigned NOT NULL DEFAULT '0'  COMMENT '初始化借款本金' AFTER repayment_origin_amount;
alter table cashman_repayment_plan add column repayment_origin_interest_amount INT(10) unsigned NOT NULL DEFAULT '0' COMMENT '初始化借款利息' AFTER repayment_origin_principal_amount;

CREATE TABLE `cashman_overdue_calc_log` (
  `id` bigint(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `repayment_plan_id` bigint(11) NOT NULL COMMENT '还款计划ID',
  `overdue_cal_time` date NOT NULL COMMENT '日罚息计算日期',
  `status` tinyint(1) NOT NULL DEFAULT '0' COMMENT '日志状态 0 初始入库  1 日罚息计算错误',
  `created_time` datetime NOT NULL COMMENT '创建时间',
  `updated_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `data_valid` tinyint(1) DEFAULT '1' COMMENT '数据有效性 0 无效  1 有效',
  `version` int(10) DEFAULT '1' COMMENT '数据版本号，用于乐观锁，每次更新操作递增加1',
  PRIMARY KEY (`id`),
  KEY `idx_repayment_plan_ros` (`repayment_plan_id`) COMMENT 'repayment_plan_id查询索引',
  UNIQUE KEY `uk_repayment_plan_calc_time` (`repayment_plan_id`,`overdue_cal_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='日罚息计算日志表';

alter table cashman_mq_message modify column message varchar(2048);

INSERT INTO `tb_mq_consumer` (
	`consumer_name`,
	`consumer_desc`,
	`queue_name`,
	`consumer_url`,
	`url_params`,
	`request_encoding`,
	`request_method`,
	`call_type`,
	`retry_times`,
	`create_time`,
	`version`,
	`status`,
	`is_handle`
)
VALUES
	(
		'cms-alipay-repayment-income',
		'支付宝还款入账MQ',
		'alipay-repayment-income',
		'http://CASHMAN-APP/service/event/alipay-repay-commit',
		NULL,
		'UTF-8',
		'POST',
		'EUREKA',
		'7',
		now(),
		'2',
		'0',
		'0'
	);

