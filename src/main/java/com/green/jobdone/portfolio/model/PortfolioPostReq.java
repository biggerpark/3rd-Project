package com.green.jobdone.portfolio.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PortfolioPostReq {
    @JsonIgnore
    private long portfolioId;

    @Schema(title = "업체pk", requiredMode = Schema.RequiredMode.REQUIRED)
    private long businessId;

    @Schema(title = "가격", requiredMode = Schema.RequiredMode.REQUIRED)
    private int price;

    @Schema(title = "소요시간", requiredMode = Schema.RequiredMode.REQUIRED)
    private String takingTime;

    @Schema(title = "타이틀", requiredMode = Schema.RequiredMode.REQUIRED)
    private String title;

    @Schema(title = "포폴내용", requiredMode = Schema.RequiredMode.REQUIRED)
    private String contents;

    @Schema(title = "유튜브url", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String youtubeUrl;


}
