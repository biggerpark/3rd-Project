package com.green.jobdone.product.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductOptionDetailDto {
    private Long optionDetailId;
    private String optionDetailName;
    private Integer optionDetailPrice;
}
