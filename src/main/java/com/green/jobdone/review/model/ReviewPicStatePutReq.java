package com.green.jobdone.review.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ReviewPicStatePutReq {
    @Schema(title = "리뷰 사진 pk", example = "1",description = "리뷰 수정중 기존 사진 삭제시 리뷰사진 PK", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Long reviewPicId;
    @Schema(title = "리뷰 pk", example = "1",description = "리뷰 수정중 취소시 데이터베이스 상태값 원래대로하기위해 리뷰 PK", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Long reviewId;
}
