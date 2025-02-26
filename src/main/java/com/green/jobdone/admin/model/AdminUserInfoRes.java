package com.green.jobdone.admin.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AdminUserInfoRes {
    private String userName;
    private String phone;
    private String detailTypeName;
    private int serviceNumber;
    private String type;
}
