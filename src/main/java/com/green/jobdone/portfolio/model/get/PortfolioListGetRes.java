package com.green.jobdone.portfolio.model.get;

import com.green.jobdone.common.PicUrlMaker;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Setter
public class PortfolioListGetRes {
    private long businessId;
    private long portfolioId;
    private String title;
    private String thumbnail;


}
