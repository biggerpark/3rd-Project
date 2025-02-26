package com.green.jobdone.product.model;

import com.green.jobdone.product.model.dto.ProductGetDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ProductGetRes {
    private long productId;
    private int productPrice;
    private String detailTypeName;
    private List<ProductGetDto> optionList;
}
