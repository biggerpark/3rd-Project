package com.green.jobdone.product.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.green.jobdone.service.model.Dto.OptionDetailDto;
import lombok.*;

import java.util.List;

@Getter
@Setter
public class OptionDto {
    private Long optionId;
    private String optionName;
    private List<ProductOptionDetailDto> optionDetails;
}
