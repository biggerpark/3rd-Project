package com.green.jobdone.business.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class BusinessLogoPatchReq {


    @Schema(title = "회사pk",requiredMode = Schema.RequiredMode.REQUIRED)
    private long businessId;

    @JsonIgnore
    private String logo;
}
