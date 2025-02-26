package com.green.jobdone.business;

import com.green.jobdone.business.model.*;
import com.green.jobdone.business.model.get.*;
import com.green.jobdone.business.phone.BusinessPhonePostReq;
import com.green.jobdone.business.pic.BusinessOnePicsGetReq;
import com.green.jobdone.business.pic.BusinessOnePicsGetRes;
import com.green.jobdone.business.pic.BusinessPicPostRes;
import com.green.jobdone.common.model.ResultResponse;
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
@RequestMapping("business")
@RestController
@Slf4j
@Tag(name = "업체")
public class BusinessController {
    private final BusinessService businessService;

    @PostMapping("sign-up")
    @Operation(summary = "업체 등록")
    public ResultResponse<Long> postBusiness(@RequestPart(required = false) MultipartFile paper,@RequestPart(required = false) MultipartFile logo,
                                                 @Valid @RequestPart BusinessPostSignUpReq p) {
        long result = businessService.insBusiness(paper,logo,p);

        return ResultResponse.<Long>builder()
                .resultData(p.getBusinessId())
                .resultMessage(result != 0? "업체 등록 완료" : "업체 등록 실패")
                .build();
    }

    @PutMapping("detail")
    @Operation(summary = "업체 상세정보 기입")
    public ResultResponse<Integer> udtBusinessDetail(@Valid @ParameterObject @ModelAttribute BusinessDetailPutReq p) {
        int result = businessService.udtBusiness(p);
        return ResultResponse.<Integer>builder()
                .resultData(result)
                .resultMessage(result == 0 ? "업체 정보 수정 실패" : "업체 정보 수정 성공")
                .build();
    }

    @PatchMapping("logo")
    @Operation(summary = "업체 로고사진 변경")
    public ResultResponse<Integer> patchProfilePic(@Valid @RequestPart BusinessLogoPatchReq p, @RequestPart(required = false) MultipartFile logo) {
        log.info("UserController > patchProfilePic > p: {}", p);

        int result = businessService.patchBusinessLogo(p, logo);

        return ResultResponse.<Integer>builder()
                .resultMessage("로고 사진 수정 완료")
                .resultData(result)
                .build();
    }

    @PatchMapping("paper")
    @Operation(summary = "업체 사업자등록증 변경")
    public ResultResponse<Integer> patchProfilePaper(@Valid @RequestPart BusinessPaperPatchReq p, @RequestPart(required = false) MultipartFile paper) {
        log.info("UserController > patchProfilePic > p: {}", p);

        int result = businessService.patchBusinessPaper(p, paper);

        return ResultResponse.<Integer>builder()
                .resultMessage("사업자등록증 사진 수정 완료")
                .resultData(result)
                .build();
    }



    @PostMapping("businessPic")
    @Operation(summary = "업체 사진 등록")
    public ResultResponse<BusinessPicPostRes> postBusinessPic(@RequestPart List<MultipartFile> pics,
                                                              @Valid @RequestPart BusinessGetOneReq p) {

        BusinessPicPostRes res = businessService.insBusinessPic(pics, p.getBusinessId());
        return ResultResponse.<BusinessPicPostRes>builder()
                .resultMessage("업체사진등록 완료")
                .resultData(res)
                .build();
    }

    @PostMapping("businessPicTemp")
    @Operation(summary = "업체 사진 임시 등록")
    public ResultResponse<BusinessPicPostRes> postBusinessPicTemp(@RequestPart List<MultipartFile> pics,
                                                              @Valid @RequestPart BusinessGetOneReq p) {

        BusinessPicPostRes res = businessService.businessPicTemp(pics, p.getBusinessId());
        return ResultResponse.<BusinessPicPostRes>builder()
                .resultMessage("업체사진등록 완료")
                .resultData(res)
                .build();
    }

    @PostMapping("businessPicConf")
    @Operation(summary = "파일경로 변경 및 확정")
    public ResultResponse<Boolean> postBusinessPicConf( long businessId){
        boolean moveSuccess = businessService.businessPicConfirm(businessId);

        return ResultResponse.<Boolean>builder().resultData(moveSuccess).resultMessage(moveSuccess == true ?"댐":"싯빠이").build();
// 귀찮은데 내일 테스트 해야지

    }


    @DeleteMapping("businessPic")
    @Operation(summary = "업체 사진 삭제")
    public ResultResponse<Integer> delBusinessPic(@Valid @ParameterObject @ModelAttribute BusinessPicReq p ) {
        int result = businessService.delBusinessPic(p);
        return ResultResponse.<Integer>builder().resultData(result).resultMessage("해당 업체 사진 삭제").build();
    }

