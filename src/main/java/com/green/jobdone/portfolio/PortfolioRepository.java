package com.green.jobdone.portfolio;

import com.green.jobdone.business.model.BusinessLogoPatchReq;
import com.green.jobdone.entity.Portfolio;
import com.green.jobdone.portfolio.model.PortfolioPatchThumbnailReq;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface PortfolioRepository extends JpaRepository<Portfolio, Long> {
/*
 <delete id="delPortfolio">
        delete from portfolio
        where portfolioId = #{portfolioId}
    </delete>
 */
    @Transactional
    @Modifying
    @Query("UPDATE PortfolioPic p " +
            "SET p.state = CASE " +
            "                 WHEN p.portfolioPicId = :portfolioPicId THEN 2 " +
            "                 ELSE 1 " +
            "             END " +
            "WHERE p.portfolio.portfolioId = :portfolioId")
    int updatePortfolioThumbnail(@Param("portfolioPicId") Long portfolioPicId,
                                 @Param("portfolioId") Long portfolioId);


    @Modifying
    @Query("update Portfolio  p set p.thumbnail=:#{#p.thumbnail} where p.portfolioId=:#{#p.portfolioId}")
    Integer updatePortfolioThumbnail(@Param("p") PortfolioPatchThumbnailReq p);


}
