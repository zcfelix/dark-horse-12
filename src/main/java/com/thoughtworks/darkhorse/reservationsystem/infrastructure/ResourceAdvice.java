package com.thoughtworks.darkhorse.reservationsystem.infrastructure;

import com.thoughtworks.darkhorse.reservationsystem.appservice.representation.ErrorDetail;
import com.thoughtworks.darkhorse.reservationsystem.domainmodel.AppException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;

@RestControllerAdvice
public class ResourceAdvice {
    @ExceptionHandler(value = AppException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDetail handAppException(AppException e, HttpServletRequest request) {
        return ErrorDetail.builder()
                .code(e.getErrorCode())
                .path(request.getRequestURL().toString())
                .timestamp(Instant.now())
                .data(e.getData())
                .build();
    }
}
