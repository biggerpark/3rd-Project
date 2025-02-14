package com.green.jobdone.review;

import com.green.jobdone.review.model.ReviewPicDto;
import com.green.jobdone.review.model.ReviewPicStatePutReq;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ReviewPicMapper {
    int insReviewPic(ReviewPicDto p);
    int delReviewPic(long reviewId);
    void updReviewPicState(ReviewPicStatePutReq p);
    List<Long> selReviewPicId(long reviewId);
    List<String> selReviewPicIdWithState1(long reviewId);
}
