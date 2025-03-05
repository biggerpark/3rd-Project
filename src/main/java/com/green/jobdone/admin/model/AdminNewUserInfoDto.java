package com.green.jobdone.admin.model;

import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AdminNewUserInfoDto {
    private Integer yesterdayNewUserCount;
    private Integer todayNewUserCount;
}
