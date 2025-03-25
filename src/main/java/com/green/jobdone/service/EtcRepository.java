package com.green.jobdone.service;

import com.green.jobdone.entity.Etc;

import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EtcRepository extends JpaRepository<Etc, Long> {
    @Query("SELECT e.etcId FROM Etc e WHERE e.service.serviceId = :serviceId")
    List<Long> findEtcIdsByServiceId(@Param("serviceId") Long serviceId);
}
