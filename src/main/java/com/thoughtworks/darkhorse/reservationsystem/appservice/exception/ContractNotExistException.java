package com.thoughtworks.darkhorse.reservationsystem.appservice.exception;

import com.thoughtworks.darkhorse.reservationsystem.domainmodel.AppException;
import com.thoughtworks.darkhorse.reservationsystem.domainmodel.ErrorCode;

import java.util.Map;

public class ContractNotExistException extends AppException {
    public ContractNotExistException(ErrorCode errorCode, Map<String, Object> data) {
        super(errorCode, data);
    }
}
