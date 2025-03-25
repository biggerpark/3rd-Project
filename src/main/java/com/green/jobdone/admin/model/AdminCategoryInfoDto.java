package com.green.jobdone.admin.model;
import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AdminCategoryInfoDto {
    private Long categoryId;
    private String categoryName;
    private int categoryCount;
    private double categoryPercent;
    private List<AdminDetailTypeInfoDto> detailTypeCounts;
}
