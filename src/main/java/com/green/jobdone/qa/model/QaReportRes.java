package com.green.jobdone.qa.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class QaReportRes {
    private String qaCreatedAt;
    private String qaTypeName;
    private String qaDetailTypeName;
    private String userEmail;
    private String qaState;
    private long qaId;
}