    @PutMapping("pic")
    @Operation(summary = "사진 유형 수정")
    public ResultResponse<Integer> putBusinessPic(@Valid @ParameterObject @ModelAttribute BusinessGetOneReq p) {
        int res = businessService.udtBusinessPics(p.getBusinessId());

        return ResultResponse.<Integer>builder()
                .resultMessage(res == 0? "업체사진 수정 실패":"업체 사진 수정 완료")
                .resultData(res)
                .build();
    }

    @PutMapping("pic/thumbnail")
    @Operation(summary = "업체 사진 썸네일 설정")
    public ResultResponse<Integer> setBusinessThumbnail(@Valid @ParameterObject @ModelAttribute BusinessPicReq p){
        int result = businessService.setBusinessThumbnail(p);
        return ResultResponse.<Integer>builder().resultData(result).resultMessage("댐").build();
    }

    @PutMapping("state")
    @Operation(summary = "업체 유형 수정")
    public ResultResponse<Integer> putBusinessState(@Valid @ParameterObject @ModelAttribute BusinessStatePutReq p) {
        int res = businessService.udtBusinessState(p);

        return ResultResponse.<Integer>builder()
                .resultMessage("업체 유형 수정 완료")
                .resultData(res)
                .build();
    }

    @GetMapping("/{businessId}")
    @Operation(summary = "한 업체 조회")
    public ResultResponse<BusinessGetOneRes> selBusiness(@Valid @ParameterObject @ModelAttribute BusinessGetOneReq p) {
        BusinessGetOneReq req = new BusinessGetOneReq(p.getBusinessId());
        BusinessGetOneRes res = businessService.getBusinessOne(req);

        return ResultResponse.<BusinessGetOneRes>builder().resultData(res).resultMessage("한 업체 조회완료").build();

    }

    @GetMapping
    @Operation(summary = "여러 업체 조회")
    public ResultResponse<List<BusinessGetRes>> selBusinessList(@Valid @ParameterObject @ModelAttribute BusinessGetReq p) {
        List<BusinessGetRes> res = businessService.getBusinessList(p);
        return ResultResponse.<List<BusinessGetRes>>builder()
                .resultData(res).resultMessage("업체 리스트 조회 완료")
                .build();
    }

    @GetMapping("pic/{businessId}")
    @Operation(summary = "한 업체의 사진 리스트")
    public ResultResponse<List<BusinessOnePicsGetRes>> getBusinessPicList(@Valid @ParameterObject @ModelAttribute BusinessGetOneReq p) {
        BusinessOnePicsGetReq req = new BusinessOnePicsGetReq(p.getBusinessId());
        List<BusinessOnePicsGetRes> res = businessService.getBusinessOnePics(req);

        return  ResultResponse.<List<BusinessOnePicsGetRes>>builder()
                .resultData(res)
                .resultMessage(res != null?"업체 사진 리스트 조회완":"업체 사진 리스트 조회 실패")
                .build();
    }

    @GetMapping("monthly/{businessId}")
    @Operation(summary = "업체의 월매출 조회하기")
    public ResultResponse<List<BusinessGetMonthlyRes>> getBusinessMonthly(@Valid @ParameterObject @ModelAttribute BusinessGetOneReq p) {
        BusinessGetInfoReq req = new BusinessGetInfoReq(p.getBusinessId());
        List<BusinessGetMonthlyRes> res = (List<BusinessGetMonthlyRes>) businessService.getBusinessMonthly(req);
        return ResultResponse.<List<BusinessGetMonthlyRes>>builder()
                .resultData(res)
                .resultMessage(res != null? "업체의 월매출 조회완료":"꽝 다음기회에").build();
    }

    @PostMapping("contents")
    @Operation(summary = "상품 타이틀 및 내용 수정")
    public ResultResponse<BusinessContentsPostRes> postBusinessContents( @RequestBody BusinessContentsPostReq p){

//        BusinessContentsPostReq req = new BusinessContentsPostReq(p.getBusinessId());
        BusinessContentsPostRes res = businessService.postBusinessContents(p);
        return ResultResponse.<BusinessContentsPostRes>builder()
                .resultData(res)
                .resultMessage(res != null? "네":"아니요").build();
    }

    @GetMapping("serviceCount")
    @Operation(summary = "업체가 받은 주문 카운트")
    public ResultResponse<Integer> getBusinessServiceCount(@Valid @ParameterObject @ModelAttribute BusinessGetInfoReq p) {
        //루키루키 나의루키루키뤀; 마치마치 느낌적인 느낌느낌
        int res = businessService.getBusinessService(p);
        return ResultResponse.<Integer>builder().resultData(res).resultMessage("네").build();
    }






















    @PostMapping("phone")
    @Operation(summary = "업체 전화번호 기입")
    public ResultResponse<Integer> postBusinessPhone(BusinessPhonePostReq p) {
        int result = businessService.insBusinessPhone(p);
        return ResultResponse.<Integer>builder()
                .resultMessage("전화번호 추가 완료")
                .resultData(result)
                .build();
    }
}














