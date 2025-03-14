package com.green.jobdone.qa;


import com.green.jobdone.admin.model.AdminUserInfoRes;
import com.green.jobdone.common.MyFileUtils;
import com.green.jobdone.common.PicUrlMaker;
import com.green.jobdone.common.exception.CommonErrorCode;
import com.green.jobdone.common.exception.CustomException;
import com.green.jobdone.common.exception.ServiceErrorCode;
import com.green.jobdone.common.exception.UserErrorCode;
import com.green.jobdone.config.jwt.JwtUser;
import com.green.jobdone.config.jwt.UserRole;
import com.green.jobdone.config.security.AuthenticationFacade;
import com.green.jobdone.entity.*;
import com.green.jobdone.qa.model.*;
import com.green.jobdone.service.ServiceRepository;
import com.green.jobdone.service.model.Dto.ServiceQaDto;
import com.green.jobdone.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    private final ServiceRepository serviceRepository;
    private final QaViewRepository qaViewRepository;
    private final UserRepository userRepository;


    @Transactional(noRollbackFor = CustomException.class)
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
                .title(p.getTitle())
                .qaTypeDetail(qaTypeDetail)
                .contents(p.getContents())
                .reportReason(p.getQaReportReason())
                .qaTargetId(p.getQaTargetId())
                .build();
        if (pics == null) {
            qaRepository.save(qa);
        }
        if (p.getQaReportReason().getCode() == 4) {
            ServiceQaDto qaDto = serviceRepository.findQaDtoByServiceId(p.getQaTargetId());
            if (qaDto == null || qaDto.getCompleted() < 6) {
                throw new CustomException(ServiceErrorCode.FAIL_UPDATE_SERVICE);
            }
            if (qaDto.getDoneAt() == null) {
                updCompleted(qaDto.getCompleted(), p.getQaTargetId());
                return;
            }

            if (qaDto.getDoneAt().isBefore(LocalDateTime.now().minusWeeks(1))) {
                serviceRepository.updCompleted(p.getQaTargetId(), 13);
                throw new CustomException(ServiceErrorCode.TIME_OVER);
            }
            updCompleted(qaDto.getCompleted(), p.getQaTargetId());
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

    private void updCompleted(int completed, Long targetId) {
        switch (completed) {
            case 7:
            case 8:
            case 9:
                serviceRepository.updCompleted(targetId, 10);
                break;
            default:
                throw new CustomException(ServiceErrorCode.INVALID_SERVICE_STATUS);
        }
    }

    @Transactional
    public List<QaRes> getQa() {

        JwtUser jwtUser = authenticationFacade.getSignedUser();
        long signedUserId = authenticationFacade.getSignedUserId(); // 일반 유저일때 사용할 유저 pk

        boolean isAdmin = jwtUser.getRoles().contains(UserRole.ADMIN); // Admin 인지 판단하는것

//        if(jwtUser.getRoles().contains(UserRole.ADMIN)){
        List<QaRes> res = qaMapper.getQa(isAdmin, signedUserId); // 관리자일때 가져오는 RES

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
        boolean isAdmin = jwtUser.getRoles().contains(UserRole.ADMIN);// 관리자인지 판단하는 boolean


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

            if (!"00103".equals(qa.getQaState())) { // 🔥 상태가 "답변 완료(00103)"이면 변경하지 않음!
                qa.setQaState("00102"); // 검토중으로 변경
                qaRepository.save(qa);
            }
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
    public QaAnswerRes getQaAnswer(long qaId) { // 관리자가 답변한 문의 답변 확인

        if (qaMapper.getQaAnswer(qaId) == null) {
            throw new CustomException(CommonErrorCode.NOT_ANSWER);
        }

        return qaMapper.getQaAnswer(qaId);

    }


    @Transactional
    public List<QaTypeDetailRes> getQaTypeDetail(long qaTypeId) {

        return qaMapper.getQaTypeDetail(qaTypeId);

    }


    @Transactional
    public QaDetailRes getQaBoardDetail(long qaId) {
        long signedUserId = authenticationFacade.getSignedUserId();


        Optional<QaView> qaViewOptional = qaViewRepository.findByQaViewsIds_QaIdAndQaViewsIds_UserId(qaId, signedUserId); // 복합키를 이용해서 QaView ENTITY 설정

        if (qaViewOptional.isPresent()) {
            QaView qaView = qaViewOptional.get();
            if (qaView.canIncreaseViewCount()) {
                qaView.increaseViewCount();
                qaViewRepository.save(qaView);
            }
        } else { //본적 없는 userId 인 경우
            QaViewsIds id = new QaViewsIds(qaId, signedUserId);
            QaView newQaView = new QaView();
            newQaView.setQaViewsIds(id);
            newQaView.setViewCount(1);

            // Qa 객체를 가져와서 설정
            Qa qa = qaRepository.findById(qaId).orElseThrow(() -> new RuntimeException("Qa not found with id " + qaId));
            User user = userRepository.findById(signedUserId).orElseThrow(() -> new RuntimeException("User not found with id " + signedUserId));

            // 복합키 설정 및 QaView 객체에 연결
            newQaView.setQa(qa);  // Qa 설정
            newQaView.setUser(user);  // User 설정

            qaViewRepository.save(newQaView);
        }
        // 위 로직은 DB 에 userId,qaId 집어넣고 조회수 증가시켜주는 로직


        QaDetailRes result = qaMapper.getQaDetail(qaId);

        List<String> updatedPics = new ArrayList<>();
        for (String item : result.getPics()) {
            item = PicUrlMaker.makePicQa(qaId, item);
            updatedPics.add(item);
        }
        result.setPics(updatedPics);


        return result; // 성공 시 반환값

    }

    @Transactional
    public List<QaBoardRes> getQaBoard() {

        return qaMapper.getQaBoard();
    }

    @Transactional
    public int deleteQa(long qaId) {


        qaRepository.deleteById(qaId);

        String deletePath = String.format("qa/%d", qaId);
        myFileUtils.deleteFolder(deletePath, true);


        return 1;
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


