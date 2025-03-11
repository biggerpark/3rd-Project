package com.green.jobdone.user.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

@Schema(title = "유저 정보 조회 응답")
public class UserInfoGetRes {
    private String pic;
    private String name;
    private String email;
    private String phone;




}
