package com.green.jobdone.business.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(title="업체 컨텐츠 등록 요청")
@EqualsAndHashCode
public class BusinessContentsPostReq {
    @JsonIgnore
    private long signedUserId;

    @Schema(title = "업체 pk")
    private long businessId;

    @Schema(title = "타이틀")
    private String title;

    @Schema(title = "내용")
    private String contents;
}
