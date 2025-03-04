package com.green.jobdone.portfolio.model;

import lombok.Builder;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Builder
public class PortfolioPostRes {
    private long portfolioId;
    private List<String> pics;
}
