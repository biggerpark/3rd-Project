package com.green.jobdone.service.model.Dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OptionDetailDto {
    private Long optionDetailId;
    private String optionDetailName;
    private int optionDetailPrice;
    private String contents;
    private Long serviceOptionId;
    private String optionComment;
}
