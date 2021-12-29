package com.thoughtworks.darkhorse.reservationsystem.domainservice.externalservice;

import com.thoughtworks.darkhorse.reservationsystem.domainmodel.AppException;
import com.thoughtworks.darkhorse.reservationsystem.domainmodel.ErrorCode;

import java.util.Map;

public class CreateTransactionTimeoutException extends AppException {
    public CreateTransactionTimeoutException(ErrorCode errorCode, Map<String, Object> data) {
        super(errorCode, data);
    }
}
