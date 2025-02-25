package com.green.jobdone.user.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(title = "로그인 요청")
public class UserSignInReq {

    @NotNull(message = "아이디를 입력하셔야 합니다.")
    @Schema(description = "유저 이메일", example = "sysooo18@naver.com", requiredMode = Schema.RequiredMode.REQUIRED)
    private String email;


    @NotNull(message = "비밀번호를 입력하셔야 합니다.")
    @Size(min=4, max=50, message = "비밀번호는 4~50자 사이만 가능합니다.")
    @Schema(description = "유저 비밀번호", example = "Zz111111", requiredMode = Schema.RequiredMode.REQUIRED)
    private String upw;
}
