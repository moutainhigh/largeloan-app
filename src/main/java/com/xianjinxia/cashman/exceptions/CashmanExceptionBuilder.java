package com.xianjinxia.cashman.exceptions;

import com.xianjinxia.cashman.domain.IdempotentEvent;

public class CashmanExceptionBuilder {

    public static ServiceException build(String errorCode) {
        return new ServiceException(errorCode, ErrorProperties.getErrorMsg(errorCode));
    }

    public static IdempotentException build(IdempotentEvent idempotentEvent) {
        return new IdempotentException(idempotentEvent);
    }
}
