package com.thoughtworks.darkhorse.reservationsystem.domainmodel;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

/**
 * 应用程序异常，所有异常均应该继承此类
 */
@AllArgsConstructor
@Getter
public abstract class AppException extends RuntimeException {
    private final ErrorCode errorCode;
    private Map<String, Object> data;
}
