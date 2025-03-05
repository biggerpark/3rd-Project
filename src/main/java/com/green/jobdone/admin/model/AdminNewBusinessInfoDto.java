package com.green.jobdone.admin.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AdminNewBusinessInfoDto {
    private Integer yesterdayNewBusinessCount;
    private Integer todayNewBusinessCount;
}
