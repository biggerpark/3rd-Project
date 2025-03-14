package com.green.jobdone.user.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.green.jobdone.config.jwt.UserRole;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@Schema(title="로그인 응답")
public class UserSignInRes {
    @JsonIgnore
    private long userId;
    private String name;
    private String email;
    private String phone;
    private int type;
    private String pic;
    private String accessToken;
    private long businessId;


    @JsonIgnore //swagger 표시 안 되지만, 응답 때 빼는 역할도 한다.
    private String upw;
    @JsonIgnore
    private String message;
}
