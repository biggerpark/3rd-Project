package com.green.jobdone.admin.model;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Year;

public class AdminSalesInfoReq {
    @Schema(description = "년도", example = "1234park@naver.com", requiredMode = Schema.RequiredMode.REQUIRED)
    private Year year;
}
