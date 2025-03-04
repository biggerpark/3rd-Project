package com.green.jobdone.portfolio.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PortfolioPicStatePutReq {
    @Schema(title = "포폴 사진 pk", example = "1",description = "포폴 수정중 기존 사진 삭제시 포폴사진 PK", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Long portfolioPicId;
    @Schema(title = "포폴 pk", example = "1",description = "포폴 수정중 취소시 데이터베이스 상태값 원위치하기위한 포폴 PK", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Long portfolioId;
}
