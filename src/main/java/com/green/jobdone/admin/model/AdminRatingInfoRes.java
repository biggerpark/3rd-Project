package com.green.jobdone.admin.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AdminRatingInfoRes {
    private Double totalAvgScore;
    private Double avgScoreCleaning;
    private Double avgScoreMoving;
    private Double avgScoreCarWash;
}
