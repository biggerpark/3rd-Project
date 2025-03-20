package com.green.jobdone.business;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.green.jobdone.business.model.*;
import com.green.jobdone.business.model.get.*;
import com.green.jobdone.business.pic.BusinessOnePicsGetReq;
import com.green.jobdone.business.pic.BusinessOnePicsGetRes;
import com.green.jobdone.business.pic.BusinessPicPostRes;
import com.green.jobdone.category.DetailTypeRepository;
import com.green.jobdone.common.MyFileUtils;
import com.green.jobdone.common.PicUrlMaker;
import com.green.jobdone.common.model.Domain;
import com.green.jobdone.config.security.AuthenticationFacade;
import com.green.jobdone.entity.Business;
import com.green.jobdone.entity.BusinessPic;
import com.green.jobdone.user.UserRepository;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
@Slf4j
@RequiredArgsConstructor
@Validated
public class BusinessService {



    @Value("${file.directory}")
    private String fileDirectory;

    @Value("${domain.path.server}")
    private String docker;

    private final BusinessMapper businessMapper;
    private final MyFileUtils myFileUtils;
    private final AuthenticationFacade authenticationFacade; //인증받은 유저가 이용 할 수 있게.
    //일단 사업등록하기 한번기입하면 수정불가하는 절대적정보
    public final BusinessRepository businessRepository;
    public final BusinessPicRepository businessPicRepository;
    public final Domain domain;

    private final UserRepository userRepository;
    private final DetailTypeRepository detailTypeRepository;

    private final Validator validator;

    @Transactional
    public long insBusiness(MultipartFile paper, MultipartFile logo,  BusinessPostSignUpReq p) {


        long userId = authenticationFacade.getSignedUserId();
        p.setSignedUserId(userId);


        Random random = new Random();

        String safeTel = String.format("050-%04d-%04d", random.nextInt(10000), random.nextInt(10000));


        // 사업자 등록번호 유효성 체크
        if (p.getBusinessNum() == null || p.getBusinessNum().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "유효하지 않은 사업자 번호입니다");
        }

        //사업자 등록번호 중복체크
        int exists = businessRepository.findExistBusinessNum(p.getBusinessNum());
        if (exists > 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이미 등록된 사업자 번호입니다");
        }



        Business business = new Business();
        business.setUser(userRepository.findById(userId).orElse(null));
        business.setDetailType(detailTypeRepository.findById(p.getDetailTypeId()).orElse(null));
        business.setBusinessNum(p.getBusinessNum());
        business.setBusinessName(p.getBusinessName());
        business.setAddress(p.getAddress());
        business.setBusiCreatedAt(p.getBusiCreatedAt());
        business.setTel(p.getTel());
        business.setState(100);
        business.setSafeTel(safeTel);


        businessRepository.save(business);
        long businessId = business.getBusinessId();


        String paperPath = String.format("business/%d/paper", businessId);
        String logoPath = String.format("business/%d/logo", businessId);
        myFileUtils.makeFolders(paperPath);
        myFileUtils.makeFolders(logoPath);
        log.info("파일 저장 경로: {}", myFileUtils.getUploadPath());

        String savedPaperName = (paper != null ? myFileUtils.makeRandomFileName(paper) : null);
        String savedLogoName = (logo != null ? myFileUtils.makeRandomFileName(logo) : null);

