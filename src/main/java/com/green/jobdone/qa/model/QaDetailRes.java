package com.green.jobdone.qa.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class QaDetailRes {
    private String title;
    private String contents;
    private List<String> pics;

}
