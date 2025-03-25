package com.green.jobdone.admin.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AdminRatingInfoDto {
    private String categoryName;
    private Double avgScore;
    private Double totalAvgScore;
}
