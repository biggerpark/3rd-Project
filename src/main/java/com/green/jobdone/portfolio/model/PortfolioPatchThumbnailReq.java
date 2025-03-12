package com.green.jobdone.portfolio.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PortfolioPatchThumbnailReq {

    @Schema(title = "businessId")
    private long businessId;

    @Schema(title = "portfolioId")
    private long portfolioId;

    @JsonIgnore
    private String thumbnail;


}
