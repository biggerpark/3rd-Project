package com.green.jobdone.admin.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SignInAdminReq {
    @Schema(example = "rhksflwk2")
    private String aId;
    @Schema(example = "1111aaaa")
    private String aPw;
}
