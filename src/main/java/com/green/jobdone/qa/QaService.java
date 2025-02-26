package com.green.jobdone.qa;


import com.green.jobdone.config.security.AuthenticationFacade;
import com.green.jobdone.entity.Qa;
import com.green.jobdone.entity.QaTypeDetail;
import com.green.jobdone.entity.ReportReason;
import com.green.jobdone.entity.User;
import com.green.jobdone.qa.model.QaReq;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class QaService {
    private final QaRepository qaRepository;
    private final QaTypeDetailRepository qaTypeDetailRepository;
    private final AuthenticationFacade authenticationFacade;

    @Transactional
    public void insQa(QaReq p) {
        Long userId = authenticationFacade.getSignedUserId();
        User user = new User();
        user.setUserId(userId);
        QaTypeDetail qaTypeDetail =
                QaTypeDetail.builder()
                .qaTypeDetailId(p.getQaTypeDetailId())
                .build();
        Qa qa = Qa.builder()
                .user(user)
                .qaTypeDetail(qaTypeDetail)
                .contents(p.getContents())
                .reportReason(p.getQaReportReason())
                .qaTargetId(p.getQaTargetId())
                .build();
        qaRepository.save(qa);
    }


}
