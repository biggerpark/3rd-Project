package com.green.jobdone.service.model.Dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ServiceOptionDto {
    @JsonIgnore
    private long optionId;
    private String optionName;
    @JsonIgnore
    private long optionDetailId;
    private String optionDetailName;
    private int optionDetailPrice;
    @JsonIgnore
    private String contents;
    @JsonIgnore
    private Long serviceOptionId;
    @JsonIgnore
    private String optionComment;

}
