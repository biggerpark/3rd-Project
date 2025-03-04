package com.green.jobdone.qa;


import com.green.jobdone.admin.model.AdminUserInfoRes;
import com.green.jobdone.common.MyFileUtils;
import com.green.jobdone.common.PicUrlMaker;
import com.green.jobdone.common.exception.CommonErrorCode;
import com.green.jobdone.common.exception.CustomException;
import com.green.jobdone.common.exception.UserErrorCode;
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
        if (pics == null) {
            qaRepository.save(qa);
        }

        if (pics != null) {
            qaRepository.save(qa);
            long qaId = qa.getQaId();
            String middlePath = String.format("qa/%d", qaId);
            myFileUtils.makeFolders(middlePath);

            for (MultipartFile pic : pics) {
                String picName = myFileUtils.makeRandomFileName(pic);
                QaPic qaPic = QaPic.builder()
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
        JwtUser jwtUser = authenticationFacade.getSignedUser();
        long signedUserId = authenticationFacade.getSignedUserId(); // 일반 유저일때 사용할 유저 pk

        boolean isAdmin = jwtUser.getRoles().contains(UserRole.ADMIN); // Admin 인지 판단하는것

//        if(jwtUser.getRoles().contains(UserRole.ADMIN)){
        List<QaRes> res = qaMapper.getQa(offset,isAdmin,signedUserId); // 관리자일때 가져오는 RES

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
        boolean isAdmin=jwtUser.getRoles().contains(UserRole.ADMIN);// 관리자인지 판단하는 boolean


        long signedUserId = authenticationFacade.getSignedUserId(); // 유저일때 사용할 유저 pk, qaId 로 판단돼서 필요없을듯

        QaDetailRes res = qaMapper.getQaDetail(qaId);


        List<String> updatedPics = new ArrayList<>();
        for (String item : res.getPics()) {
            item = PicUrlMaker.makePicQa(qaId, item);
            updatedPics.add(item);
        }
        res.setPics(updatedPics);

        if (isAdmin) { // 권한이 관리자가 있으면 qa state 를 진행중으로 바꿔주기
            Qa qa = qaRepository.findById(qaId)
                    .orElseThrow(() -> new RuntimeException("해당 QA가 존재하지 않습니다."));

            qa.setQaState("00102"); // state 를 검토중 으로 바꿔줌.


            qaRepository.save(qa);

        }

        return res;
    }

    @Transactional
    public int postQaAnswer(QaAnswerReq p) { // 관리자측 문의 답변
        List<UserRole> userRole = authenticationFacade.getSignedUser().getRoles();

        if (!userRole.contains(UserRole.ADMIN)) {
            throw new CustomException(UserErrorCode.FORBIDDEN_ACCESS);
        }

        Qa qa = qaRepository.findById(p.getQaId())
                .orElseThrow(() -> new RuntimeException("해당 QA가 존재하지 않습니다."));

        qa.setQaState("00103");


        Admin admin = new Admin();
        admin.setAdminId(1L); // 나중에 JWT 넣기


        QaAnswer qaAnswer = QaAnswer.builder()
                .qa(qa)
                .answer(p.getAnswer())
                .admin(admin)
                .build();


        qaRepository.save(qa);
        qaAnswerRepository.save(qaAnswer);


        return 1;
    }

    @Transactional
    public QaAnswerRes getQaAnswer(long qaId){

        if(qaMapper.getQaAnswer(qaId)==null){
            throw new CustomException(CommonErrorCode.NOT_ANSWER);
        }

        return qaMapper.getQaAnswer(qaId);

    }


//    public List<QaReportRes> getQaReport(int page) { // 신고내역 확인
//        int offset = (page - 1) * 10;
//
//
//        List<QaReportRes> res = qaMapper.getQaReport(offset);
//
//        return res;
//
//    }


}


