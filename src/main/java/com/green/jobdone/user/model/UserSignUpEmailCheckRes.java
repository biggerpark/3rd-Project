package com.green.jobdone.user.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(title = "유저 이메일 중복 확인 응답")
public class UserSignUpEmailCheckRes {
    private String email;
    private Integer type;
}
