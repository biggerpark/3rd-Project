package com.green.jobdone.service;

import com.green.jobdone.entity.Etc;

import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface EtcRepository extends JpaRepository<Etc, Long> {
}
