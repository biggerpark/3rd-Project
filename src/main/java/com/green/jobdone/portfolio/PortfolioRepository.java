package com.green.jobdone.portfolio;

import com.green.jobdone.entity.Portfolio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PortfolioRepository extends JpaRepository<Portfolio, Long> {
/*
 <delete id="delPortfolio">
        delete from portfolio
        where portfolioId = #{portfolioId}
    </delete>
 */

}
