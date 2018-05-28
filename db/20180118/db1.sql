alter table cashman_loan_order add column is_depository varchar(2) NOT NULL default '1' comment '是否存管，1表示是 2表示否';

alter table cashman_loan_order add column term_unit varchar(20) NOT NULL default '' comment '期数单位（01表示天 02表示月）';

alter table cashman_loan_order add column term_rate decimal(10,4) NOT NULL default '0.0000' comment '期利率';


ALTER TABLE cashman_loan_order DROP INDEX idx_loan_order_tno;

create unique index idx_loan_order_tno on cashman_loan_order(trace_no);

INSERT INTO `contract` VALUES ('3', '存管协议', 'DEPOSITORY_AGREEMENT', '1', 'http://large-installment.xianjinxia.com/#/custodyprotocol', null, '1', null, '', now());


alter table products add column is_depository varchar(2) NOT NULL DEFAULT '1' COMMENT '是否存管(1表示是 2表示否)';

alter table products add column day_payment_max_amount bigint(20) NOT NULL DEFAULT '0' COMMENT '单日放款最大值（单位是分）';
--2018-02-12修改冷却时间为30天
update products set quiet_period =30 where product_category = 2 and data_valid =1;