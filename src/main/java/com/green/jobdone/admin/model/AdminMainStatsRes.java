package com.green.jobdone.admin.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class AdminMainStatsRes {
    @Schema(title = "전월대비 성장률", description = "결제완료된 서비스들로 비교했습니다.")
    private Double growthRate;
    @Schema(title = "이번달 신규고객 수")
    private Integer newCustomerCount;
    @Schema(title = "총 리뷰평점 평균")
    private Double totalAvg;
    @Schema(title = "분야 별 리뷰평점 평균")
    private List<AdminRatingInfoRes> ratingInfoRes;
    @Schema(title = "총 거래량 수")
    private Integer compeletedServiceCount;

}
