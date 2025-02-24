package com.green.jobdone.business.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@AllArgsConstructor
public class BusinessContentsPostRes {
    private String title;
    private String contents;
}
