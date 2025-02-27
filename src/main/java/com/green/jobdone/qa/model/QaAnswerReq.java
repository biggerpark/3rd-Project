package com.green.jobdone.qa.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class QaAnswerReq {
    private long qaId;
    private String answer;
}
