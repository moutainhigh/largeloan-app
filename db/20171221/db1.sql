CREATE TABLE `cashman_loan_capital_info` (
  `id` bigint(32) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `trd_loan_order_id` bigint(32) NOT NULL COMMENT '订单id',
  `capital_payers` varchar(255) DEFAULT NULL COMMENT '投资人',
  `capital_company` varchar(255) DEFAULT NULL COMMENT '居间方公司名称',
  `capital_city` varchar(255) DEFAULT NULL COMMENT '居间方公司所在城市',
  `created_at` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_loan_capital_order_id` (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='订单投资人信息表';

alter table cashman_loan_order add column interest_amount int DEFAULT 0 COMMENT '借款利息';

alter table cashman_loan_order add column loan_usage varchar(128) DEFAULT '' COMMENT '借款用途';

alter table cashman_loan_order add column loan_time datetime DEFAULT NULL COMMENT '实际放款时间';


alter table products_fee_config add column periods int(11) NOT NULL COMMENT '期数';

DROP TABLE IF EXISTS `cashman_loan_usage`;
CREATE TABLE `cashman_loan_usage` (
  `id` bigint(32) NOT NULL AUTO_INCREMENT COMMENT '表id',
  `usage_order` int(11) NOT NULL COMMENT '场景序号',
  `usage_code` varchar(32) NOT NULL COMMENT '用途场景编号',
  `usage_name` varchar(32) NOT NULL COMMENT '用途场景名称',
  `description` varchar(255) DEFAULT NULL COMMENT '描述',
  `data_valid` tinyint(1) NOT NULL DEFAULT '1' COMMENT '数据有效性 0 无效  1 有效',
  `updated_time` datetime DEFAULT NULL COMMENT '更新时间',
  `created_user` varchar(32) NOT NULL COMMENT '创建人',
  `created_time` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_loan_usage_id` (`id`) USING BTREE,
  KEY `idx_loan_uasge_ut` (`updated_time`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COMMENT='借款用途场景配置表';

alter table contract modify column contract_url varchar(255) NOT NULL COMMENT '合同路径';





DROP TABLE IF EXISTS `cashman_job_lock`;
CREATE TABLE `cashman_job_lock` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `job_name` varchar(255) NOT NULL COMMENT '任务名称',
  `uk_token` bigint(20) NOT NULL DEFAULT '0' COMMENT '唯一约束字段',
  `created_time` datetime NOT NULL COMMENT '创建时间',
  `updated_time` datetime NOT NULL COMMENT '修改时间',
  `desc` varchar(100) NOT NULL DEFAULT '' COMMENT 'job描述',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_job_lock_uk` (`job_name`,`uk_token`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `cashman_loan_contract`;
CREATE TABLE `cashman_loan_contract` (
  `id` bigint(32) NOT NULL AUTO_INCREMENT,
  `contract_name` varchar(32) DEFAULT NULL,
  `contract_type` varchar(32) NOT NULL,
  `trd_loan_id` bigint(32) NOT NULL,
  `user_id` bigint(32) NOT NULL,
  `contract_path` varchar(512) DEFAULT NULL,
  `loan_contract_desc` varchar(128) DEFAULT NULL,
  `status` varchar(32) NOT NULL,
  `created_time` datetime NOT NULL,
  `created_user` varchar(32) DEFAULT NULL,
  `updated_time` datetime DEFAULT NULL,
  `updated_user` varchar(32) DEFAULT NULL,
  `data_valid` tinyint(1) DEFAULT NULL COMMENT '数据有效性 0 无效  1 有效',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_loan_contract_tuc` (`trd_loan_id`,`user_id`,`contract_type`),
  KEY `idx_loan_contract_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='借款合同表';

alter table cashman_loan_order add column review_fail_time datetime DEFAULT NULL COMMENT '审核失败时间';

alter table cashman_payment_request modify column resp_msg varchar(1000) DEFAULT NULL  COMMENT '支付请求返回信息';


alter table cashman_repayment_record add column repay_principal_amt int(11) NOT NULL DEFAULT '0' COMMENT '还款本金';

alter table cashman_repayment_record add column repay_interest_amt int(11) NOT NULL DEFAULT '0' COMMENT '还款利息';

alter table cashman_repayment_record add column  repay_overdue_amt int(11) NOT NULL DEFAULT '0' COMMENT '还款罚息,逾期产生的利息费用';



###初始化基础数据
truncate products;
INSERT INTO `products` VALUES ('1', '大额分期贷', '1', '大额分期（1-3期）', '2', '300000', '1000000', '30', '01', '1', '3', '35', null, null, null, now(), 'sys', null, '1', null, now(), null)

truncate products_fee_config;
('1', '1', 'sys', now(), 'sys', now(), '利息费', 'interest_fee', '0.0300', '大额利息费（月利率）', '1', '1'),
('2', '1', 'sys', now(), 'sys', now(), '利息费', 'interest_fee', '0.0300', '大额利息费（月利率）', '1', '2'),
('3', '1', 'sys', now(), 'sys', now(), '利息费', 'interest_fee', '0.0300', '大额利息费（月利率）', '1', '3'),
('4', '1', 'sys', now(), 'sys', now(), '逾期费', 'overdue_fee', '0.0001', '大额逾期费（日利率）', '1', '1'),
('5', '1', 'sys', now(), 'sys', now(), '逾期费', 'overdue_fee', '0.0001', '大额逾期费（日利率）', '1', '2'),
('6', '1', 'sys', now(), 'sys', now(), '逾期费', 'overdue_fee', '0.0001', '大额逾期费（日利率）', '1', '3');
truncate cashman_loan_usage;
INSERT INTO `cashman_loan_usage` VALUES ('1', '1', 'LIVING_CONSUMPTION', '生活消费', null, '1', now(), 'sys', now());
INSERT INTO `cashman_loan_usage` VALUES ('2', '2', 'RENOVATION_COST', '装修', null, '1', now(), 'sys', now());
INSERT INTO `cashman_loan_usage` VALUES ('3', '3', 'EDUCATION_COST', '教育', null, '1', now(), 'sys', now());
INSERT INTO `cashman_loan_usage` VALUES ('4', '4', 'MEDICAL_COST', '医疗', null, '1', now(), 'sys', now());

truncate contract;
INSERT INTO `contract` VALUES ('1', '借款协议', 'LOAN_AGREEMENT', '1', 'http://large-installment.xianjinxia.com/#/loanprotocol', null, '1', null, '', now());
INSERT INTO `contract` VALUES ('2', '平台服务协议', 'PLATFORM_SERVICE_AGREEMENT', '1', 'http://large-installment.xianjinxia.com/#/platformservice', null, '1', null, '', now());

DROP TABLE IF EXISTS `cashman_job_lock`;
CREATE TABLE `cashman_job_lock` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `job_name` varchar(255) NOT NULL COMMENT '任务名称',
  `uk_token` bigint(20) NOT NULL DEFAULT '0' COMMENT '唯一约束字段',
  `created_time` datetime NOT NULL COMMENT '创建时间',
  `updated_time` datetime NOT NULL COMMENT '修改时间',
  `desc` varchar(100) NOT NULL DEFAULT '' COMMENT 'job描述',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_job_lock_uk` (`job_name`,`uk_token`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `cashman_loan_contract`;
CREATE TABLE `cashman_loan_contract` (
  `id` bigint(32) NOT NULL AUTO_INCREMENT,
  `contract_name` varchar(32) DEFAULT NULL,
  `contract_type` varchar(32) NOT NULL,
  `trd_loan_id` bigint(32) NOT NULL,
  `user_id` bigint(32) NOT NULL,
  `contract_path` varchar(512) DEFAULT NULL,
  `loan_contract_desc` varchar(128) DEFAULT NULL,
  `status` varchar(32) NOT NULL,
  `created_time` datetime NOT NULL,
  `created_user` varchar(32) DEFAULT NULL,
  `updated_time` datetime DEFAULT NULL,
  `updated_user` varchar(32) DEFAULT NULL,
  `data_valid` tinyint(1) DEFAULT NULL COMMENT '数据有效性 0 无效  1 有效',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_loan_contract_tuc` (`trd_loan_id`,`user_id`,`contract_type`),
  KEY `idx_loan_contract_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='借款合同表';

alter table cashman_loan_order add column review_fail_time datetime DEFAULT NULL COMMENT '审核失败时间';

alter table cashman_payment_request modify column resp_msg varchar(1000) DEFAULT NULL  COMMENT '支付请求返回信息';


alter table cashman_repayment_record add column repay_principal_amt int(11) NOT NULL DEFAULT '0' COMMENT '还款本金';

alter table cashman_repayment_record add column repay_interest_amt int(11) NOT NULL DEFAULT '0' COMMENT '还款利息';

alter table cashman_repayment_record add column  repay_overdue_amt int(11) NOT NULL DEFAULT '0' COMMENT '还款罚息,逾期产生的利息费用';



###初始化基础数据
truncate products;
INSERT INTO `products` VALUES ('1', '大额分期贷', '1', '大额分期（1-3期）', '2', '300000', '1000000', '30', '01', '1', '3', '35', null, null, null, now(), 'sys', null, '1', null, now(), null)

truncate products_fee_config;
INSERT INTO `products_fee_config` VALUES
('1', '1', 'sys', now(), 'sys', now(), '利息费', 'interest_fee', '0.0300', '大额利息费（月利率）', '1', '1'),
('2', '1', 'sys', now(), 'sys', now(), '利息费', 'interest_fee', '0.0300', '大额利息费（月利率）', '1', '2'),
('3', '1', 'sys', now(), 'sys', now(), '利息费', 'interest_fee', '0.0300', '大额利息费（月利率）', '1', '3'),
('4', '1', 'sys', now(), 'sys', now(), '逾期费', 'overdue_fee', '0.0001', '大额逾期费（日利率）', '1', '1'),
('5', '1', 'sys', now(), 'sys', now(), '逾期费', 'overdue_fee', '0.0001', '大额逾期费（日利率）', '1', '2'),
('6', '1', 'sys', now(), 'sys', now(), '逾期费', 'overdue_fee', '0.0001', '大额逾期费（日利率）', '1', '3');

truncate cashman_loan_usage;
INSERT INTO `cashman_loan_usage` VALUES ('1', '1', 'LIVING_CONSUMPTION', '生活消费', null, '1', now(), 'sys', now());
INSERT INTO `cashman_loan_usage` VALUES ('2', '2', 'RENOVATION_COST', '装修', null, '1', now(), 'sys', now());
INSERT INTO `cashman_loan_usage` VALUES ('3', '3', 'EDUCATION_COST', '教育', null, '1', now(), 'sys', now());
INSERT INTO `cashman_loan_usage` VALUES ('4', '4', 'MEDICAL_COST', '医疗', null, '1', now(), 'sys', now());

truncate contract;
INSERT INTO `contract` VALUES ('1', '借款协议', 'LOAN_AGREEMENT', '1', 'http://large-installment.xianjinxia.com/#/loanprotocol', null, '1', null, '', now());
INSERT INTO `contract` VALUES ('2', '平台服务协议', 'PLATFORM_SERVICE_AGREEMENT', '1', 'http://large-installment.xianjinxia.com/#/platformservice', null, '1', null, '', now());
