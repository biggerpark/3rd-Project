package com.green.jobdone.admin.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class AdminCategoryInfoRes {
    private Long categoryId;
    private String categoryName;
    private int categoryCount;
    private double categoryPercent;
    private List<AdminDetailTypeInfoDto> dto;
}
