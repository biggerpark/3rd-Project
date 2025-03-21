package com.green.jobdone.user.model;

import com.green.jobdone.config.jwt.UserRole;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserSignInResDto {
    private long userId;
    private Integer state;
    private String name;
    private String email;
    private String type;
    private String pic;
    private List<UserRole> roles;
    private String upw;
    private String phone;
    private long businessId;


}
