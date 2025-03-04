package com.green.jobdone.admin.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AdminPaidInfoDto {
    private Integer pastPaidCount;
    private Integer nowPaidCount;
}
