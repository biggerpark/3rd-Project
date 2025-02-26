package com.green.jobdone.review;

import com.green.jobdone.entity.Review;
import com.green.jobdone.entity.ReviewPic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ReviewPicRepository extends JpaRepository<ReviewPic, Long> {
    @Query("SELECT r.pic FROM ReviewPic r WHERE r.review.reviewId = :reviewId AND r.state = 1")
    List<String> getReviewPicByReviewIdAndState(@Param("reviewId") Long reviewId);

    @Modifying
    @Transactional
    @Query("DELETE FROM ReviewPic r WHERE r.review.reviewId = :reviewId AND r.state = 1")
    int deleteReviewPicByReviewId(@Param("reviewId") Long reviewId);

    @Modifying
    @Transactional
    @Query("UPDATE ReviewPic SET state = 1 WHERE reviewPicId = :reviewPicId")
    void updateStateByReviewPicId(@Param("reviewPicId") Long reviewPicId);

    @Modifying
    @Transactional
    @Query("UPDATE ReviewPic  SET state = 0 WHERE review.reviewId = :reviewId")
    void updateStateByReviewId(@Param("reviewId") Long reviewId);


}
