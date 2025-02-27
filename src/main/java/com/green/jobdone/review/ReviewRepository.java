package com.green.jobdone.review;

import com.green.jobdone.entity.Review;
import com.green.jobdone.review.comment.model.ReviewCommentGetRes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    @Query("SELECT d.user.userId FROM Review a JOIN a.service b JOIN b.product c JOIN c.business d WHERE a.reviewId = :reviewId")
    Long findBusinessUserIdByReviewId(Long reviewId);

    @Query("SELECT b.user.userId FROM Review a JOIN a.service b WHERE a.reviewId = :reviewId")
    Long findUserIdByReviewId(Long reviewId);

    @Query("SELECT a.user.userId FROM Service a WHERE a.serviceId = :serviceId")
    Long findUserIdByServiceId(Long serviceId);

    @Query("SELECT c.commentId, c.contents, c.createdAt, c.updatedAt, " +
            "u.userId, u.name, b.logo, b.businessId " +
            "FROM Comment c " +
            "JOIN c.review r " +
            "JOIN r.service s " +
            "JOIN s.product p " +
            "JOIN p.business b " +
            "JOIN b.user u " +
            "WHERE r.reviewId = :reviewId")
    ReviewCommentGetRes selReviewCommentByReviewId(Long reviewId);


}
