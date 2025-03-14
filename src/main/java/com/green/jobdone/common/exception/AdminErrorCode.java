package com.green.jobdone.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AdminErrorCode implements ErrorCode {
    USE_ONLY_ADMIN(HttpStatus.BAD_REQUEST,"관리자만 가능한 기능입니다."),
    ALREADY_USE_ID(HttpStatus.BAD_REQUEST,"사용 불가능한 ID입니다."),
    CHECK_ADMIN(HttpStatus.BAD_REQUEST, "올바른 관리자 계정 혹은 비밀번호인지 확인해 주세요")
    ;
    private final HttpStatus httpStatus;
    private final String message;
}
