package com.green.jobdone.qa;

import com.green.jobdone.entity.QaTypeDetail;
import com.green.jobdone.entity.QaViewsIds;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QaViewRepository extends JpaRepository<QaViewRepository, QaViewsIds> {



}
