package com.green.jobdone.qa.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class QaBoardRes {
    private String title;
    private String userName;
    private String createdAt;
    private int qaView;
    private long qaId;
    private String reason;
}
