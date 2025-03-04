package com.green.jobdone.review;

import com.green.jobdone.common.MyFileUtils;
import com.green.jobdone.common.PicUrlMaker;
import com.green.jobdone.common.exception.CustomException;
import com.green.jobdone.common.exception.ReviewErrorCode;
import com.green.jobdone.config.security.AuthenticationFacade;
import com.green.jobdone.entity.*;
import com.green.jobdone.review.comment.ReviewCommentMapper;
import com.green.jobdone.review.comment.model.ReviewCommentGetRes;
import com.green.jobdone.review.model.*;
import com.green.jobdone.service.ServiceRepository;
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
public class ReviewService {
    private final ReviewMapper reviewMapper;
    private final ReviewPicMapper reviewPicMapper;
    private final ReviewCommentMapper reviewCommentMapper;
    private final AuthenticationFacade authenticationFacade;
    private final ReviewRepository reviewRepository;
    private final ReviewPicRepository reviewPicRepository;
    private final ServiceRepository serviceRepository;
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
        if(reviewRepository.findUserIdByServiceId(p.getServiceId()) != authenticationFacade.getSignedUserId()) {
            throw new CustomException(ReviewErrorCode.FAIL_TO_REG);
        }
        com.green.jobdone.entity.Service service = new com.green.jobdone.entity.Service();
        service.setServiceId(p.getServiceId());
        Review review = new Review();
        review.setContents(p.getContents());
        review.setScore(p.getScore());
        review.setService(service);
        Review savedReview = reviewRepository.save(review);
       // int result = reviewMapper.insReview(p);

        Long reviewId = savedReview.getReviewId();

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
        List<ReviewPic> reviewPicList = new ArrayList<>(picNameList.size());
        for(String item : picNameList) {
            ReviewPic reviewPic = new ReviewPic();
            reviewPic.setReview(review);
            reviewPic.setPic(item);
            reviewPicList.add(reviewPic);
        }
        reviewPicRepository.saveAll(reviewPicList);
//        ReviewPicDto reviewPicDto = new ReviewPicDto();
//        reviewPicDto.setReviewId(reviewId);
//        reviewPicDto.setPics(picNameList);
//        int resultPics = reviewPicMapper.insReviewPic(reviewPicDto);

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
        if(reviewRepository.findUserIdByReviewId(p.getReviewId()) != authenticationFacade.getSignedUserId()) {
            throw new CustomException(ReviewErrorCode.FAIL_TO_UPD);
        }
        Review review = reviewRepository.findById(p.getReviewId()).orElse(null);
        com.green.jobdone.entity.Service service = new com.green.jobdone.entity.Service();
        service.setServiceId(p.getServiceId());
        review.setReviewId(p.getReviewId());
        review.setService(service);
        review.setContents(p.getContents());
        review.setScore(p.getScore());
        reviewRepository.save(review);
//        int result = reviewMapper.updReview(p);
        List<String> picNameList = new ArrayList<>();
//        String check = pics.get(0).getOriginalFilename();

        long reviewId = p.getReviewId();
        List<String> delPicNameList = reviewPicRepository.getReviewPicByReviewIdAndState(reviewId);
        for(String pic : delPicNameList) {
            String middlePath = String.format("review/%d/%s", reviewId, pic);
            myFileUtils.deleteFile(middlePath);
        }

        int delPics = reviewPicRepository.deleteReviewPicByReviewId(reviewId);

        if(pics == null || pics.isEmpty()){
            return ReviewPutRes.builder()
                    .reviewId(p.getReviewId())
                    .pics(picNameList)
                    .build();
        }


        String middlePath = String.format("review/%d", reviewId);
//        myFileUtils.deleteFolder(middlePath, true);
        myFileUtils.makeFolders(middlePath);
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
        List<ReviewPic> reviewPicList = new ArrayList<>(picNameList.size());
        for(String item : picNameList) {
            ReviewPic reviewPic = new ReviewPic();
            reviewPic.setReview(review);
            reviewPic.setPic(item);
            reviewPicList.add(reviewPic);
        }
        reviewPicRepository.saveAll(reviewPicList);

//        ReviewPicDto reviewPicDto = new ReviewPicDto();
//        reviewPicDto.setReviewId(reviewId);
//        reviewPicDto.setPics(picNameList);
//        int resultPics = reviewPicMapper.insReviewPic(reviewPicDto);

        return ReviewPutRes.builder()
                .reviewId(reviewId)
                .pics(picNameList)
                .build();
    }

    public void updReviewPicState(ReviewPicStatePutReq p) {
        if(p.getReviewId() == null && p.getReviewPicId() != null) {
            reviewPicRepository.updateStateByReviewPicId(p.getReviewPicId());
        } else {
            reviewPicRepository.updateStateByReviewId(p.getReviewId());
        }
    }

    public void delReview(ReviewDelReq p) {
        if(reviewRepository.findUserIdByReviewId(p.getReviewId()) != authenticationFacade.getSignedUserId()) {
            throw new CustomException(ReviewErrorCode.FAIL_TO_DEL);
        }
        Review review = reviewRepository.findById(p.getReviewId()).orElse(null);
        if(review == null) {
            throw new CustomException(ReviewErrorCode.FAIL_TO_DEL);
        }
        reviewRepository.delete(review);
        //리뷰 사진 삭제
        String deletePath = String.format("review/%d", p.getReviewId());
        myFileUtils.deleteFolder(deletePath, true);
    }


}
