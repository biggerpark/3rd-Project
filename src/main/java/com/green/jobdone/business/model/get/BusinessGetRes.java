package com.green.jobdone.business.model.get;

import com.green.jobdone.common.PicUrlMaker;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
public class BusinessGetRes {
    private String detailTypeName;
    private String pic;
    private long businessId;
    private String businessName;
    private String title;
    private double scoreAvg;
    private int price;
    private int like;

    public BusinessGetRes(String detailTypeName,String pic, long businessId, String businessName, String title
            , int price, double scoreAvg, int like) {
        this.detailTypeName = detailTypeName;
        this.pic = PicUrlMaker.makePicUrl(businessId, pic);
        this.businessId = businessId;
        this.businessName = businessName;
        this.title = title;
        this.price = price;
        this.scoreAvg = scoreAvg;
        this.like = like;
    }
}
