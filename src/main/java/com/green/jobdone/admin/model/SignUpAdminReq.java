package com.green.jobdone.admin.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SignUpAdminReq {
    private String name;
    private String aId;
    private String aPw;
    private String phone;
}
