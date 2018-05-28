-- 新增催收代扣消费队列
INSERT  INTO tb_mq_consumer(consumer_name,consumer_desc,queue_name,consumer_url,request_encoding,request_method,call_type,status,is_handle,version)
VALUES ('cashmanapp-collection-deduct','催收减免消费队列','collection_deduct_push_queue','http://CASHMAN-APP/service/event/collection-deduct','UTF-8','POST','EUREKA',0,0,2);
--新增冲正还款消费队列
INSERT  INTO tb_mq_consumer(consumer_name,consumer_desc,queue_name,consumer_url,request_encoding,request_method,call_type,status,is_handle,version)
VALUES ('cashmanapp-before-hand-pay','冲正提前还款消费队列','before_hand_pay_queue','http://PAYMENTCENTER/api/progress/custody/small/repaymenthandle','UTF-8','POST','EUREKA',0,0,2);