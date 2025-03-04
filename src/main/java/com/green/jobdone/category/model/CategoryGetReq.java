package com.green.jobdone.category.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryGetReq {
    @Schema(title = "카테고리 아이디", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private long categoryId;
}
