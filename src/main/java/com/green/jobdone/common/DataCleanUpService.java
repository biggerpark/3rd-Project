package com.green.jobdone.common;

import com.green.jobdone.user.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class DataCleanUpService {
    @PersistenceContext
    private EntityManager em;
    private final UserRepository userRepository;

    @Scheduled(cron = "0 0 * * * *")
    @Transactional
    public void deleteHour(){
        String sql = "DELETE FROM business_pic WHERE createdAt < CURRENT_TIMESTAMP - INTERVAL 1 DAY AND state = 1";
        em.createNativeQuery(sql).executeUpdate();
    }
    
    @Scheduled(cron = "0 0 0 * * *")
    @Transactional
    public void deleteDay(){
        userRepository.deleteByDays(LocalDateTime.now().minusMonths(6));
    }

}
