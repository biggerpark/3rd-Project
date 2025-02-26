package com.green.jobdone.business;

import com.green.jobdone.entity.Business;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BusinessRepository extends JpaRepository<Business, Long> {

    @Query("select b.user.userId from Business b where b.businessId=:businessId")
    Long findUserIdByBusinessId(@Param("businessId") Long businessId);
    @Query("select b.businessId from Business b where b.user.userId=:UserId")
    Long findBusinessIdByUserId(@Param("UserId") Long UserId);
  
    Optional<Business> findById(long id);
   // Optional<Business> findByUserIdAndBusinessId(Long businessId, Long userId);
  
}
