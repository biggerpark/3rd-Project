package com.green.jobdone.review;

import com.green.jobdone.common.MyFileUtils;
import com.green.jobdone.common.PicUrlMaker;
import com.green.jobdone.common.exception.CustomException;
import com.green.jobdone.common.exception.ReviewErrorCode;
import com.green.jobdone.config.security.AuthenticationFacade;
import com.green.jobdone.review.comment.ReviewCommentMapper;
import com.green.jobdone.review.comment.model.ReviewCommentDto;
import com.green.jobdone.review.comment.model.ReviewCommentGetRes;
import com.green.jobdone.review.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewMapper reviewMapper;
    private final ReviewPicMapper reviewPicMapper;
    private final ReviewCommentMapper reviewCommentMapper;
    private final AuthenticationFacade authenticationFacade;
    private final MyFileUtils myFileUtils;

    public void postImg (MultipartFile pic, int num) {
        myFileUtils.makeFolders("user/defaultImg");
        String filePath = String.format("user/defaultImg/img%d.jpg", num);
        try {
            myFileUtils.transferTo(pic, filePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public ReviewPostRes postReview (List<MultipartFile> pics, ReviewPostReq p) {
        if(reviewMapper.selUserIdByServiceId(p.getServiceId()) != authenticationFacade.getSignedUserId()) {
            throw new CustomException(ReviewErrorCode.FAIL_TO_REG);
        }
        int result = reviewMapper.insReview(p);

        long reviewId = p.getReviewId();

        String middlePath = String.format("review/%d", reviewId);
        myFileUtils.makeFolders(middlePath);

        List<String> picNameList = new ArrayList<>(pics.size());
        for(MultipartFile pic : pics) {
            //각 파일 랜덤파일명 만들기
            String savedPicName = myFileUtils.makeRandomFileName(pic);
            picNameList.add(savedPicName);
            String filePath = String.format("%s/%s", middlePath,savedPicName);
            try {
                myFileUtils.transferTo(pic, filePath);
            } catch (IOException e) {
                //폴더삭제 처리
                String delFolderPath = String.format("%s/%s", myFileUtils.getUploadPath(), middlePath);
                myFileUtils.deleteFolder(delFolderPath, true);
                throw new RuntimeException(e);
            }
        }
        ReviewPicDto reviewPicDto = new ReviewPicDto();
        reviewPicDto.setReviewId(reviewId);
        reviewPicDto.setPics(picNameList);
        int resultPics = reviewPicMapper.insReviewPic(reviewPicDto);

        return ReviewPostRes.builder()
                .reviewId(reviewId)
                .pics(picNameList)
                .build();
    }

    public List<ReviewGetRes> getFeedList(ReviewGetReq p) {
        List<ReviewGetRes> list = new ArrayList<>(p.getSize());

        //SELECT (1): review + review_pic
        if(p.getBusinessId() == null) {
            p.setUserId(authenticationFacade.getSignedUserId());
        }
        log.info("state: {}", p.getState());
        List<ReviewAndPicDto> reviewAndPicDtoList = reviewMapper.selReviewWithPicList(p);

        ReviewGetRes beforeReviewGetRes = new ReviewGetRes();
        for(ReviewAndPicDto reviewAndPicDto : reviewAndPicDtoList) {
            if(beforeReviewGetRes.getReviewId() != reviewAndPicDto.getReviewId()) {
                beforeReviewGetRes = new ReviewGetRes();
                beforeReviewGetRes.setPics(new ArrayList<>(3));
                list.add(beforeReviewGetRes);
                beforeReviewGetRes.setReviewId(reviewAndPicDto.getReviewId());
                beforeReviewGetRes.setContents(reviewAndPicDto.getContents());
                beforeReviewGetRes.setScore(reviewAndPicDto.getScore());
                String createdAt = reviewAndPicDto.getCreatedAt().substring(0,10);
                beforeReviewGetRes.setCreatedAt(createdAt);
                beforeReviewGetRes.setServiceId(reviewAndPicDto.getServiceId());
                beforeReviewGetRes.setName(reviewAndPicDto.getName());
                String profile = reviewAndPicDto.getWriterPic().substring(0,3);
                String profile2 = "img";
                if(profile.equals(profile2)){
                    beforeReviewGetRes.setWriterPic(String.format("/pic/user/defaultImg/%s", reviewAndPicDto.getPic()));
                } else {
                    beforeReviewGetRes.setWriterPic(PicUrlMaker.makePicUserUrl(reviewAndPicDto.getUserId(),reviewAndPicDto.getWriterPic()));
                }
                beforeReviewGetRes.setDetailTypeName(reviewAndPicDto.getDetailTypeName());
                beforeReviewGetRes.setAverageScore(Math.round(reviewAndPicDto.getAverageScore()*100)/100.0);
            }
            String picUrl = PicUrlMaker.makePicUrlReview(reviewAndPicDto.getReviewId(),reviewAndPicDto.getPic());
            beforeReviewGetRes.getPics().add(picUrl);
            beforeReviewGetRes.getPics().add(reviewAndPicDto.getReviewPicId());
        }
        //SELECT (2): review_comment
        List<Long> reviewIds = new ArrayList<>(list.size());
        for(ReviewGetRes item : list) {
            ReviewCommentGetRes comment = reviewCommentMapper.selReviewCommentByReviewId(item.getReviewId());
            if(comment != null) {
                comment.setLogo(PicUrlMaker.makePicUrlLogo(comment.getBusinessId(),comment.getLogo()));
                item.setComment(comment);
            }
        }

        return list;
    }

    public ReviewPutRes updReview(List<MultipartFile> pics, ReviewPutReq p) {
        if(reviewMapper.selUserIdByReviewId(p.getReviewId()) != authenticationFacade.getSignedUserId()) {
            throw new CustomException(ReviewErrorCode.FAIL_TO_UPD);
        }
        int result = reviewMapper.updReview(p);

        long reviewId = p.getReviewId();

        String middlePath = String.format("review/%d", reviewId);
        myFileUtils.makeFolders(middlePath);
        List<String> picNameList = new ArrayList<>(pics.size());
        for(MultipartFile pic : pics) {
            //각 파일 랜덤파일명 만들기
            String savedPicName = myFileUtils.makeRandomFileName(pic);
            picNameList.add(savedPicName);
            String filePath = String.format("%s/%s", middlePath,savedPicName);
            try {
                myFileUtils.transferTo(pic, filePath);
            } catch (IOException e) {
                //폴더삭제 처리
                String delFolderPath = String.format("%s/%s", myFileUtils.getUploadPath(), middlePath);
                myFileUtils.deleteFolder(delFolderPath, true);
                throw new RuntimeException(e);
            }
        }
        ReviewPicDto reviewPicDto = new ReviewPicDto();
        reviewPicDto.setReviewId(reviewId);
        reviewPicDto.setPics(picNameList);
        int resultPics = reviewPicMapper.insReviewPic(reviewPicDto);

        int delPic = reviewPicMapper.delReviewPic(p.getReviewId());

        return ReviewPutRes.builder()
                .reviewId(reviewId)
                .pics(picNameList)
                .build();
    }

    public void updReviewPicState(ReviewPicStatePutReq p) {
        reviewPicMapper.updReviewPicState(p);
    }

    public int delReview(ReviewDelReq p) {
        if(reviewMapper.selUserIdByReviewId(p.getReviewId()) != authenticationFacade.getSignedUserId()) {
            throw new CustomException(ReviewErrorCode.FAIL_TO_DEL);
        }
        int affectedRows = reviewMapper.delReview(p);

        //리뷰 사진 삭제
        String deletePath = String.format("%s/review/%d", myFileUtils.getUploadPath(), p.getReviewId());
        myFileUtils.deleteFolder(deletePath, true);

        return affectedRows;
    }


}
