package com.green.jobdone.portfolio.model;

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

    @Schema(title = "thumbnail")
    private String thumbnail;


}
