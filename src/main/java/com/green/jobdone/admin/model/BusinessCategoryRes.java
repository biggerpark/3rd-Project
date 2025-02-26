package com.green.jobdone.admin.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class BusinessCategoryRes {
    private String applicationCreatedAt;
    private double reviewScore;
    private int reviewNumbers;
    private String detailTypeName;
    private String userName;
    private String businessName;
    private String tel;

}
