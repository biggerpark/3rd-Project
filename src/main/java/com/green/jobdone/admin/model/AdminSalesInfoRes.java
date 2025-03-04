package com.green.jobdone.admin.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AdminSalesInfoRes {
    private String month;
    private Integer totalSales;
    private Integer cleaningSales;
    private Integer movingSales;
    private Integer carWashSales;
}
