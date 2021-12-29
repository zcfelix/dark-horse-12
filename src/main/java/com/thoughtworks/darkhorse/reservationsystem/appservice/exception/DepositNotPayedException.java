package com.thoughtworks.darkhorse.reservationsystem.appservice.exception;

import com.thoughtworks.darkhorse.reservationsystem.domainmodel.AppException;
import com.thoughtworks.darkhorse.reservationsystem.domainmodel.ErrorCode;

import java.util.Map;

public class DepositNotPayedException extends AppException {
    public DepositNotPayedException(ErrorCode errorCode, Map<String, Object> data) {
        super(errorCode, data);
    }
}
