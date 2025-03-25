package com.green.jobdone.review;

import com.green.jobdone.common.model.ResultResponse;
import com.green.jobdone.review.model.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("review")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "리뷰")
public class ReviewController {
    private final ReviewService reviewService;

    @PostMapping("/Img")
    public void postImg(@RequestPart MultipartFile pic, @RequestPart int num) {
        reviewService.postImg(pic, num);
    }

    @PostMapping
    @Operation(summary = "리뷰 등록", description = "필수: 사진리스트 || 옵션: 별점, 내용")
    public ResultResponse<ReviewPostRes> postReview(@RequestPart List<MultipartFile> pics
            , @Valid @RequestPart ReviewPostReq p) {
        ReviewPostRes res = reviewService.postReview(pics, p);
        return ResultResponse.<ReviewPostRes>builder()
                .resultMessage("리뷰 등록 완료")
                .resultData(res)
                .build();
    }

    @GetMapping
    @Operation(summary = "리뷰 리스트 불러오기")
    public ResultResponse<List<ReviewGetRes>> getReviewList(@Valid @ParameterObject @ModelAttribute ReviewGetReq p) {
        log.info("ReviewController > getReviewList > p: {}", p);
        List<ReviewGetRes> list = reviewService.getReviewList(p);
        return ResultResponse.<List<ReviewGetRes>>builder()
                .resultMessage(String.format("%d rows", list.size()))
                .resultData(list)
                .build();
    }

    @GetMapping("/main")
    @Operation(summary = "메인에 쓸 리뷰 리스트 불러오기")
    public ResultResponse<List<ReviewGetMainRes>> getReviewListForMain() {
        List<ReviewGetMainRes> list = reviewService.getReviewMainList();
        return ResultResponse.<List<ReviewGetMainRes>>builder()
                .resultMessage(String.format("%d rows", list.size()))
                .resultData(list)
                .build();
    }

    @PutMapping
    @Operation(summary = "리뷰 수정")
    public ResultResponse<ReviewPutRes> updReview(@RequestPart(required = false) List<MultipartFile> pics, @Valid @RequestPart ReviewPutReq p) {
        ReviewPutRes res = reviewService.updReview(pics, p);
        return ResultResponse.<ReviewPutRes>builder()
                .resultMessage("리뷰 수정 완료")
                .resultData(res)
                .build();
    }

    @PutMapping("state")
    @Operation(summary = "리뷰 사진 상태값 업데이트")
    public void putReviewPicState(@RequestBody ReviewPicStatePutReq p) {
        reviewService.updReviewPicState(p);
    }

    @DeleteMapping
    @Operation(summary = "리뷰 삭제")
    public ResultResponse<Integer> delFeed(@ParameterObject @ModelAttribute ReviewDelReq p) {
        reviewService.delReview(p);
        return ResultResponse.<Integer>builder()
                .resultMessage("리뷰 삭제 완료")
                .build();
    }


}
