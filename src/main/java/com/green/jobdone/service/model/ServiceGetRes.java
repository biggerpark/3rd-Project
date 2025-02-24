package com.green.jobdone.service.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class ServiceGetRes {
    private long serviceId;
    private String userName;
    private String businessName;
    private String detailTypeName;
    private int price;
    private int addPrice;
    private int totalPrice;
    private int completed;
    private String startDate;
    private String createdAt;
    private String address;
}
