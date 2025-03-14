package com.green.jobdone.business.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BusinessPatchThumbnailReq {
    @Schema(title = "businessId")
    private long businessId;

    @JsonIgnore
    private String thumbnail;
}
