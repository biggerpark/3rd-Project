package com.green.jobdone.qa;

import com.green.jobdone.entity.QaType;
import com.green.jobdone.entity.QaTypeDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QaTypeDetailRepository extends JpaRepository<QaTypeDetail, Long> {
}
