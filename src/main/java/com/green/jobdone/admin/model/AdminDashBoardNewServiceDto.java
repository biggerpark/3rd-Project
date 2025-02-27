package com.green.jobdone.admin.model;

import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AdminDashBoardNewServiceDto {
    private Integer todayServiceCount;
    private Integer yesterdayServiceCount;
}
