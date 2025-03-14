package com.green.jobdone.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AdminErrorCode implements ErrorCode {
    USE_ONLY_ADMIN(HttpStatus.BAD_REQUEST,"관리자만 가능한 기능입니다.");
    private final HttpStatus httpStatus;
    private final String message;
}
