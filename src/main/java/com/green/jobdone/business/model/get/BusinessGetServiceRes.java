package com.green.jobdone.business.model.get;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@Slf4j
public class BusinessGetServiceRes {
    private int year;
    private int month;
    private String BusinessName;
    private int serviceCount;
}
