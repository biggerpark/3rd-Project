package com.green.jobdone.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ChatErrorCode implements ErrorCode {
    FAIL_TO_CONNECT(HttpStatus.BAD_REQUEST, "소켓 연결 실패"),
    MISSING_ROOM(HttpStatus.BAD_REQUEST, "잘못된 접근 입니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
