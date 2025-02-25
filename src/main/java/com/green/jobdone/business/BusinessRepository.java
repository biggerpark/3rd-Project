package com.green.jobdone.business;

import com.green.jobdone.business.model.BusinessContentsPostReq;
import com.green.jobdone.entity.Business;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BusinessRepository extends JpaRepository<Business, Long> {

    Optional<Business> findById(long id);
   // Optional<Business> findByUserIdAndBusinessId(Long businessId, Long userId);

    @Modifying
    @Query("update Business b set b.title=:#{#p.title} ,b.contents=:#{#p.contents} where b.businessId=:#{#p.businessId}")
    void updateBusinessContents(@Param("p") BusinessContentsPostReq p);
}
