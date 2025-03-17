package com.green.jobdone.service;

import com.green.jobdone.entity.Service;
import com.green.jobdone.service.model.Dto.CancelDto;
import com.green.jobdone.service.model.Dto.KakaoPayDto;
import com.green.jobdone.service.model.Dto.ServiceQaDto;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDateTime;

public interface ServiceRepository extends JpaRepository<Service, Long> {
    @Modifying
    @Query("UPDATE Service s Set s.completed = :completed WHERE s.serviceId = :serviceId")
    void updCompleted(@Param("serviceId") Long serviceId, int completed);

    @Query("SELECT b.user.userId from Service s join s.product p join p.business b WHERE s.serviceId = :serviceId")
    Long userIdByServiceId(@Param("serviceId") Long serviceId);

    @Query("SELECT s.completed from Service s WHERE s.serviceId = :serviceId")
    int completedByServiceId(@Param("serviceId") Long serviceId);
//    @Modifying
//    @Query("update Service s set s.completed = 13 where s.paidAt <:week and s.completed not in(10, 11, 12)")
//    void updCompletedByDate(LocalDateTime week); 1시간마다라 애매해져서 그냥 신청할때 채크한 뒤 보내기로

//    @Modifying
//    @Query("UPDATE Service s Set s.completed =7 , s.doneAt = now() WHERE s.serviceId = :serviceId")
//    void doneCompleted(@Param("serviceId") Long serviceId);
//
//    @Modifying
//    @Query("UPDATE Service s Set s.completed =6 , s.paidAt = now() WHERE s.serviceId = :serviceId")
//    void paidCompleted(@Param("serviceId") Long serviceId); // 위의 updCompleted 로 통일

    @Modifying
    @Query("UPDATE Service s Set s.tid = :tid WHERE s.serviceId = :serviceId")
    void saveTid(@Param("serviceId") Long serviceId, String tid);

    @Query("SELECT u.userId FROM Service s " +
            "join s.product p join p.business b join b.user u" +
            " WHERE s.serviceId=:serviceId")
    Long providerUserIdByServiceId(Long serviceId);

    @Query("select new com.green.jobdone.service.model.Dto.CancelDto(s.tid ,s.user.userId, s.totalPrice, s.completed, d.startDate) " +
            "from ServiceDetail d join d.service s where s.serviceId =:serviceId")
    CancelDto findCancelDtoByServiceId(@Param("serviceId") Long serviceId);

    @Query("select new com.green.jobdone.service.model.Dto.ServiceQaDto(s.completed, s.doneAt, s.paidAt) from Service s where s.serviceId =:serviceId")
    ServiceQaDto findQaDtoByServiceId(@Param("serviceId") Long serviceId);


//    @Query("SELECT a.user.userId, a.price, c.detailTypeName as productName, ifnull(a.tid,0) as tid" +
//            "FROM Service a" +
//            "JOIN a.product b "+
//            "JOIN b.detailType c")
//    KakaoPayDto findServiceInfoByServiceId(@Param("serviceId") Long serviceId);

    @Query("SELECT COUNT(s.serviceId) FROM Service s WHERE s.completed >= 6")
    int getTotalCompletedInfo();

    @Transactional
    @Modifying
    @Query("UPDATE Service s SET s.completed =:completed , " +
            "s.paidAt = CASE WHEN :completed = 6 AND s.paidAt IS NULL THEN CURRENT_TIMESTAMP ELSE s.paidAt END, " +
            "s.doneAt = CASE WHEN :completed = 7 AND s.doneAt IS NULL THEN CURRENT_TIMESTAMP ELSE s.doneAt END " +
            "WHERE s.serviceId = :serviceId")
    void updateServiceStatus(@Param("serviceId") Long serviceId, @Param("completed") int completed);
    //차라리 6 7을 받고나서 직접 여기서 처리하면?
}
