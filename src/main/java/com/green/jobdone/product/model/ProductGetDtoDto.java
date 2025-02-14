package com.green.jobdone.product.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductGetDtoDto {
    private long optionId;
    private String optionDetailName;
    private int optionDetailPrice;
    private String contents;
}
