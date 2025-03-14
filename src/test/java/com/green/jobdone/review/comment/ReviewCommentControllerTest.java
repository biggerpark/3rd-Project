package com.green.jobdone.review.comment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.green.jobdone.common.GlobalOauth2;
import com.green.jobdone.common.model.ResultResponse;
import com.green.jobdone.config.JpaAuditingConfiguration;
import com.green.jobdone.config.jwt.TokenProvider;
import com.green.jobdone.review.comment.model.*;
import org.apache.catalina.security.SecurityConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.client.servlet.OAuth2ClientAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(
        controllers = ReviewCommentController.class
        , excludeAutoConfiguration = { SecurityAutoConfiguration.class, OAuth2ClientAutoConfiguration.class })
class ReviewCommentControllerTest {
    @Autowired ObjectMapper objectMapper;
    @Autowired MockMvc mockMvc;
    // @MockBean 대신 Mockito로 직접 mock 생성
    @MockitoBean ReviewCommentService reviewCommentService;

    final String BASE_URL = "/api/review/comment";

    @Test
    @DisplayName("리뷰 댓글 작성 테스트")
    void postReviewComment() throws Exception {
        ReviewCommentPostReq givenParam = new ReviewCommentPostReq();
        givenParam.setReviewId(1L);
        givenParam.setContents("댓글 내용");
        long expectedId = 1L;

        given(reviewCommentService.postReviewComment(givenParam)).willReturn(expectedId);

        String paramJson = objectMapper.writeValueAsString(givenParam);

        ResultActions resultActions = mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(paramJson));

        String expectedResJson = objectMapper.writeValueAsString(
                ResultResponse.<Long>builder().resultMessage("댓글 등록 완료").resultData(expectedId).build());

        resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResJson));

        verify(reviewCommentService).postReviewComment(givenParam);
    }

    @Test
    @DisplayName("리뷰 댓글 불러오기 테스트")
    void getReviewComment() throws Exception {
        ReviewCommentGetReq givenParam = new ReviewCommentGetReq();
        givenParam.setReviewId(1L);
        ReviewCommentGetRes expectedRes = new ReviewCommentGetRes();
        given(reviewCommentService.selReviewCommentByReviewId(givenParam)).willReturn(expectedRes);

        ResultActions resultActions = mockMvc.perform(get(BASE_URL)
                .queryParam("reviewId", "1")
                .queryParam("startIdx", "0")
                .queryParam("size", "10"));

        String expectedResJson = objectMapper.writeValueAsString(
                ResultResponse.<ReviewCommentGetRes>builder().resultMessage("댓글 검색 완료").resultData(expectedRes).build());

        resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResJson));

        verify(reviewCommentService).selReviewCommentByReviewId(givenParam);
    }

    @Test
    @DisplayName("리뷰 댓글 수정 테스트")
    void updReviewComment() throws Exception {
        ReviewCommentUpdReq givenParam = new ReviewCommentUpdReq();
        givenParam.setReviewId(1L);
        givenParam.setContents("수정된 댓글 내용");
        String expectedResult = "수정 완료";
        given(reviewCommentService.updReviewComment(givenParam)).willReturn(expectedResult);

        String paramJson = objectMapper.writeValueAsString(givenParam);
        ResultActions resultActions = mockMvc.perform(put(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(paramJson));

        String expectedResJson = objectMapper.writeValueAsString(
                ResultResponse.<String>builder().resultMessage("댓글 수정 완료").resultData(expectedResult + "로 수정되었습니다.").build());

        resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResJson));

        verify(reviewCommentService).updReviewComment(givenParam);
    }

    @Test
    @DisplayName("리뷰 댓글 삭제 테스트")
    void delReviewComment() throws Exception {
        ReviewCommentDelReq givenParam = new ReviewCommentDelReq();
        givenParam.setReviewId(1L);

        ResultActions resultActions = mockMvc.perform(delete(BASE_URL)
                .queryParam("reviewCommentId", "1"));

        String expectedResJson = objectMapper.writeValueAsString(
                ResultResponse.<Integer>builder().resultMessage("리뷰 댓글 삭제 완료").build());

        resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResJson));

        verify(reviewCommentService).delReviewComment(givenParam);
    }
}