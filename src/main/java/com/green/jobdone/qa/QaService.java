package com.green.jobdone.qa;


import com.green.jobdone.admin.model.AdminUserInfoRes;
import com.green.jobdone.common.PicUrlMaker;
import com.green.jobdone.config.security.AuthenticationFacade;
import com.green.jobdone.entity.*;
import com.green.jobdone.qa.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class QaService {
    private final QaRepository qaRepository;
    private final QaTypeDetailRepository qaTypeDetailRepository;
    private final AuthenticationFacade authenticationFacade;
    private final QaMapper qaMapper;
    private final QaAnswerRepository qaAnswerRepository;


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

    @Transactional
    public List<QaRes> getQa(int page) {
        int offset = (page - 1) * 10;

        List<QaRes> res = qaMapper.getQa(offset);


        for (QaRes item : res) {
            String type = switch (item.getUserTypeDB()) {
                case 100 -> "일반유저";
                case 110 -> "업체 직원";
                case 120 -> "업체 매니저";
                case 130 -> "업체 사장";
                case 140 -> "프리랜서";
                default -> null;
            };

            item.setUserType(type);
        }


        return res;
    }

    public QaDetailRes getQaDetail(long qaId) {
        QaDetailRes res = qaMapper.getQaDetail(qaId);


        List<String> updatedPics = new ArrayList<>();
        for (String item : res.getPics()) {
            item = PicUrlMaker.makePicQa(qaId, item);
            updatedPics.add(item);
        }
        res.setPics(updatedPics);

        return res;
    }

    @Transactional
    public int postQaAnswer(QaAnswerReq p){
       Qa qa=Qa.builder()
               .qaId(p.getQaId())
               .build();

       Admin admin = new Admin();
       admin.setAdminId(1L); // 나중에 JWT 넣기

       QaAnswer qaAnswer=QaAnswer.builder()
               .qa(qa)
               .answer(p.getAnswer())
               .admin(admin)
               .build();

       qaAnswerRepository.save(qaAnswer);

       return 1;
    }


    public List<QaReportRes> getQaReport(int page) {
       int offset = (page - 1) * 10;


       List<QaReportRes> res = qaMapper.getQaReport(offset);

       return res;

    }



}


