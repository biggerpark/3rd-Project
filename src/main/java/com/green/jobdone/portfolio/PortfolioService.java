package com.green.jobdone.portfolio;

import com.green.jobdone.business.BusinessMapper;
import com.green.jobdone.business.BusinessRepository;
import com.green.jobdone.business.model.BusinessStatePutReq;
import com.green.jobdone.common.MyFileUtils;
import com.green.jobdone.common.PicUrlMaker;
import com.green.jobdone.config.security.AuthenticationFacade;
import com.green.jobdone.entity.Business;
import com.green.jobdone.entity.Portfolio;
import com.green.jobdone.entity.PortfolioPic;
import com.green.jobdone.portfolio.model.*;
import com.green.jobdone.portfolio.model.get.*;
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
import java.util.stream.Collectors;

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


    // 포폴 만들기
    @Transactional
    public PortfolioPostRes insPortfolio(List<MultipartFile> pics, PortfolioPostReq p){

        long signedUserId = authenticationFacade.getSignedUserId();

        // 보안 챙겨주기
        long userId = businessRepository.findUserIdByBusinessId(p.getBusinessId());
        if (userId != signedUserId) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "해당 업체에 대한 권한이 없습니다");
        }

        Portfolio portfolio = Portfolio.builder()
                .business(businessRepository.findById(p.getBusinessId()).orElse(null))
                .title(p.getTitle())
                .price(p.getPrice())
                .takingTime(p.getTakingTime())
                .contents(p.getContents())
                .build(); // 일단 빌더로 적을거 다 적고

        Portfolio savedPortfolio= portfolioRepository.save(portfolio);
        Long portfolioId =savedPortfolio.getPortfolioId();

        String middlePath = String.format("pic/business/%d/portfolio/%d", p.getBusinessId(), portfolioId);
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
        List<PortfolioPic> portfolioPics = portfolioPicList.stream()
                .map(item -> PortfolioPic.builder()
                        .portfolio(savedPortfolio)
                        .pic(item)
                        .build())
                .collect(Collectors.toList());

        portfolioPicRepository.saveAll(portfolioPics);


        return PortfolioPostRes.builder().portfolioId(portfolioId).pics(portfolioPicList).build();

    }

    //포폴 사진 등록하기
    @Transactional
    public PortfolioPicPostRes insPortfolioPic(List<MultipartFile> pics,long businessId, long portfolioId) {

//        long signedUserId =authenticationFacade.getSignedUserId();
//
//        long userId = businessMapper.existBusinessId(businessId);
//        if (userId != signedUserId){
//            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "해당 업체에 대한 권한이 없습니다");
//        }


        String middlePath = String.format("pic/business/%d/portfolio/%d/pics", businessId, portfolioId);
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
                e.printStackTrace();
            }
        }
        PortfolioPicDto portfolioPicDto = new PortfolioPicDto();
        portfolioPicDto.setPortfolioId(portfolioId);
        portfolioPicDto.setPics(portfolioPicList);
        int resultPics = portfolioMapper.insPortfolioPic(portfolioPicDto);

        return PortfolioPicPostRes.builder().portfolioPicId(portfolioId).pics(portfolioPicList).build();
    }

    public int udtPortfolioPics(PortfolioGetOneReq p) {

        return portfolioMapper.putPortfolioPic(p);
    }
    public int udtPortfolioThumbnail(PortfolioPicReq p){
        return portfolioMapper.udtPortfolioThumbnail(p);
    }


    //포폴사진삭제
    public int delPortfolioPic(PortfolioPicDelReq p) {
        String portfolioPicName = portfolioMapper.getPortfolioPicName(p.getPortfolioId());
        String filePath = String.format("business/%d/portfolio/%d/pics/%s", p.getBusinessId(), p.getPortfolioId(), portfolioPicName);

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
    public int delPortfolio(PortfolioDelReq p) {

        myFileUtils.deleteFolder(String.format("business/%d/portfolio/%d", p.getBusinessId(), p.getPortfolioId()),true);
        return portfolioMapper.delPortfolio(p);
    }

    //포폴 수정하기
    public int udtPortfolio(PortfolioPutReq p){

//        long signedUserId =authenticationFacade.getSignedUserId();
//
//        long userId = businessMapper.existBusinessId(p.getBusinessId());
//        if (userId != signedUserId){
//            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "해당 업체에 대한 권한이 없습니다");
//        }
        return portfolioMapper.udtPortfolio(p);
    }

    //포폴리스트 조회
    public List<PortfolioListGetRes> getPortfolioList(PortfolioListGetReq p){
        List<PortfolioListGetRes> res = portfolioMapper.selAllPortfolioList(p);

        for (PortfolioListGetRes r : res) {
            String picUrl = PicUrlMaker.makePicUrlPortfolio(r.getBusinessId(),r.getPortfolioId(),r.getIsThumbnail());
            r.setIsThumbnail(picUrl);
        }

        return res;
    }

    public PortfolioGetOneRes getOnePortfolio(PortfolioGetOneReq p) {
        PortfolioGetOneRes res = portfolioMapper.selOnePortfolio(p.getPortfolioId());
        if (res == null) {
            res = new PortfolioGetOneRes();
        }

        if (p.getPortfolioId()>0){
            res.setPortfolioId(p.getPortfolioId());
        }
        return res;
    }

    public List<PortfolioPicGetRes> getPortfolioPicList(PortfolioPicGetReq p) {
        List<PortfolioPicGetRes> res = portfolioMapper.getPortfolioPicList(p);
        for (PortfolioPicGetRes pic : res) {
            String picUrl = PicUrlMaker.makePicUrlPortfolio(pic.getBusinessId(),pic.getPortfolioId(),pic.getPic());
            pic.setPic(picUrl);
        }
        return res;
    }











}
