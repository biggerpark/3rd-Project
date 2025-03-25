package com.green.jobdone.qa.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class QaAnswerRes {
    private String answer;
    private long qaAnswerId;
    private String createdAt;
}
