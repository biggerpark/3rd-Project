package com.green.jobdone.review.comment;

import com.green.jobdone.common.exception.CustomException;
import com.green.jobdone.common.exception.ReviewErrorCode;
import com.green.jobdone.config.security.AuthenticationFacade;
import com.green.jobdone.entity.Comment;
import com.green.jobdone.entity.Review;
import com.green.jobdone.entity.User;
import com.green.jobdone.review.CommentRepository;
import com.green.jobdone.review.ReviewRepository;
import com.green.jobdone.review.comment.model.ReviewCommentGetRes;
import com.green.jobdone.review.comment.model.ReviewCommentPostReq;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ReviewCommentServiceForPostTest {

    @Mock
    private ReviewCommentMapper reviewCommentMapper;

    @Mock
    private AuthenticationFacade authenticationFacade;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private ReviewRepository reviewRepository;

    @InjectMocks
    private ReviewCommentService reviewCommentService;

    @Test
    void postReviewComment_success() {
        // Arrange
        long reviewId = 1L;
        String content = "This is a review comment";
        long signedUserId = 1L;

        ReviewCommentPostReq request = new ReviewCommentPostReq();
        request.setReviewId(reviewId);
        request.setContents(content);

        when(authenticationFacade.getSignedUserId()).thenReturn(signedUserId);
        when(reviewRepository.findBusinessUserIdByReviewId(reviewId)).thenReturn(signedUserId);
        when(reviewRepository.selReviewCommentByReviewId(reviewId)).thenReturn(null); // No existing comment

        Review savedReview = new Review();
        User savedUser = new User();
        savedUser.setUserId(signedUserId);
        Comment comment = new Comment();
        comment.setReview(savedReview);
        comment.setContents(content);
        comment.setUser(savedUser);
        comment.setCommentId(1L); // Set a valid commentId here

        when(commentRepository.save(any(Comment.class))).thenReturn(comment);

        // Act
        long commentId = reviewCommentService.postReviewComment(request);

        // Assert
        assertEquals(comment.getCommentId(), commentId);
    }

    @Test
    void postReviewComment_fail_due_to_mismatched_user() {
        // Arrange
        long reviewId = 1L;
        String content = "This is a review comment";
        long signedUserId = 2L; // Signed user is different
        long businessUserId = 1L; // Business user ID associated with review

        ReviewCommentPostReq request = new ReviewCommentPostReq();
        request.setReviewId(reviewId);
        request.setContents(content);

        when(authenticationFacade.getSignedUserId()).thenReturn(signedUserId);
        when(reviewRepository.findBusinessUserIdByReviewId(reviewId)).thenReturn(businessUserId);

        // Act & Assert
        CustomException exception = assertThrows(CustomException.class, () -> {
            reviewCommentService.postReviewComment(request);
        });

        assertEquals(ReviewErrorCode.FAIL_TO_REG_COMMENT, exception.getErrorCode());
    }

    @Test
    void postReviewComment_fail_due_to_existing_comment() {
        // Arrange
        long reviewId = 1L;
        String content = "This is a review comment";
        long signedUserId = 1L;

        ReviewCommentPostReq request = new ReviewCommentPostReq();
        request.setReviewId(reviewId);
        request.setContents(content);

        when(authenticationFacade.getSignedUserId()).thenReturn(signedUserId);
        when(reviewRepository.findBusinessUserIdByReviewId(reviewId)).thenReturn(signedUserId);
        when(reviewRepository.selReviewCommentByReviewId(reviewId)).thenReturn(new ReviewCommentGetRes()); // Existing comment found

        // Act & Assert
        CustomException exception = assertThrows(CustomException.class, () -> {
            reviewCommentService.postReviewComment(request);
        });

        assertEquals(ReviewErrorCode.FAIL_TO_REG_COMMENT_EXIST, exception.getErrorCode());
    }
}