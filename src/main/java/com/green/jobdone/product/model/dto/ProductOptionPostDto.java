package com.green.jobdone.product.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductOptionPostDto {
    @Schema(title = "상품 PK", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private long productId;
    @Schema(title = "상품 이름", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;
    @JsonIgnore
    private long userId;
}
