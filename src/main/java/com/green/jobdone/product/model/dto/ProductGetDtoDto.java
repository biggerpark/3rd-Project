package com.green.jobdone.product.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductGetDtoDto {
    private long optionDetailId;
    private String optionDetailName;
    private int optionDetailPrice;
    private String contents;
}
