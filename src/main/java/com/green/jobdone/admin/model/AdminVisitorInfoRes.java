package com.green.jobdone.admin.model;

import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AdminVisitorInfoRes {
    private String date;
    private String dateOfWeek;
    private Integer visitorCount;
}