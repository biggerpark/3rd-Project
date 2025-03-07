package com.green.jobdone.admin.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AdminDashBoardInfoRes {
    @Schema(title = "당일 신규 거래 수")
    private Integer newServiceCount;
    @Schema(title = "어제 대비 신규 거래 증가율")
    private Double newServicePercent;
    @Schema(title = "당일 신규 유저 수")
    private Integer newUserCount;
    @Schema(title = "어제 대비 신규 유저 증가율")
    private Double newUserPercent;
    @Schema(title = "미처리 문의 건수")
    private Integer unprocessedInquiries;
    @Schema(title = "오늘 증가된 미처리 문의 건수")
    private Integer increaseUnprocessedInquiries;
    @Schema(title = "당일 신규 등록 업체 수")
    private Integer newBusinessCount;
    @Schema(title = "어제 대비 신규 등록 업체 증가 수")
    private Integer newBusinessCountThenYesterday;
}
