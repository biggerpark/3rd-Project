package com.green.jobdone.service.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.green.jobdone.service.model.Dto.ServiceEtcDto;
import com.green.jobdone.service.model.Dto.ServiceOptionDto;
import com.green.jobdone.service.model.Dto.ServicePhone;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ServiceGetOneRes {
    private long serviceId;
    @JsonIgnore
    private long userId;
    @JsonIgnore
    private long busiUserId;
    private String userName;
    private String userPhone;
    private String businessName;
    private String detailTypeName;
    private String businessPhone;
    private int price;
    private int addPrice;
    private int totalPrice;
    private int completed;
    private String comment;
    private String addComment;
    private String startDate;
    private String endDate;
    private String mStartTime;
    private String mEndTime;
    private String address;
    private String businessAddress;
    private String categoryName;
    private String businessNum;
    private String createdAt;
    private String updatedAt;
    private String pyeong;
    private String paidAt;
    private String doneAt;
    private List<ServiceOptionDto> options;
    private List<ServiceEtcDto> etc;
}
