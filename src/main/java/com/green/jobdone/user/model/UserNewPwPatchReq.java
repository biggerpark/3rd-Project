package com.green.jobdone.user.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserNewPwPatchReq {
    @Schema(name = "유저 ID", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private long userId;
    @Schema(name = "신규 비밀번호", example = "2222", requiredMode = Schema.RequiredMode.REQUIRED)
    private String newPassword;
}
