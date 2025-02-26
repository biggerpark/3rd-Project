package com.green.jobdone.business;

import com.green.jobdone.business.model.*;
import com.green.jobdone.business.model.get.*;
import com.green.jobdone.business.phone.BusinessPhonePostReq;
import com.green.jobdone.business.pic.BusinessOnePicsGetReq;
import com.green.jobdone.business.pic.BusinessOnePicsGetRes;
import com.green.jobdone.business.pic.BusinessPicDto;
import com.green.jobdone.business.pic.BusinessPicPostRes;
import com.green.jobdone.common.MyFileUtils;
import com.green.jobdone.common.PicUrlMaker;
import com.green.jobdone.common.model.Domain;
import com.green.jobdone.config.security.AuthenticationFacade;
import com.green.jobdone.entity.Business;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
@Slf4j
@RequiredArgsConstructor
public class BusinessService {

    private final BusinessMapper businessMapper;
    private final MyFileUtils myFileUtils;
    private final AuthenticationFacade authenticationFacade; //인증받은 유저가 이용 할 수 있게.
    //일단 사업등록하기 한번기입하면 수정불가하는 절대적정보
    public final BusinessRepository businessRepository;
    public final BusinessPicRepository businessPicRepository;
    public final Domain domain;


    public static String generateSafeTel(){
        Random random = new Random();

        int n1 = random.nextInt(10000);
        int n2 = random.nextInt(10000);
        return String.format("050-%04d-%04d", n1, n2);
    }


    @Transactional
    public long insBusiness(MultipartFile paper, MultipartFile logo, BusinessPostSignUpReq p) {

        long userId = authenticationFacade.getSignedUserId();
        p.setSignedUserId(userId);


        Random random = new Random();

        int n1 = random.nextInt(10000);
        int n2 = random.nextInt(10000);
        String safeTel = String.format("050%04d%04d", n1, n2);


        // 사업자 등록번호 유효성 체크
        if (p.getBusinessNum() == null || p.getBusinessNum().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "유효하지 않은 사업자 번호입니다");
        }
        //사업자 등록번호 중복체크
        int exists = businessMapper.existBusinessNum(p.getBusinessNum());
        if (exists > 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이미 등록된 사업자 번호입니다");
        }

//       if (userId == 0){
//           throw new IllegalArgumentException("인증되지 않은 유저입니다.");
//       }


        if (paper == null || logo == null) {
            return businessMapper.insBusiness(p);
        }


        String paperPath = String.format("business/%d/paper", p.getBusinessId());
        String logoPath = String.format("business/%d/logo", p.getBusinessId());
        myFileUtils.makeFolders(paperPath);
        myFileUtils.makeFolders(logoPath);
        String savedPicName = (paper != null ? myFileUtils.makeRandomFileName(paper) : null);
        String savedPicName2 = (paper != null ? myFileUtils.makeRandomFileName(logo) : null);
        String filePath = String.format("%s/%s", paperPath, savedPicName);
        String filePath2 = String.format("%s/%s", logoPath, savedPicName2);
        try {
            myFileUtils.transferTo(paper, filePath2);
            myFileUtils.transferTo(paper, filePath);
        } catch (IOException e) {
            log.error(e.getMessage());


        }
        log.debug("Generated safeTel: {}", safeTel);

        p.setSafeTel(safeTel);
        p.setPaper(savedPicName);
        p.setLogo(logoPath);
        log.debug("Generated safeTel: {}", safeTel);
        log.debug("BusinessPostSignUpReq: {}", p.toString());

