package com.green.jobdone.review.comment;

import com.green.jobdone.common.exception.CustomException;
import com.green.jobdone.common.exception.ReviewErrorCode;
import com.green.jobdone.config.security.AuthenticationFacade;
import com.green.jobdone.entity.Comment;
import com.green.jobdone.entity.Review;
import com.green.jobdone.entity.User;
import com.green.jobdone.review.CommentRepository;
import com.green.jobdone.review.ReviewRepository;
import com.green.jobdone.review.comment.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewCommentService {
    private final ReviewCommentMapper reviewCommentMapper;
    private final AuthenticationFacade authenticationFacade;
    private final CommentRepository commentRepository;
    private final ReviewRepository reviewRepository;

    public long postReviewComment(ReviewCommentPostReq p) {
        if(reviewRepository.findBusinessUserIdByReviewId(p.getReviewId()) != authenticationFacade.getSignedUserId()) {
            throw new CustomException(ReviewErrorCode.FAIL_TO_REG_COMMENT);
        }
        ReviewCommentGetRes res = reviewRepository.selReviewCommentByReviewId(p.getReviewId());
        if(res != null) {
            throw new CustomException(ReviewErrorCode.FAIL_TO_REG_COMMENT_EXIST);
        }
//        p.setUserId(authenticationFacade.getSignedUserId());
        Review review = new Review();
        review.setReviewId(p.getReviewId());
        User user = new User();
        user.setUserId(authenticationFacade.getSignedUserId());
        Comment comment = new Comment();
        comment.setReview(review);
        comment.setContents(p.getContents());
        comment.setUser(user);
        Comment savedComment = commentRepository.save(comment);
//        reviewCommentMapper.insReviewComment(p);
        return savedComment.getCommentId();
    }

    public ReviewCommentGetRes selReviewCommentByReviewId(ReviewCommentGetReq p) {
        return reviewRepository.selReviewCommentByReviewId(p.getReviewId());
    }

    public String updReviewComment(ReviewCommentUpdReq p) {
        p.setUserId(authenticationFacade.getSignedUserId());
        Long commentId = commentRepository.getCommentIdById(p.getUserId(), p.getReviewId());
        Review review = new Review();
        review.setReviewId(p.getReviewId());
        User user = new User();
        user.setUserId(p.getUserId());
        Comment comment = commentRepository.findById(commentId).orElse(null);
        comment.setContents(p.getContents());
        comment.setReview(review);
        comment.setUser(user);
        Comment comment1 = commentRepository.save(comment);
        return comment1.getContents();
    }

    public void delReviewComment(ReviewCommentDelReq p) {
        p.setUserId(authenticationFacade.getSignedUserId());
        Long commentId = commentRepository.getCommentIdById(p.getUserId(), p.getReviewId());
        Comment comment = commentRepository.findById(commentId).orElse(null);
        commentRepository.delete(comment);
    }
}
