package com.green.jobdone.admin.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class BusinessRejectReq {
    @Schema(description = "업체 ID", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private long businessId;
    @Schema(description = "거절사유", example = "카테고리 관련 업체가 아님", requiredMode = Schema.RequiredMode.REQUIRED)
    private String rejectContents;

}
