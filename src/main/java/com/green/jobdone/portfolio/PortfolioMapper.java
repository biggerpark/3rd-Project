package com.green.jobdone.portfolio;

import com.green.jobdone.portfolio.model.PortfolioPicDto;
import com.green.jobdone.portfolio.model.PortfolioPostReq;
import com.green.jobdone.portfolio.model.PortfolioPutReq;
import com.green.jobdone.portfolio.model.get.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PortfolioMapper {
    int insPortfolio(PortfolioPostReq p);
    int delPortfolio(long portfolioId);

    int udtPortfolio(PortfolioPutReq p);

    List<PortfolioListGetRes> selAllPortfolioList(PortfolioListGetReq p);
    PortfolioGetOneRes selOnePortfolio(long portfolioId);

    //pic
    int insPortfolioPic(PortfolioPicDto p);
    List<PortfolioPicGetRes> getPortfolioPicList(PortfolioPicGetReq p);
    int delPortfolioPic(long portfolioPicId);
}

