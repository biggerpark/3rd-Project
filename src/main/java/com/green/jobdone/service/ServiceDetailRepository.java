package com.green.jobdone.service;

import com.green.jobdone.entity.ServiceDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceDetailRepository extends JpaRepository<ServiceDetail,Long> {
}
