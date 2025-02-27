package com.green.jobdone.service;

import com.green.jobdone.entity.Service;
import com.green.jobdone.service.model.Dto.KakaoPayDto;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface ServiceRepository extends JpaRepository<Service, Long> {
    @Modifying
    @Query("UPDATE Service s Set s.completed = :completed WHERE s.serviceId = :serviceId")
    void updCompleted(@Param("serviceId") Long serviceId, int completed);

    @Query("SELECT b.user.userId from Service s join s.product p join p.business b WHERE s.serviceId = :serviceId")
    Long userIdByServiceId(@Param("serviceId") Long serviceId);

    @Query("SELECT s.completed from Service s WHERE s.serviceId = :serviceId")
    int completedByServiceId(@Param("serviceId") Long serviceId);

    @Modifying
    @Query("UPDATE Service s Set s.completed =7 , s.doneAt = now() WHERE s.serviceId = :serviceId")
    void doneCompleted(@Param("serviceId") Long serviceId);

    @Modifying
    @Query("UPDATE Service s Set s.completed =6 , s.paidAt = now() WHERE s.serviceId = :serviceId")
    void paidCompleted(@Param("serviceId") Long serviceId);

    @Modifying
    @Query("UPDATE Service s Set s.tid = :tid WHERE s.serviceId = :serviceId")
    void saveTid(@Param("serviceId") Long serviceId, String tid);

    @Query("SELECT u.userId FROM Service s " +
            "join s.product p join p.business b join b.user u" +
            " WHERE s.serviceId=:serviceId")
    Long providerUserIdByServiceId(Long serviceId);


//    @Query("SELECT a.user.userId, a.price, c.detailTypeName as productName, ifnull(a.tid,0) as tid" +
//            "FROM Service a" +
//            "JOIN a.product b "+
//            "JOIN b.detailType c")
//    KakaoPayDto findServiceInfoByServiceId(@Param("serviceId") Long serviceId);

    @Query("SELECT COUNT(s.serviceId) FROM Service s WHERE s.completed >= 6")
    int getTotalCompletedInfo();
}
