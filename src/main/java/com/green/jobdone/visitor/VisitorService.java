package com.green.jobdone.visitor;

import com.green.jobdone.entity.VisitorCount;
import com.green.jobdone.entity.VisitorHistory;
import com.green.jobdone.entity.VisitorLog;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;


import java.time.LocalDate;

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

    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");

        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr(); // 기본값 (프록시 없는 경우)
        } else {
            // X-Forwarded-For에 여러 개의 IP가 있을 경우, 첫 번째 것이 실제 IP
            ip = ip.split(",")[0].trim();
        }
        return ip;
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

    @Scheduled(cron = "0 0 0 * * *")
    public void resetVisitorCount() {
        VisitorCount visitorCount = visitorCountRepository.findById(1L).orElse(new VisitorCount());
        int yesterdayCount = visitorCount.getCount();
        visitorHistoryRepository.save(new VisitorHistory(LocalDate.now().minusDays(1), yesterdayCount));
        visitorCount.setCount(0);
        visitorCountRepository.save(visitorCount);
        System.out.println("✅ 방문자 수 초기화 완료 | 전날 방문자 수: " + yesterdayCount);
    }
}
