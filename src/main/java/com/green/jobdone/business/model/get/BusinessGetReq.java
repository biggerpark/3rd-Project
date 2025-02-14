package com.green.jobdone.business.model.get;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class BusinessGetReq {
    @Schema(title = "대분류")
    private Long categoryId;

    @Schema(title = "소분류")
    private Long detailTypeId;

    @Schema(title = "지역")
    private Long regionId;

    @JsonIgnore
    private long signedUserId;

    @Schema(title = "정렬 기준", description = "latest(최신순), rating(별점순), order(주문순), price(가격순), distance(거리순)")
    private String sortType;

    @Schema(title = "검색어")
    private String searchTerm;

    @Schema(title = "user_latitude")
    private double userLat;

    @Schema(title = "user_longitude")
    private double userLng;
}