package com.green.jobdone.qa;

import com.green.jobdone.entity.Qa;
import com.green.jobdone.entity.QaAnswer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QaAnswerRepository extends JpaRepository<QaAnswer, Long> {
}
