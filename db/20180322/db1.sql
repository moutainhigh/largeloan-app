--订单表添加字段merchant_no(商户号)
alter table cashman_loan_order add column merchant_no varchar(64) DEFAULT ''  COMMENT '商户号';
--产品表添加字段merchant_no(商户号)
alter table products add column merchant_no varchar(64) DEFAULT ''  COMMENT '商户号';

--添加是否购买提速卡
alter table cashman_loan_order add column buy_card varchar(10) DEFAULT ''  COMMENT '是否购买提速卡';

--添加是否购买提速卡
alter table cashman_loan_order add column speed_card_id varchar(32) DEFAULT ''  COMMENT '提速卡id';

--添加提速卡支付状态
alter table cashman_loan_order add column speed_card_pay_status varchar(10) DEFAULT ''  COMMENT '提速卡支付状态';





--添加小额产品信息

