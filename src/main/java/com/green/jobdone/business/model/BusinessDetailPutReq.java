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

    @Schema(title = "업체 pk", example = "21:00")
    private long businessId;
    @Schema(title = "오픈 시간", example = "21:00")
    private String openingTime;
    @Schema(title = "마감 시간", example = "21:00")
    private String closingTime;


    @Schema(title = "전번" , example = "01055555555")
    private String tel;
}
