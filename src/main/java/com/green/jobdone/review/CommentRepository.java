package com.green.jobdone.review;

import com.green.jobdone.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query("SELECT c.commentId FROM Comment c WHERE c.user.userId = :userId AND c.review.reviewId = :reviewId")
    Long getCommentIdById(Long userId, Long reviewId);

}
