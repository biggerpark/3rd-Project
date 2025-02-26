package com.green.jobdone.admin.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    private long userId;

    @JsonIgnore
    private int typeName;

    private String type;
}
