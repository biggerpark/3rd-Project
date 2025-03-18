package com.green.jobdone.business;

import com.green.jobdone.entity.Business;
import com.green.jobdone.entity.BusinessPic;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BusinessPicRepository extends JpaRepository<BusinessPic, Long> {

    @Modifying
    @Query("delete from BusinessPic where businessPicId =:businessPicId")
    Integer deleteByBusinessPicId(@Param("businessPicId") Long businessPicId);


    boolean existsByBusinessAndState(Business business, int state);
}
