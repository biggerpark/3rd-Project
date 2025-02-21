package com.green.jobdone.visitor;

import com.green.jobdone.entity.VisitorHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
interface VisitorHistoryRepository extends JpaRepository<VisitorHistory, Long> {}
