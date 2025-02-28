package com.green.jobdone.qa.model;

import com.green.jobdone.entity.ReportReason;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class QaReq {
    @Schema(description = "문의 관련 종류,환불 안의 상품불량에 해당되는 pk ", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long qaTypeDetailId;
    @Schema(description = "문의 관련 코멘트 ", example = "상품이 다 부서져서 왔어요", requiredMode = Schema.RequiredMode.REQUIRED)
    private String contents;
    @Schema(example = "SERVICE,COMMENT,REVIEW,CHAT,BUSINESS 중 하나")
    private ReportReason qaReportReason;
    @Schema(description = "환불할 서비스에 해당되는 서비스 pk", example = "환불할 서비스에 해당되는 서비스 pk,1")
    private Long qaTargetId;
}