        return businessMapper.insBusiness(p);

    }

    //사업상세정보 기입 - 로고사진은 따로 뺄게요 ~~


    public int udtBusiness(BusinessDetailPutReq p) {

        long userId = businessRepository.findUserIdByBusinessId(p.getBusinessId());

        long signedUserId = authenticationFacade.getSignedUserId();
        p.setSignedUserId(signedUserId);
        if (userId != signedUserId) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "해당 업체에 대한 권한이 없습니다");
        }
        return businessMapper.udtBusiness(p);
    }


    // 로고 수정
    public int patchBusinessLogo(BusinessLogoPatchReq p, MultipartFile logo) {

        long signedUserId = authenticationFacade.getSignedUserId();

        long userId = businessRepository.findUserIdByBusinessId(p.getBusinessId());
        if (userId != signedUserId) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "해당 업체에 대한 권한이 없습니다");
        }


        // 누락 파일 처리
        if (logo == null || logo.isEmpty()) {
            return 0;
        }

        // 로고파일 저장 폴더 경로
        String folderPath = String.format("business/%d/logo", p.getBusinessId());

        // 기존 로고 폴더가 있다면 폴더 삭제
        myFileUtils.deleteFolder(folderPath, true); // true: 폴더 내 모든 파일 및 하위 폴더 삭제

        // 새 폴더 생성
        myFileUtils.makeFolders(folderPath);

        // 랜덤 파일명 생성
        String savedPicName = myFileUtils.makeRandomFileName(logo); // 사진 등록 후 파일명 만들기
        String newFilePath = String.format("%s/%s", folderPath, savedPicName); // 만약 폴더가 없으면 새로 만들기

        try {
            // 파일을 지정된 경로로 저장
            myFileUtils.transferTo(logo, newFilePath);
            log.info("File successfully saved to: {}", newFilePath);
        } catch (IOException e) {
            // 파일 저장 실패시 처리
            log.error("Error saving file: {}", e.getMessage());
            return 0;
        }

        // DB에 로고 수정 정보 업데이트
        p.setLogo(savedPicName);
        return businessMapper.udtBusinessLogo(p);
    }

    // 사업자등록증 수정
    public int patchBusinessPaper(BusinessPaperPatchReq p, MultipartFile paper) {

        long signedUserId = authenticationFacade.getSignedUserId();

        long userId = businessRepository.findUserIdByBusinessId(p.getBusinessId());
        if (userId != signedUserId) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "해당 업체에 대한 권한이 없습니다");
        }

        // 누락 파일 처리
        if (paper == null || paper.isEmpty()) {
            return 0;
        }

        // 로고파일 저장 폴더 경로
        String folderPath = String.format("business/%d/paper", p.getBusinessId());

        // 기존 로고 폴더가 있다면 폴더 삭제
        myFileUtils.deleteFolder(folderPath, true); // true: 폴더 내 모든 파일 및 하위 폴더 삭제

        // 새 폴더 생성
        myFileUtils.makeFolders(folderPath);

        // 랜덤 파일명 생성
        String savedPicName = myFileUtils.makeRandomFileName(paper); // 사진 등록 후 파일명 만들기
        String newFilePath = String.format("%s/%s", folderPath, savedPicName); // 만약 폴더가 없으면 새로 만들기

        try {
            // 파일을 지정된 경로로 저장
            myFileUtils.transferTo(paper, newFilePath);
            log.info("File successfully saved to: {}", newFilePath);
        } catch (IOException e) {
            // 파일 저장 실패시 처리
            log.error("Error saving file: {}", e.getMessage());
            return 0;
        }

        // DB에 로고 수정 정보 업데이트
        p.setPaper(savedPicName);
        return businessMapper.udtBusinessPaper(p);
    }


    @Transactional
    public BusinessPicPostRes insBusinessPic(List<MultipartFile> pics, long businessId) {

        long signedUserId = authenticationFacade.getSignedUserId();

        long userId = businessMapper.existBusinessId(businessId);
        if (userId != signedUserId) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "해당 업체에 대한 권한이 없습니다");
        }


        String middlePath = String.format("business/%d/pics", businessId);
        myFileUtils.makeFolders(middlePath);

        List<String> businessPicList = new ArrayList<>(pics.size());
        for (MultipartFile pic : pics) {
            //pics리스트에 있는 사진들 전수조사
            String savedPicName = myFileUtils.makeRandomFileName(pic);

            businessPicList.add(savedPicName);
            String filePath = String.format("%s/%s", middlePath, savedPicName);
            try {
                myFileUtils.transferTo(pic, filePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        BusinessPicDto businessPicDto = new BusinessPicDto();
        businessPicDto.setBusinessId(businessId);
        businessPicDto.setPics(businessPicList);
        int resultPics = businessMapper.insBusinessPic(businessPicDto);

        return BusinessPicPostRes.builder().businessId(businessId).pics(businessPicList).build();
    }

    @Transactional
    public BusinessContentsPostRes postBusinessContents(BusinessContentsPostReq p) {

        long signedUserId = authenticationFacade.getSignedUserId();

        Business business = businessRepository.findById(p.getBusinessId())
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"너 누구냐"));

        if (business.getUser().getUserId() != signedUserId) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "해당 업체에 대한 권한이 없습니다, 근데 너 누구냐");
        }

        business.setTitle(p.getTitle());
        business.setContents(p.getContents());

        businessRepository.updateBusinessContents(p);

        return new BusinessContentsPostRes(business.getTitle(),business.getContents());
    }

    @Transactional
    public BusinessPicPostRes businessPicTemp(List<MultipartFile> pics, long businessId) {
        long signedUserId = authenticationFacade.getSignedUserId();

        Business business = businessRepository.findById(businessId)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"너 누구냐"));

        if (business.getUser().getUserId() != signedUserId) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "해당 업체에 대한 권한이 없습니다, 근데 너 누구냐");
        } //일단 보안먼저 챙겨주고


        String tempPath = String.format("business/%d/temp", businessId);
        myFileUtils.makeFolders(tempPath);

        List<String> tempPicUrls = new ArrayList<>(pics.size());
        List<String> businessPicList = new ArrayList<>(pics.size());
        for (MultipartFile pic : pics) {
            String savedPicName = myFileUtils.makeRandomFileName(pic);
            String filePath = String.format("%s/%s", tempPath, savedPicName);

            String tempPicUrl = String.format("%s/%s",domain.getServer(),filePath);

            try{
                myFileUtils.transferTo(pic,filePath);
            }catch (IOException e) {
                e.printStackTrace();
            }

            businessPicList.add(savedPicName);
            tempPicUrls.add(tempPicUrl);
        }
        BusinessPicDto businessPicDto = new BusinessPicDto();
        businessPicDto.setBusinessId(businessId);
        businessPicDto.setPics(businessPicList);
        int resultPics = businessMapper.insBusinessPic(businessPicDto);

        return  BusinessPicPostRes.builder().businessId(businessId).pics(tempPicUrls).build();
    }

    @Value("${file.directory}")
    private String fileDirectory;

    @Transactional
    public boolean businessPicConfirm( long businessId) {
        long signedUserId = authenticationFacade.getSignedUserId();

        Business business = businessRepository.findById(businessId)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"너 누구냐"));

        if (business.getUser().getUserId() != signedUserId) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "해당 업체에 대한 권한이 없습니다, 근데 너 누구냐");
        }

        String tempPath = String.format("%s/business/%d/temp", fileDirectory, businessId);
        String middlePath = String.format("%s/business/%d/pics", fileDirectory, businessId);
        myFileUtils.makeFolders(middlePath);

        boolean moveSuccess = myFileUtils.moveFolder(tempPath,middlePath);

        return moveSuccess;
    }




    public int udtBusinessPics(long businessPicId) {
        return businessMapper.putBusinessPic(businessPicId);
    }

    public int udtBusinessState(BusinessStatePutReq p) {

        return businessMapper.putBusinessState(p);
    }

    public int setBusinessThumbnail(BusinessPicReq p) {
        return businessMapper.udtBusinessThumbnail(p);
    }


    public int delBusinessPic(BusinessPicReq p) {
        //String uploadPath = myFileUtils.getUploadPath();
        String businessPicName = businessMapper.getBusinessPicName(p.getBusinessPicId());
        String filePath = String.format("business/%d/pics/%s",  p.getBusinessId(), businessPicName);

        log.info("Generated file path: {}", filePath);  // 경로 출력

        // 파일 삭제 시도
        if (myFileUtils.deleteFile(filePath)) {  // 단일 파일 삭제 시도
            log.info("File successfully deleted: {}", filePath);  // 삭제 성공 로그
        } else {
            log.error("Failed to delete file: {}", filePath);  // 삭제 실패 로그
        }

        // DB에서 해당 사진 정보 삭제
        return businessMapper.delBusinessPic(p);
    }

    //업체 조회하기
    // 1. 업체 리스트 조회
    public List<BusinessGetRes> getBusinessList(BusinessGetReq p) {
        // 업체 리스트 조회
        List<BusinessGetRes> res = businessMapper.selAllBusiness(p);

        // 각 비즈니스 객체마다 사진 경로 생성
        for (BusinessGetRes business : res) {
            // 비즈니스 객체의 pic 필드를 이용하여 사진 경로 생성
            String picUrl = PicUrlMaker.makePicUrlBusiness(business.getBusinessId(), business.getPic());
            business.setPic(picUrl);  // 사진 경로 업데이트
        }

        return res;
    }

    // 2. 단일업체 조회
    public BusinessGetOneRes getBusinessOne(BusinessGetOneReq p) {
        BusinessGetOneRes res = businessMapper.selOneBusiness(p.getBusinessId());
        if (res == null) {
            res = new BusinessGetOneRes();  // res가 null일 경우 새로운 객체 생성
        }

        if (p.getBusinessId() > 0) {
            res.setBusinessId(p.getBusinessId());
        }

        if (res != null && res.getLogo() != null) {
            res.setLogo(PicUrlMaker.makePicUrlLogo(p.getBusinessId(), res.getLogo()));
        }
        return res;
    }

    //업체 하나에 있는 사진들
    public List<BusinessOnePicsGetRes> getBusinessOnePics(BusinessOnePicsGetReq p) {

        List<BusinessOnePicsGetRes> res = businessMapper.getBusinessPicList(p);
        for (BusinessOnePicsGetRes pic : res) {
            // 비즈니스 객체의 pic 필드를 이용하여 사진 경로 생성
            String picUrl = PicUrlMaker.makePicUrlBusiness(pic.getBusinessId(), pic.getPic());
            pic.setPic(picUrl);  // 사진 경로 업데이트
        }
        return res;
    }


    public int insBusinessPhone(BusinessPhonePostReq p) {
        int exists = businessMapper.existBusinessPhone(p.getBusinessId(), p.getPhone());
        if (exists > 0) {
            throw new IllegalArgumentException("이미 존재하는 전화번호입니다");
        }
        return businessMapper.insBusinessPhone(p);
    }



    public List<BusinessGetMonthlyRes> getBusinessMonthly(BusinessGetInfoReq p){


            long userId = businessMapper.existBusinessId(p.getBusinessId());

            long signedUserId = authenticationFacade.getSignedUserId();
            p.setSignedUserId(signedUserId);
            if (userId != signedUserId) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "해당 업체에 대한 권한이 없습니다");
            }

        return businessMapper.getBusinessMonthly(p);
    }

    public int getBusinessService(BusinessGetInfoReq p) {
        long userId = businessMapper.existBusinessId(p.getBusinessId());

        long signedUserId = authenticationFacade.getSignedUserId();
        p.setSignedUserId(signedUserId);
        if (userId != signedUserId) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "해당 업체에 대한 권한이 없습니다");
        }
        return businessRepository.countBusinessServices(p.getBusinessId()); //이거 마저 해야해요
    }
}




















