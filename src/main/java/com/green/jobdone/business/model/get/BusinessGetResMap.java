package com.green.jobdone.business.model.get;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@Getter
@Setter
public class BusinessGetResMap {
    private double userLat;
    private double userLng;

    private List<BusinessGetRes> businessList;

}