        if (savedPaperName == null || savedLogoName == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "파일 이름 생성 실패");
        }

        String paperFilePath = String.format("%s/%s", paperPath, savedPaperName);
        String logoFilePath = String.format("%s/%s", logoPath, savedLogoName);

        try {
            if (paper != null) myFileUtils.transferTo(paper, paperFilePath);
            if (logo != null) myFileUtils.transferTo(logo, logoFilePath);
        } catch (IOException e) {
            log.error("error occurs:{}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());

        }
        log.debug("Generated safeTel: {}", safeTel);

        business.setLogo(savedLogoName);
        business.setPaper(savedPaperName);
        return businessRepository.save(business).getBusinessId();
    }

    @Value("${kakao.map.api-key}")
    String apikey = "apiKey";

    public double[] getCoordinatesFromAddress(String address) {
        String url = "https://dapi.kakao.com/v2/local/search/address.json?query=" + address;

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "KakaoAK " + apikey);

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        String responseBody = response.getBody();

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(responseBody);

            JsonNode documents = rootNode.get("documents");
            if (documents.isArray() && documents.size() > 0) {
                JsonNode firstResult = documents.get(0);
                double latitude = firstResult.get("x").asDouble(); // 위도
                double longitude = firstResult.get("y").asDouble(); // 경도
                return new double[]{latitude, longitude};
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new double[0];
    }

    ;

    //사업상세정보 기입 - 로고사진은 따로 뺄게요 ~~
    @Transactional
    public int patchBusinessThumbnail(BusinessPatchThumbnailReq p, MultipartFile thumbnail) {

        long signedUserId = authenticationFacade.getSignedUserId();

        long userId = businessRepository.findUserIdByBusinessId(p.getBusinessId());
        if (userId != signedUserId) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "해당 업체에 대한 권한이 없습니다");
        }


        Long businessId = p.getBusinessId();
        // 누락 파일 처리
        if (thumbnail == null || thumbnail.isEmpty()) {
            return 0;
        }
        String thumbPath = String.format("business/%d/thumbnail", p.getBusinessId());
        myFileUtils.makeFolders(thumbPath);

        String savedThumbName = (thumbnail != null && !thumbnail.isEmpty()) ? myFileUtils.makeRandomFileName(thumbnail) : null;
        String thumbnailFilePath = String.format("%s/%s",thumbPath,savedThumbName);

        try {
            if (thumbnail != null && !thumbnail.isEmpty()) {
                myFileUtils.transferTo(thumbnail,thumbnailFilePath);
            }
        }catch (IOException e) {
            log.error("error occurs:{}", e.getMessage());
            return 0;
        }

        // DB에 썸넬 수정 정보 업데이트

        p.setBusinessId(businessId);
        p.setThumbnail(savedThumbName);
        return businessRepository.patchBusinessThumbnail(p);
    }

    // 로고 수정
    @Transactional
    public int patchBusinessLogo(BusinessLogoPatchReq p, MultipartFile logo) {

        long signedUserId = authenticationFacade.getSignedUserId();

        long userId = businessRepository.findUserIdByBusinessId(p.getBusinessId());
        if (userId != signedUserId) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "해당 업체에 대한 권한이 없습니다");
        }


        Long businessId = p.getBusinessId();
        // 누락 파일 처리
        if (logo == null || logo.isEmpty()) {
            return 0;
        }

        // 로고파일 저장 폴더 경로
        String folderPath = String.format("business/%d/logo", businessId);

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

        p.setBusinessId(businessId);
        p.setLogo(savedPicName);
        return businessRepository.updateBusinessLogo(p);
    }

    // 사업자등록증 수정
    @Transactional
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
        return businessRepository.updateBusinessPaper(p);
    }


    @Transactional
    public BusinessContentsPostRes postBusinessContents(BusinessContentsPostReq p) {

        long signedUserId = authenticationFacade.getSignedUserId();

        Business business = businessRepository.findById(p.getBusinessId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "너 누구냐"));

        if (business.getUser().getUserId() != signedUserId) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "해당 업체에 대한 권한이 없습니다, 근데 너 누구냐");
        }

        business.setTitle(p.getTitle());
        business.setContents(p.getContents());

        businessRepository.updateBusinessContents(p);

        return new BusinessContentsPostRes(business.getTitle(), business.getContents());
    }


    @Transactional
    public BusinessPicPostRes businessPicTemp(List<MultipartFile> pics, long businessId) {
        long signedUserId = authenticationFacade.getSignedUserId();

        Business business = businessRepository.findById(businessId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "너 누구냐"));

        if (business.getUser().getUserId() != signedUserId) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "해당 업체에 대한 권한이 없습니다, 근데 너 누구냐");
        } //일단 보안먼저 챙겨주고
        boolean hasThumbnail = businessPicRepository.existsByBusinessAndState(business, 2);
        String middlePath = String.format("business/%d/pics", businessId);
        String tempPath = String.format("business/%d/temp", businessId);

        // temp 폴더가 없으면 생성
        myFileUtils.makeFolders(tempPath);



        List<String> tempPicUrls = new ArrayList<>(pics.size());
        List<BusinessPic> businessPicList = new ArrayList<>(pics.size());
        int index = 0;
        for (MultipartFile pic : pics) {
            String savedPicName = myFileUtils.makeRandomFileName(pic);
            String filePath = String.format("%s/%s", middlePath, savedPicName);
            String tempPicUrl = String.format("%s/pic/%s", docker, filePath);

            try {
                myFileUtils.transferTo(pic, filePath);
            } catch (IOException e) {
                e.printStackTrace();
            }

            BusinessPic businessPic = new BusinessPic();
            businessPic.setBusiness(business);
            businessPic.setPic(savedPicName);
            businessPic.setState(index== 0 && !hasThumbnail ? 2:0);
            businessPicList.add(businessPic);
            tempPicUrls.add(tempPicUrl);
            index++;
        }
        businessPicRepository.saveAll(businessPicList);
        return BusinessPicPostRes.builder().businessId(businessId).pics(tempPicUrls).build();
    }


    @Transactional
    public boolean businessPicConfirm(long businessId) {
        long signedUserId = authenticationFacade.getSignedUserId();

        Business business = businessRepository.findById(businessId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "너 누구냐"));

        if (business.getUser().getUserId() != signedUserId) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "해당 업체에 대한 권한이 없습니다, 근데 너 누구냐");
        }

        String tempPath = String.format("business/%d/temp", businessId);
        String middlePath = String.format("business/%d/pics", businessId);
        myFileUtils.makeFolders(middlePath);

        boolean moveSuccess = myFileUtils.moveFolder(tempPath, middlePath);
        if (!moveSuccess) {
            return false;
        }
        return moveSuccess;
    }

    //@Transactional
    public boolean businessHasNoContents(long businessId) {
        String isReal = businessRepository.findContentsById(businessId);
        return isReal == null;
    }

    @Transactional
    public int udtBusiness(BusinessDetailPutReq p) {

        long userId = businessRepository.findUserIdByBusinessId(p.getBusinessId());

        long signedUserId = authenticationFacade.getSignedUserId();
        p.setSignedUserId(signedUserId);
        if (userId != signedUserId) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "해당 업체에 대한 권한이 없습니다");
        }
        return businessMapper.udtBusiness(p);
    }


    @Transactional
    public int udtBusinessState(BusinessStatePutReq p) {

        int res = businessRepository.updateBusinessState(p);
        Business business = new Business();
        business.setState(p.getState());

        return res;
    }

    @Transactional
    public int setBusinessThumbnail(BusinessPicReq p) {
        return businessMapper.udtBusinessThumbnail(p);
    }


    @Transactional
    public Integer delBusinessPic(BusinessPicReq p) {
        //String uploadPath = myFileUtils.getUploadPath();
        String businessPicName = businessMapper.getBusinessPicName(p.getBusinessPicId());
        String filePath = String.format("pic/business/%d/pics/%s", p.getBusinessId(), businessPicName);

        log.info("Generated file path: {}", filePath);  // 경로 출력

        // 파일 삭제 시도
        if (myFileUtils.deleteFile(filePath)) {  // 단일 파일 삭제 시도
            log.info("File successfully deleted: {}", filePath);  // 삭제 성공 로그
        } else {
            log.error("Failed to delete file: {}", filePath);  // 삭제 실패 로그
        }

        // DB에서 해당 사진 정보 삭제
        return businessPicRepository.deleteByBusinessPicId(p.getBusinessPicId());
    }

    //업체 조회하기
    // 1. 업체 리스트 조회
    @Transactional
    public List<BusinessGetRes> getBusinessList(BusinessGetReq p) {


//        long signedUserId = authenticationFacade.getSignedUserId();
//        if(signedUserId != 0L) {
//            p.setSignedUserId(signedUserId);
//        }else {
//            p.setSignedUserId(null);
//        }
        // 업체 리스트 조회
        List<BusinessGetRes> res = businessMapper.selAllBusiness(p);

        // 각 비즈니스 객체마다 사진 경로 생성
        for (BusinessGetRes business : res) {
            // 비즈니스 객체의 pic 필드를 이용하여 사진 경로 생성
            String picUrl = PicUrlMaker.makePicUrlBusiness(business.getBusinessId(), business.getPic());
            business.setPic(picUrl);  // 사진 경로 업데이트
            String logoUrl = PicUrlMaker.makePicUrlLogo(business.getBusinessId(), business.getLogo());
            business.setLogo(logoUrl);  // 사진 경로 업데이트
        }

        return res;
    }

    //카카오맵 적용해서 업체조회하기
    @Transactional
    public BusinessGetResMap getBusinessListMap(BusinessGetReq p) {
        BusinessGetResMap res = new BusinessGetResMap();
        List<BusinessGetRes> businessList = businessMapper.selAllBusiness(p);

        for (BusinessGetRes business : businessList) {
            //로고 url을 생성하고 business객체의 logo필드에 설정
            String logo = PicUrlMaker.makePicUrlLogo(business.getBusinessId(), business.getLogo());
            business.setLogo(logo); // 만들어진 로고 경로를 업체 객체에 설정
        }

        //결과값 설정
        res.setBusinessList(businessList);
        res.setUserLat(p.getUserLat());
        res.setUserLng(p.getUserLng());

        return res;
    }

    // 2. 단일업체 조회
    @Transactional
    public BusinessGetOneRes getBusinessOne(BusinessGetOneReq p) {
        BusinessGetOneRes res = businessMapper.selOneBusiness(p.getBusinessId());
        if (res == null) {
            res = new BusinessGetOneRes();  // res가 null일 경우 새로운 객체 생성
        }

        if (p.getBusinessId() > 0) {
            res.setBusinessId(p.getBusinessId());
        }

        String logo = PicUrlMaker.makePicUrlLogo(p.getBusinessId(), res.getLogo());


        if (res != null && res.getLogo() != null) {
            res.setLogo(logo);
        }
        return res;
    }

    //업체 하나에 있는 사진들
    @Transactional
    public List<BusinessOnePicsGetRes> getBusinessOnePics(BusinessOnePicsGetReq p) {

        List<BusinessOnePicsGetRes> res = businessMapper.getBusinessPicList(p);
        for (BusinessOnePicsGetRes pic : res) {
            // 비즈니스 객체의 pic 필드를 이용하여 사진 경로 생성
            String picUrl = PicUrlMaker.makePicUrlBusiness(pic.getBusinessId(), pic.getPic());
            pic.setPic(picUrl);  // 사진 경로 업데이트
        }
        return res;
    }


    @Transactional
    public List<BusinessGetRevenueRes> getBusinessMonthly(BusinessGetInfoReq p) {

        long userId = businessRepository.findUserIdByBusinessId(p.getBusinessId());

        long signedUserId = authenticationFacade.getSignedUserId();
        p.setSignedUserId(signedUserId);
        if (userId != signedUserId) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "해당 업체에 대한 권한이 없습니다");
        }
        return businessMapper.getBusinessMonthly(p);
    }

    @Transactional
    public List<BusinessGetRevenueResByAdmin> getBusinessRevenueByAdmins() {

        return businessMapper.getBusinessRevenueList();
    }

    @Transactional
    public List<BusinessGetServiceRes> getBusinessService(BusinessGetInfoReq p) {
        long userId = businessRepository.findUserIdByBusinessId(p.getBusinessId());

        long signedUserId = authenticationFacade.getSignedUserId();
        p.setSignedUserId(signedUserId);
        if (userId != signedUserId) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "해당 업체에 대한 권한이 없습니다");
        }
        return businessMapper.countBusinessServices(p); //이거 마저 해야해요
    }

    @Transactional
    public List<BusinessGetServiceResByAdmin> getBusinessServiceByAdmin() {
        return businessMapper.countBusinessServicesList(); //이거 마저 해야해요
    }

}





















