package com.xianjinxia.cashman.mapper;

import com.xianjinxia.cashman.domain.IdempotentEvent;

public interface IdempotentEventMapper {

    int insert(IdempotentEvent idempotentEvent);

}
