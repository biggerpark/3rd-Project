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
    private Long qaTypeDetailId;
    private String contents;
    @Schema(example = "서비스, 업체, 리뷰, 채팅, 댓글 중하나")
    private ReportReason qaReportReason;
    private Long qaTargetId;
}
