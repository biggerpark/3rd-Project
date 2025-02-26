package com.green.jobdone.product.model;

import com.green.jobdone.product.model.dto.OptionDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class ProductPostAllReq {
    private Integer price;
    private Long productId;
    private List<OptionDto> options;
}
