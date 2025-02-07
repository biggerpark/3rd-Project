package com.green.jobdone.portfolio;

import com.green.jobdone.business.BusinessMapper;
import com.green.jobdone.common.MyFileUtils;
import com.green.jobdone.common.PicUrlMaker;
import com.green.jobdone.config.security.AuthenticationFacade;
import com.green.jobdone.portfolio.model.PortfolioPicDto;
import com.green.jobdone.portfolio.model.PortfolioPicPostRes;
import com.green.jobdone.portfolio.model.PortfolioPostReq;
import com.green.jobdone.portfolio.model.PortfolioPutReq;
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

@Service
@Slf4j
@RequiredArgsConstructor
public class PortfolioService {
    private final PortfolioMapper portfolioMapper;
    private final BusinessMapper businessMapper;
    private final MyFileUtils myFileUtils;
    private final AuthenticationFacade authenticationFacade; //인증받은 유저가 이용 할 수 있게.


    // 포폴 만들기
    public int insPortfolio(PortfolioPostReq p){

        long signedUserId =authenticationFacade.getSignedUserId();

        long userId = businessMapper.existBusinessId(p.getBusinessId());
        if (userId != signedUserId){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "해당 업체에 대한 권한이 없습니다");
        }

        return portfolioMapper.insPortfolio(p);

    }

    //포폴 사진 등록하기
    @Transactional
    public PortfolioPicPostRes insPortfolioPic(List<MultipartFile> pics,long businessId, long portfolioId) {

        long signedUserId =authenticationFacade.getSignedUserId();

        long userId = businessMapper.existBusinessId(businessId);
        if (userId != signedUserId){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "해당 업체에 대한 권한이 없습니다");
        }


        String middlePath = String.format("business/%d/portfolio/%d/pics", businessId, portfolioId);
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

    //포폴 수정하기
    public int udtPortfolio(PortfolioPutReq p){

        long signedUserId =authenticationFacade.getSignedUserId();

        long userId = businessMapper.existBusinessId(p.getBusinessId());
        if (userId != signedUserId){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "해당 업체에 대한 권한이 없습니다");
        }
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
