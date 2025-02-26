package com.green.jobdone.qa;

import com.green.jobdone.entity.Qa;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QaRepository extends JpaRepository<Qa, Long> {
}
