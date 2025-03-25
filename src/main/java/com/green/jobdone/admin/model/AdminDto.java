package com.green.jobdone.admin.model;

import com.green.jobdone.config.jwt.UserRole;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class AdminDto {
    private String aPw;
    private List<UserRole> roles;
    private String name;
    private Long adminId;
}
