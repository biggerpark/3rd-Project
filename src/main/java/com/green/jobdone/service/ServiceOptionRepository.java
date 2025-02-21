package com.green.jobdone.service;

import com.green.jobdone.entity.ServiceOption;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceOptionRepository extends JpaRepository<ServiceOption, Long> {
}
