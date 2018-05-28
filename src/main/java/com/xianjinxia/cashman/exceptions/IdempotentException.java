package com.xianjinxia.cashman.exceptions;

import com.xianjinxia.cashman.domain.IdempotentEvent;

public class IdempotentException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public IdempotentException(IdempotentEvent idempotentEvent) {
        super("has an idempotent exception for [idempotentKey:" + idempotentEvent.getSourceId()
                + ",idempotentType," + idempotentEvent.getSourceType() + "]");
    }

}
