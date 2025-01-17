package com.green.jobdone.user.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.beans.ConstructorProperties;

@Getter
@Setter
@Schema(title = "유저 정보 조회")
@ToString
public class UserInfoGetReq {

    @Schema(name = "유저 ID", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private long userId;


}
