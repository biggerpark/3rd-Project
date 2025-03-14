package com.green.jobdone.qa.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class QaViewReq {
    @Schema(description = "조회 문의 pk ", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private long qaId;
    @Schema(description = "조회한 유저 pk ", example = "2", requiredMode = Schema.RequiredMode.REQUIRED)
    private long userId;
}
