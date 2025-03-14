package com.green.jobdone.portfolio;

import com.green.jobdone.entity.PortfolioPic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface PortfolioPicRepository extends JpaRepository<PortfolioPic, Integer> {

    @Modifying
    @Transactional
    @Query("UPDATE PortfolioPic SET state = 1 WHERE portfolioPicId = :portfolioPicId")
    void updateStateByPortfolioPicId(@Param("portfolioPicId") Long portfolioPicId);

    @Modifying
    @Transactional
    @Query("UPDATE PortfolioPic  SET state = 0 WHERE portfolio.portfolioId = :portfolioId")
    void updateStateByPortfolioId(@Param("portfolioId") Long portfolioId);

    @Modifying
    @Transactional
    @Query("delete from PortfolioPic p where p.portfolio.portfolioId =:portfolioId and p.state = 1")
    int deletePortfolioPicByPortfolioPicId(@Param("portfolioId") Long portfolioId);

    @Query("SELECT p.pic FROM PortfolioPic p WHERE p.portfolio.portfolioId = :portfolioId AND p.state = 1")
    List<String> getPortfolioPicsByPortfolioId(@Param("portfolioId") Long portfolioId);



}
