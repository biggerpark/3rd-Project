package com.green.jobdone.business.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class BusinessPicDelReq {
    private long businessId;
    private long businessPicId;
}
