package com.green.jobdone.qa;

import com.green.jobdone.entity.QaTypeDetail;
import com.green.jobdone.entity.QaView;
import com.green.jobdone.entity.QaViewsIds;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface QaViewRepository extends JpaRepository<QaView, QaViewsIds> {
    Optional<QaView> findByQaViewsIds_QaIdAndQaViewsIds_UserId(Long qaId, Long userId);

}
