package com.green.jobdone.admin.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AdminMainStatsRes {
    @Schema(title = "전월대비 성장률", description = "결제완료된 서비스들로 비교했습니다.")
    private Double growthRate;
    @Schema(title = "이번달 신규고객 수")
    private Integer newCustomerCount;
    @Schema(title = "총 전체 리뷰 별점 평균")
    private Double averageRating;
    @Schema(title = "청소 전체 리뷰 별점 평균")
    private Double cleaningAverageRating;
    @Schema(title = "이사 전체 리뷰 별점 평균")
    private Double movingAverageRating;
    @Schema(title = "세차 전체 리뷰 별점 평균")
    private Double carWashAverageRating;
    @Schema(title = "총 거래량 수")
    private Integer compeletedServiceCount;

}
