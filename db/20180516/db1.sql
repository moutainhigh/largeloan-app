alter table cashman_repayment_record add column refund_amt INT(10) unsigned NULL DEFAULT '0'  COMMENT '等退款金额' AFTER repay_overdue_amt;

CREATE TABLE `cashman_refund_log` (
  `id` bigint(11) unsigned NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT '自增ID',
  `user_phone` varchar(50) NOT NULL COMMENT '用户手机号码',
  `loan_order_id` bigint(11) NOT NULL COMMENT '借款订单ID',
  `repayment_plan_id` bigint(11) NOT NULL COMMENT '还款计划ID',
  `repayment_record_id` bigint(11) NOT NULL COMMENT '还款记录ID',
  `refund_amt` int(10) NOT NULL COMMENT '退款金额',
  `refund_channel` varchar(50) DEFAULT NULL COMMENT '退款渠道',
  `refund_order_no` varchar(120) DEFAULT NULL COMMENT '退款订单号',
  `refund_time` datetime NOT NULL COMMENT '退款时间',
  `created_time` datetime NOT NULL COMMENT '创建时间',
  `updated_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `data_valid` tinyint(1) DEFAULT '1' COMMENT '数据有效性 0 无效  1 有效',
  `version` int(10) DEFAULT '1' COMMENT '数据版本号，用于乐观锁，每次更新操作递增加1'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='退款日志表';