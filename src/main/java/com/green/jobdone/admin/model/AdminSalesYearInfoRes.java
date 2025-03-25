package com.green.jobdone.admin.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class AdminSalesYearInfoRes {
    private String month;
    private Integer totalPrice;
    private List<AdminSalesInfoDto> salesInfoDtos;
}
