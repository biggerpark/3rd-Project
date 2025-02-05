package com.green.jobdone.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ServiceErrorCode implements ErrorCode {
    BUSINESS_OWNER_MISMATCH(HttpStatus.FORBIDDEN, "해당 업체만 가능한 작업입니다."),
    USER_MISMATCH(HttpStatus.FORBIDDEN,"서비스를 신청한 유저만 조회 가능합니다."),
    INVALID_SERVICE_STATUS(HttpStatus.BAD_REQUEST,"잘못된 상태전환 요청입니다.")
    ;
    private final HttpStatus httpStatus;
    private final String message;
}
