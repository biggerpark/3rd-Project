package com.green.jobdone.qa.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class QaRes {
    private String createdAt;
    private long qaId;
    private String userName;
    @JsonIgnore
    private int userTypeDB;
    private String userType;
    private String qaType; // 환불,리뷰신고,일반문의 등 ..
    private String qaState;

}
