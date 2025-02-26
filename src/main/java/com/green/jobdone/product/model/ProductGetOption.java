package com.green.jobdone.product.model;

import com.green.jobdone.product.model.dto.ProductGetOptionDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ProductGetOption {
    private long detailTypeId;
    private String detailTypeName;
    private List<ProductGetOptionDto> detailTypeOptions;
}
