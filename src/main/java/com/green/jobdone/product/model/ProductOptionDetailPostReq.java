package com.green.jobdone.product.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductOptionDetailPostReq {
    @Schema(title = "옵션 PK", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private long optionId;
    @Schema(title = "옵션 상세 명", example = "1개,2개", requiredMode = Schema.RequiredMode.REQUIRED)
    private String optionDetailName;
    @Schema(title = "옵션 상세 내용", example = "1개,2개 입니다", requiredMode = Schema.RequiredMode.REQUIRED)
    private String contents;
    @Schema(title = "옵션별 상세 가격", example = "40000", requiredMode = Schema.RequiredMode.REQUIRED)
    private int optionDetailPrice;
    @JsonIgnore
    private long userId;

    @JsonIgnore
    private long optionDetailId;
}
