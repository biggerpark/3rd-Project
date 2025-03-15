package com.green.jobdone.business.model.get;

import com.green.jobdone.common.PicUrlMaker;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;

@Slf4j
@Getter
@Setter
public class BusinessGetRes {
    private long categoryId;
    private String categoryName;
    private long detailTypeId;
    private String detailTypeName;
    private String logo;
    private String pic;
    private long businessId;
    private String businessName;
    private String title;
    private double scoreAvg;
    private int price;
    private int like;  // 컬럼명 유지
    private int reviewCount;
    private long regionId;
    private String region;
    private int serviceCount;
    private double lat;
    private double lng;
    private int distance;


}
