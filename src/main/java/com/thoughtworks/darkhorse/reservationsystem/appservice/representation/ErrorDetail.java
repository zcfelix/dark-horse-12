package com.thoughtworks.darkhorse.reservationsystem.appservice.representation;

import com.thoughtworks.darkhorse.reservationsystem.domainmodel.ErrorCode;
import lombok.*;

import java.time.Instant;
import java.util.Map;

/**
 * 错误详细信息，用与表示程序中出现的错误
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public final class ErrorDetail {
    private ErrorCode code;

    private String path;

    private Instant timestamp;

    private Map<String, Object> data;
}
