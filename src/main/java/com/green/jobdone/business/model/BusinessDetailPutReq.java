package com.green.jobdone.business.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class BusinessDetailPutReq {
    @JsonIgnore
    private long signedUserId;

    @Schema(title = "업체 pk", example = "21:00", requiredMode = Schema.RequiredMode.REQUIRED)
    private long businessId;
    @Schema(title = "오픈 시간", example = "21:00", requiredMode = Schema.RequiredMode.REQUIRED)
    private String openingTime;
    @Schema(title = "마감 시간", example = "21:00", requiredMode = Schema.RequiredMode.REQUIRED)
    private String closingTime;


    @Schema(title = "업체 간단 설명", example = "벌레 잡아드립니다", requiredMode = Schema.RequiredMode.REQUIRED)
    private String title;
    @Schema(title = "업체 상세 설명", example = "겨울방학이라서 집벌레 많죠? 잡아드림.", requiredMode = Schema.RequiredMode.REQUIRED)
    private String contents;

    @Schema(title = "전번" , example = "01055555555", requiredMode = Schema.RequiredMode.REQUIRED)
    private String tel;
}
