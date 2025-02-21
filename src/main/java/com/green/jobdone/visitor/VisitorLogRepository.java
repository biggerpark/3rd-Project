package com.green.jobdone.visitor;

import com.green.jobdone.entity.VisitorLog;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;

public interface VisitorLogRepository extends JpaRepository<VisitorLog, Long> {
    boolean existsByIpAddressAndVisitDate(String ipAddress, LocalDate visitDate);
}