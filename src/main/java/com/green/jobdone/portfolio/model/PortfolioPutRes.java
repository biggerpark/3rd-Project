package com.green.jobdone.portfolio.model;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class PortfolioPutRes {
    private long portfolioId;
    private String youtubeId;
    private List<String> pics;
}
