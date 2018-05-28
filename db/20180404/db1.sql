-- 创建催收系统请求表
CREATE TABLE `cashman_collect_req` (
  `id` bigint(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `uuid` varchar(100) DEFAULT NULL COMMENT '催收业务字段',
  `repayment_plan_id` bigint(11) NOT NULL DEFAULT '0' COMMENT '借款单ID',
  `user_id` bigint(11) unsigned NOT NULL DEFAULT '0' COMMENT '用户ID',
  `amount` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '代扣金额(分)',
	`status` int(6) NOT NULL DEFAULT '0' COMMENT '请求状态 0 初始请求  1 请求成功  2 请求失败',
	`collect_type` int(6) NOT NULL DEFAULT '0' COMMENT '催收类型：1催收代扣 2 催收减免',
  `created_time` datetime NOT NULL COMMENT '创建时间',
  `updated_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `data_valid` tinyint(1) DEFAULT '1' COMMENT '数据有效性 0 无效  1 有效',
  `version` int(10) DEFAULT '1' COMMENT '数据版本号，用于乐观锁，每次更新操作递增加1',
  PRIMARY KEY (`id`),
  KEY `idx_repayment_plan_ros` (`uuid`,`repayment_plan_id`) COMMENT 'uuid、repayment_plan_id查询的联合索引'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='催收系统请求表';

-- 新增催收代扣消费队列
INSERT  INTO tb_mq_consumer(consumer_name,consumer_desc,queue_name,consumer_url,request_encoding,request_method,call_type,status,is_handle,version)
VALUES ('cashmanapp-collection-withhold','催收代扣消费队列','collection_withhold_push_queue','http://CASHMAN-APP/service/event/collection-withhold','UTF-8','POST','EUREKA',0,0,2);