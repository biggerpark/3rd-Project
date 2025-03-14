package com.green.jobdone.portfolio;

import com.green.jobdone.business.model.BusinessLogoPatchReq;
import com.green.jobdone.common.model.ResultResponse;
import com.green.jobdone.portfolio.model.*;
import com.green.jobdone.portfolio.model.get.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("portfolio")
@RestController
@Slf4j
@Tag(name = "포트폴리오")
public class PortfolioController {
    private final PortfolioService portfolioService;

    @PostMapping
    public ResultResponse<PortfolioPostRes> insPortfolio(@RequestPart List<MultipartFile> pics
                                                         ,@RequestPart(required = false) MultipartFile thumb
            ,@Valid @RequestPart PortfolioPostReq p) {
        PortfolioPostRes res = portfolioService.insPortfolio(pics,thumb,p);
        return ResultResponse.<PortfolioPostRes>builder()
                .resultData(res)
                .resultMessage("옛다").build();
    }



    @PatchMapping("thumbnail")
    @Operation(summary = "포폴 썸네일 변경")
    public ResultResponse<Integer> patchPortfolioThumbPic(@Valid @RequestPart PortfolioPatchThumbnailReq p, @RequestPart MultipartFile thumbnail) {
        log.info("UserController > patchPortfolioThumbnailPic > p: {}", p);

        int result = portfolioService.patchPortfolioThumbnail(p,thumbnail);

        return ResultResponse.<Integer>builder()

                .resultMessage("썸네일 사진 수정 완료")
                .resultData(result)
                .build();
    }

    @PutMapping("pic/thumbnail")
    @Operation(summary = "포폴 썸네일 설정")
    public ResultResponse<Integer> setPortfolioThumbnail(@Valid @ParameterObject @ModelAttribute PortfolioPicReq p){
        int result = portfolioService.udtPortfolioThumbnail(p);
        return ResultResponse.<Integer>builder().resultData(result).resultMessage("댐").build();
    }

    @DeleteMapping("portfolioPic")
    @Operation(summary = "포폴 사진 삭제")
    public ResultResponse<Integer> delPortfolioPic(@Valid @ParameterObject @ModelAttribute PortfolioPicDelReq p) {
        int res = portfolioService.delPortfolioPic(p);
        return ResultResponse.<Integer>builder().resultData(res).resultMessage("포폴 사진 삭제 완료").build();
    }

    @DeleteMapping("{portfolioId}")
    @Operation(summary = "포폴 삭제")
    public ResultResponse<Integer> delPortfolio(@Valid @ParameterObject @ModelAttribute PortfolioDelReq p) {
        portfolioService.delPortfolio(p);
        return ResultResponse.<Integer>builder().resultMessage("포폴 삭제 완").build();
    }

    @PutMapping("state")
    @Operation(summary = "포폴 사진 상태값 업데이트")
    public void putPortfolioPicState(@RequestBody PortfolioPicStatePutReq p) {
        portfolioService.updPortfolioPicState(p);
    }

    @PutMapping
    @Operation(summary = "포폴 수정")
    public ResultResponse<PortfolioPutRes> udtPortfolioPut(@RequestPart(required = false) List<MultipartFile> pics, @Valid @RequestPart PortfolioPutReq p) {
        PortfolioPutRes res = portfolioService.udtPortfolio(pics, p);
        log.info("res: {}",res);
        return ResultResponse.<PortfolioPutRes>builder().resultData(res).resultMessage(res != null? "포폴 수정 완료": "나가라").build();
    }

    @GetMapping
    @Operation(summary = "여러 포폴조회")
    public ResultResponse<List<PortfolioListGetRes>> getPortfolioList(@Valid @ParameterObject @ModelAttribute PortfolioListGetReq p) {
        List<PortfolioListGetRes> res = portfolioService.getPortfolioList(p);
        return ResultResponse.<List<PortfolioListGetRes>>builder().resultData(res).resultMessage("포폴 리스트 조회 완료").build();
    }

    @GetMapping("/{portfolioId}")
    @Operation(summary = "한 포폴 조회")
    public ResultResponse<PortfolioGetOneRes> selPortfolio(@Valid @ParameterObject @ModelAttribute PortfolioGetOneReq p) {
        PortfolioGetOneReq req = new PortfolioGetOneReq(p.getPortfolioId());
        PortfolioGetOneRes res = portfolioService.getOnePortfolio(req);

        return ResultResponse.<PortfolioGetOneRes>builder().resultData(res).resultMessage("한 포폴 조회 완").build();
    }

    @GetMapping("pic/{portfolioId}")
    @Operation(summary = "한 포폴에서 사진 여러장 조회")
    public ResultResponse<List<PortfolioPicGetRes>> getPortfolioPicList(@Valid @ParameterObject @ModelAttribute PortfolioGetOneReq p){
        PortfolioPicGetReq req = new PortfolioPicGetReq(p.getPortfolioId());
        List<PortfolioPicGetRes> res = portfolioService.getPortfolioPicList(req);

        return ResultResponse.<List<PortfolioPicGetRes>>builder().resultData(res).resultMessage(res != null? "포폴 사진 조회 완" : "포폴 사진 조회 싯빠이").build();
    }

//    @PatchMapping("thumbnail")
//    @Operation(summary = "썸네일수정")
//    public ResultResponse<Integer> patchPortfolioThumbnail(@Valid @RequestPart PortfolioPatchThumbnailReq p, @RequestPart MultipartFile thumbnail ) {
//        int result = portfolioService.patchPortfolioThumbnail(p, thumbnail);
//
//        return ResultResponse.<Integer>builder()
//
//                .resultMessage("포폴 썸네일 사진 수정 완료")
//                .resultData(result)
//                .build();
//    }


}
