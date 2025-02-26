package com.green.jobdone.entity;


public enum ReportReason {
    SERVICE(1,"서비스"),
    BUSINESS(2,"업체"),
    REVIEW(3,"리뷰"),
    CHAT(4,"채팅"),
    COMMENT(5,"댓글");

    private final int code;
    private final String value;


    ReportReason(int code, String value) {
        this.code = code;
        this.value=value;
    }

    public int getCode() {
        return code;
    }
    public String getValue() {
        return value;
    }

    // 프론트에서 받은 코드로  ENUM 객체인 ReportReason 을 찾아서 ENUM 객체를 반환시켜주는 메소드
    public static ReportReason fromCode(int code) {
        for (ReportReason reason : ReportReason.values()) {
            if (reason.getCode() == code) {
                return reason;
            }
        }
        throw new IllegalArgumentException("Invalid code: " + code);
    }
}