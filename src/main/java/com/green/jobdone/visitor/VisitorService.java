package com.green.jobdone.visitor;

import com.green.jobdone.entity.VisitorCount;
import com.green.jobdone.entity.VisitorHistory;
import com.green.jobdone.entity.VisitorLog;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;


import java.time.LocalDate;

@Slf4j
@Service
@Transactional
class VisitorService {
    private final VisitorCountRepository visitorCountRepository;
    private final VisitorHistoryRepository visitorHistoryRepository;
    private final VisitorLogRepository visitorLogRepository;

    public VisitorService(VisitorCountRepository visitorCountRepository, VisitorHistoryRepository visitorHistoryRepository, VisitorLogRepository visitorLogRepository) {
        this.visitorCountRepository = visitorCountRepository;
        this.visitorHistoryRepository = visitorHistoryRepository;
        this.visitorLogRepository = visitorLogRepository;
    }

    // 클라이언트가 접속한 아이피 따는데 도커로 하면 도커자체 아이피로 고정됨
    private String getClientIp(HttpServletRequest request) {
        log.info("getClientIp - getRemoteAddr: {}", request.getRemoteAddr());
        String ip = request.getHeader("X-Forwarded-For");
        if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
            log.info("getClientIp - 1");
            return ip.split(",")[0].trim();  // 첫 번째 IP가 실제 클라이언트 IP
        }

        ip = request.getHeader("Proxy-Client-IP");
        if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
            log.info("getClientIp - 2");
            return ip;
        }

        ip = request.getHeader("WL-Proxy-Client-IP");
        if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
            log.info("getClientIp - 3");
            return ip;
        }

        ip = request.getHeader("HTTP_CLIENT_IP");
        if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
            log.info("getClientIp - 4");
            return ip;
        }

        ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
            log.info("getClientIp - 5");
            return ip;
        }
        log.info("getClientIp - 6");
        return request.getRemoteAddr();  // 마지막으로 기본 요청 IP 사용
    }
    /*
    private String getClientIp() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            return request.getRemoteAddr(); // 실제 배포 환경에서는 X-Forwarded-For 처리 필요
        }
        return "UNKNOWN";
    }
    */

    public void incrementVisitor(HttpServletRequest request) {
        String ipAddress = getClientIp(request);
        LocalDate today = LocalDate.now();

        // 같은 IP가 오늘 이미 방문했는지 확인
        if (visitorLogRepository.existsByIpAddressAndVisitDate(ipAddress, today)) {
            System.out.println("🚫 이미 방문한 IP입니다: " + ipAddress);
            return;
        }

        // 방문 기록 저장
        visitorLogRepository.save(new VisitorLog(ipAddress, today));

        // 방문자 수 증가
        VisitorCount visitorCount = visitorCountRepository.findById(1L).orElse(new VisitorCount());
        visitorCount.setCount(visitorCount.getCount() + 1);
        visitorCountRepository.save(visitorCount);

        System.out.println("✅ 새로운 방문 기록 저장: " + ipAddress);
    }

    public int getVisitorCount() {
        return visitorCountRepository.findById(1L).map(VisitorCount::getCount).orElse(0);
    }

    @Scheduled(cron = "0 1 0 * * *")
    public void resetVisitorCount() {
        VisitorCount visitorCount = visitorCountRepository.findById(1L).orElse(new VisitorCount());
        int yesterdayCount = visitorCount.getCount();
        visitorHistoryRepository.save(new VisitorHistory(LocalDate.now().minusDays(1), yesterdayCount));
        visitorCount.setCount(0);
        visitorCountRepository.save(visitorCount);
        System.out.println("✅ 방문자 수 초기화 완료 | 전날 방문자 수: " + yesterdayCount);
    }
}
