package com.green.jobdone.qa;


import com.green.jobdone.admin.model.AdminUserInfoRes;
import com.green.jobdone.common.MyFileUtils;
import com.green.jobdone.common.PicUrlMaker;
import com.green.jobdone.config.jwt.JwtUser;
import com.green.jobdone.config.jwt.UserRole;
import com.green.jobdone.config.security.AuthenticationFacade;
import com.green.jobdone.entity.*;
import com.green.jobdone.qa.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
    private final QaPicRepository qaPicRepository;
    private final MyFileUtils myFileUtils;


    @Transactional
    public void insQa(QaReq p, List<MultipartFile> pics) {
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
        if(pics==null){
            qaRepository.save(qa);
        }

        if(pics!=null){
            qaRepository.save(qa);
            long qaId=qa.getQaId();
            String middlePath = String.format("qa/%d", qaId);
            myFileUtils.makeFolders(middlePath);

            for(MultipartFile pic : pics){
                String picName = myFileUtils.makeRandomFileName(pic);
                QaPic qaPic=QaPic.builder()
                        .qa(qa)
                        .pic(picName)
                        .build();
                qaPicRepository.save(qaPic);

                String filePath = String.format("%s/%s", middlePath, picName);
                try {
                    myFileUtils.transferTo(pic, filePath);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }


        }
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

    @Transactional
    public QaDetailRes getQaDetail(long qaId) { // 문의상세내역 확인
        JwtUser jwtUser = authenticationFacade.getSignedUser(); // 관리자인지 일반유저인지 판단하기.

        QaDetailRes res = qaMapper.getQaDetail(qaId);


        List<String> updatedPics = new ArrayList<>();
        for (String item : res.getPics()) {
            item = PicUrlMaker.makePicQa(qaId, item);
            updatedPics.add(item);
        }
        res.setPics(updatedPics);

        if(jwtUser.getRoles().contains(UserRole.ADMIN)){ // 권한이 관리자가 있으면 qa state 를 진행중으로 바꿔주기
            Qa qa=Qa.builder()
                    .qaId(qaId)
                    .qaState("00102")
                    .build();
            qaRepository.save(qa);
        }

        return res;
    }

    @Transactional
    public int postQaAnswer(QaAnswerReq p){ // 관리자측 문의 답변
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



    public List<QaReportRes> getQaReport(int page) { // 신고내역 확인
       int offset = (page - 1) * 10;


       List<QaReportRes> res = qaMapper.getQaReport(offset);

       return res;

    }



}


