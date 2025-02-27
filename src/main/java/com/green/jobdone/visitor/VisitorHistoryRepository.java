package com.green.jobdone.visitor;

import com.green.jobdone.entity.VisitorHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface VisitorHistoryRepository extends JpaRepository<VisitorHistory, Long> {
    @Query("SELECT v.visitorCount FROM VisitorHistory v WHERE v.visitDate = :date")
    int getVisitorCountByDate(LocalDate date);
}
