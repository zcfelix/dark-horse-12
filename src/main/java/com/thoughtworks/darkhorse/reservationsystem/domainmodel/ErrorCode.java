package com.thoughtworks.darkhorse.reservationsystem.domainmodel;

/**
 * 统一的错误码管理
 */
public enum ErrorCode {
    CONTRACT_NOT_EXIST(404),
    DEPOSIT_NOT_PAYED(400),
    CREATE_TRANSACTION_FAILED(500),
    CREATE_TRANSACTION_TIME_OUT(500);

    private final int code;

    ErrorCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
