package com.green.jobdone.portfolio;

import com.green.jobdone.business.BusinessMapper;
import com.green.jobdone.business.BusinessRepository;
import com.green.jobdone.common.MyFileUtils;
import com.green.jobdone.common.PicUrlMaker;
import com.green.jobdone.config.security.AuthenticationFacade;
import com.green.jobdone.entity.Business;
import com.green.jobdone.entity.Portfolio;
import com.green.jobdone.entity.PortfolioPic;
import com.green.jobdone.portfolio.model.*;
import com.green.jobdone.portfolio.model.get.*;
import com.green.jobdone.youtube.YoutubeService;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class PortfolioService {
    private final PortfolioRepository portfolioRepository;
    private final PortfolioMapper portfolioMapper;
    private final BusinessMapper businessMapper;
    private final BusinessRepository businessRepository;
    private final MyFileUtils myFileUtils;
    private final AuthenticationFacade authenticationFacade; //인증받은 유저가 이용 할 수 있게.
    private final PortfolioPicRepository portfolioPicRepository;
    private final YoutubeService youtubeService;
    private final EntityManager entityManager;

    // 포폴 만들기
    @Transactional
    public PortfolioPostRes insPortfolio(List<MultipartFile> pics, PortfolioPostReq p) {

        long signedUserId = authenticationFacade.getSignedUserId();

        // 보안 챙겨주기
        long userId = businessRepository.findUserIdByBusinessId(p.getBusinessId());
        if (userId != signedUserId) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "해당 업체에 대한 권한이 없습니다");
        }
        String youtubeUrl = p.getYoutubeUrl();
        String youtubeId = youtubeService.extractVideoId(youtubeUrl);


        Portfolio portfolio = Portfolio.builder()
                .business(businessRepository.findById(p.getBusinessId()).orElse(null))
                .title(p.getTitle())
                .price(p.getPrice())
                .takingTime(p.getTakingTime())
                .contents(p.getContents())
                .youtubeUrl(youtubeUrl)
                .youtubeId(youtubeId)
                .build(); // 일단 빌더로 적을거 다 적고

        Portfolio savedPortfolio = portfolioRepository.save(portfolio);
        Long portfolioId = savedPortfolio.getPortfolioId();

        String middlePath = String.format("business/%d/portfolio/%d", p.getBusinessId(), portfolioId);
        myFileUtils.makeFolders(middlePath);

        List<String> portfolioPicList = new ArrayList<>(pics.size());
        for (MultipartFile pic : pics) {
            //pics리스트에 있는 사진들 전수조사
            String savedPicName = myFileUtils.makeRandomFileName(pic);

            portfolioPicList.add(savedPicName);
            String filePath = String.format("%s/%s", middlePath, savedPicName);
            try {
                myFileUtils.transferTo(pic, filePath); // 포폴 사진값 설정해놓음
            } catch (IOException e) {
                String delFolderPath = String.format("%s/%s", myFileUtils.getUploadPath(), middlePath);
                myFileUtils.deleteFolder(delFolderPath, true);
                throw new RuntimeException(e);
            }
        }
        boolean isFirst = true; // 첫 번째 사진 여부를 체크할 변수

        List<PortfolioPic> portfolioPics = new ArrayList<>();
        for (String picName : portfolioPicList) { // 리스트를 순회하면서 사진 저장
            PortfolioPic portfolioPic = new PortfolioPic();
            portfolioPic.setPortfolio(portfolio);
            portfolioPic.setPic(picName);
            portfolioPic.setState(isFirst ? 2 : 0); // 첫 번째 사진이면 2, 나머지는 0
            portfolioPics.add(portfolioPic);

            isFirst = false; // 첫 번째 처리가 끝났으니 false로 변경
        }

        portfolioPicRepository.saveAll(portfolioPics);


        return PortfolioPostRes.builder()
                .portfolioId(portfolioId)
                .youtubeId(youtubeId)
                .pics(portfolioPicList)
                .build();

    } //완

    // 사진등록은 존재이유가 없으니 지워버림

    //포폴 수정하기
    @Transactional
    public PortfolioPutRes udtPortfolio(List<MultipartFile> pics, PortfolioPutReq p) {

        long signedUserId = authenticationFacade.getSignedUserId();

        // 보안 챙겨주기
        long userId = businessRepository.findUserIdByBusinessId(p.getBusinessId());
        if (userId != signedUserId) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "해당 업체에 대한 권한이 없습니다");
        }


        Portfolio portfolio = entityManager.find(Portfolio.class, p.getPortfolioId());
        if (portfolio == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "포폴을 찾을수 없습니다.");
        }
        // 변경된 내용만 업데이트
        if (p.getTitle() != null) portfolio.setTitle(p.getTitle());
        if (p.getPrice() != null) portfolio.setPrice(p.getPrice());
        if (p.getTakingTime() != null) portfolio.setTakingTime(p.getTakingTime());
        if (p.getContents() != null) portfolio.setContents(p.getContents());

        // YouTube URL 처리
        if (p.getYoutubeUrl() != null) {
            String youtubeId = youtubeService.extractVideoId(p.getYoutubeUrl());
            if (youtubeId != null) {
                portfolio.setYoutubeUrl(p.getYoutubeUrl());
                portfolio.setYoutubeId(youtubeId);
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "유효하지 않은 유튜브 URL입니다.");
            }
        }

        entityManager.flush();



        List<String> portfolioPicList = new ArrayList<>();

        long portfolioId = p.getPortfolioId();
        List<String> delPortfolioPicList = portfolioPicRepository.getPortfolioPicsByPortfolioId(portfolioId);
        for (String pic : delPortfolioPicList) {
            String middlePath = String.format("business/%d/portfolio/%d/%s", p.getBusinessId(), portfolioId, pic);
            myFileUtils.makeFolders(middlePath);
        }

        portfolioPicRepository.deletePortfolioPicByPortfolioPicId(portfolioId);

        if (pics == null || pics.isEmpty()) {
            return PortfolioPutRes.builder()
                    .portfolioId(portfolioId)
                    .youtubeId(portfolio.getYoutubeId())
                    .pics(portfolioPicList)
                    .build();
        }

        String middlePath = String.format("business/%d/portfolio/%d", p.getBusinessId(), portfolioId);
        myFileUtils.makeFolders(middlePath);

        for (MultipartFile pic : pics) {
            //랜덤파일명 만들기
            String savedPicName = myFileUtils.makeRandomFileName(pic);
            portfolioPicList.add(savedPicName);
            String filePath = String.format("%s/%s", middlePath, savedPicName);
            try {
                myFileUtils.transferTo(pic, filePath);
            } catch (IOException e) {
                //폴더삭제 처리
                String delFolderPath = String.format("%s/%s", myFileUtils.getUploadPath(), middlePath);
                myFileUtils.deleteFolder(delFolderPath, true);
                throw new RuntimeException(e);
            }
        }
        List<PortfolioPic> portfolioPics = new ArrayList<>(portfolioPicList.size());
        for (String item : portfolioPicList) {
            PortfolioPic portfolioPic = new PortfolioPic();
            portfolioPic.setPortfolio(portfolio);
            portfolioPic.setPic(item);
            portfolioPics.add(portfolioPic);
        }
        portfolioPicRepository.saveAll(portfolioPics);
        return PortfolioPutRes.builder()
                .portfolioId(portfolioId)
                .youtubeId(portfolio.getYoutubeId())
                .pics(portfolioPicList)
                .build();
    }

    public void updPortfolioPicState(PortfolioPicStatePutReq p) {
        if (p.getPortfolioId() == null && p.getPortfolioPicId() != null) {
            //사진pk만 들어오면 0이나 2로 되어있다가 1로 바뀜
            portfolioPicRepository.updateStateByPortfolioPicId(p.getPortfolioPicId());
        } else {// 아니면 리뷰나 pk가 들어왔을때
            portfolioPicRepository.updateStateByPortfolioId(p.getPortfolioId());
        }
    }

    public int udtPortfolioThumbnail(PortfolioPicReq p) {
        return portfolioRepository.updatePortfolioThumbnail(p.getPortfolioPicId(),p.getPortfolioId());
    }
    // 이런건 괜히 손대면 귀찮아지니까 냅두자

    //포폴사진삭제 -> 이것도 더미
    public int delPortfolioPic(PortfolioPicDelReq p) {
        String portfolioPicName = portfolioMapper.getPortfolioPicName(p.getPortfolioId());
        String filePath = String.format("business/%d/portfolio/%d/%s", p.getBusinessId(), p.getPortfolioId(), portfolioPicName);

        log.info("Generated file path: {}", filePath);  // 경로 출력

        // 파일 삭제 시도
        if (myFileUtils.deleteFile(filePath)) {  // 단일 파일 삭제 시도
            log.info("File successfully deleted: {}", filePath);  // 삭제 성공 로그
        } else {
            log.error("Failed to delete file: {}", filePath);  // 삭제 실패 로그
        }
        return portfolioMapper.delPortfolioPic(p);
    }

    // 포폴 삭제 -> 사진폴더 통으로 날려버리기
    public void delPortfolio(PortfolioDelReq p) {
        long signedUserId = authenticationFacade.getSignedUserId();
        // 보안 췍
        long userId = businessRepository.findUserIdByBusinessId(p.getBusinessId());
        if (userId != signedUserId) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "해당 업체에 대한 권한이 없습니다");
        }

        Portfolio portfolio = portfolioRepository.findById(p.getPortfolioId()).orElse(null);
        if (portfolio == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "naga");// rid eocndgka 대층강
        }
        portfolioRepository.delete(portfolio);

        String deletePath = String.format("business/%d/portfolio/%d", p.getBusinessId(), p.getPortfolioId());
        myFileUtils.deleteFolder(deletePath, true); // 아 ㅎㅎ
    }


    //포폴리스트 조회
    public List<PortfolioListGetRes> getPortfolioList(PortfolioListGetReq p) {
        List<PortfolioListGetRes> res = portfolioMapper.selAllPortfolioList(p);

        for (PortfolioListGetRes r : res) {
            String picUrl = PicUrlMaker.makePicUrlPortfolio(r.getBusinessId(), r.getPortfolioId(), r.getIsThumbnail());
            r.setIsThumbnail(picUrl);
        }

        return res;
    }

    //단일포폴조회 이건 사진이랑 같이 조회하게 하자 는 따로따로하자
    public PortfolioGetOneRes getOnePortfolio(PortfolioGetOneReq p) {
        PortfolioGetOneRes res = portfolioMapper.selOnePortfolio(p.getPortfolioId());
        if (res == null) {
            res = new PortfolioGetOneRes();
        }

        if (p.getPortfolioId() > 0) {
            res.setPortfolioId(p.getPortfolioId());
        }
        return res;
    }

    public List<PortfolioPicGetRes> getPortfolioPicList(PortfolioPicGetReq p) {
        List<PortfolioPicGetRes> res = portfolioMapper.getPortfolioPicList(p);
        for (PortfolioPicGetRes pic : res) {
            String picUrl = PicUrlMaker.makePicUrlPortfolio(pic.getBusinessId(), pic.getPortfolioId(), pic.getPic());
            pic.setPic(picUrl);
        }
        return res;
    }


}
