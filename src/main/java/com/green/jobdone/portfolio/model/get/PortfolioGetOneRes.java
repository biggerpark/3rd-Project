package com.green.jobdone.portfolio.model.get;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PortfolioGetOneRes {
    private long businessId;
    private long portfolioId;
    private String title;
    private int price;
    private String takingTime;
    private String contents;
    private String detailType;
    private String category;
    private String youtubeUrl;
    private String youtubeId;
    private String thumbnail;

}
