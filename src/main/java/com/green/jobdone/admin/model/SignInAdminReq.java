package com.green.jobdone.admin.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Schema(title = "관리자 로그인")
@ToString
public class SignInAdminReq {
    @Schema(description = "아아디", example = "sss@naver.com", requiredMode = Schema.RequiredMode.REQUIRED)
    private String aid;
    @Schema(description = "비밀번호", example = "1111aaaa", requiredMode = Schema.RequiredMode.REQUIRED)
    private String apw;
}
