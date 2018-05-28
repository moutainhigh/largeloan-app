package com.xianjinxia.cashman.service;

import com.xjx.mqclient.pojo.MqMessage;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface IMqMessageService {

    void sendMessage(MqMessage mqMessage);

    void sendMessageList(List<MqMessage> mqMessageList);

}
