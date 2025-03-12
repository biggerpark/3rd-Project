package com.green.jobdone.admin;

import com.green.jobdone.admin.model.*;
import com.green.jobdone.common.model.ResultResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("admin")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "관리자")
public class AdminController {
    private final AdminService adminService;


    @GetMapping("businessApplication")
    @Operation(summary = "업체 신청 리스트 조회")
    public ResultResponse<List<BusinessApplicationGetRes>> getBusinessApplication(@RequestParam int page) {
        List<BusinessApplicationGetRes> result = adminService.getBusinessApplication(page);

        return ResultResponse.<List<BusinessApplicationGetRes>>builder()
                .resultMessage("업체 신청 리스트 조회 완료")
                .resultData(result)
                .build();

    }


    @PostMapping("businessReject")
    @Operation(summary = "업체 승인 취소")
    public ResultResponse<Integer> postBusinessReject(@RequestBody BusinessRejectReq businessRejectReq) {
        int result = adminService.postBusinessReject(businessRejectReq);

        return ResultResponse.<Integer>builder()
                .resultMessage("업체 승인 취소 완료")
                .resultData(result)
                .build();

    }

    @PostMapping("businessApprove")
    @Operation(summary = "업체 승인 수락")
    public ResultResponse<Integer> postBusinessApprove(@RequestParam long businessId) {
        int result = adminService.postBusinessApprove(businessId);

        return ResultResponse.<Integer>builder()
                .resultMessage("업체 승인 수락 완료")
                .resultData(result)
                .build();

    }

    @GetMapping("category")
    @Operation(summary = "해당 카테고리 업체 리스트 조회")
    public ResultResponse<List<BusinessCategoryRes>> getBusinessCategory(@RequestParam long categoryId) {

        List<BusinessCategoryRes> result = adminService.getBusinessCategory(categoryId);


        return ResultResponse.<List<BusinessCategoryRes>>builder()
                .resultMessage("해당 카테고리 업체 리스트 조회 완료")
                .resultData(result)
                .build();
    }


    @GetMapping("userInfo")
    @Operation(summary = "관리자 측 고객 정보 조회")
    public ResultResponse<List<AdminUserInfoRes>> getAdminUserInfo(@RequestParam int page) {

        List<AdminUserInfoRes> result = adminService.getAdminUserInfo(page);


        return ResultResponse.< List<AdminUserInfoRes>>builder()
                .resultMessage("관리자 측 고객 정보 조회 완료")
                .resultData(result)
                .build();
    }

    @GetMapping("statesDashBoard")
    @Operation(summary = "관리자 통계) 대쉬보드 정보 조회")
    public ResultResponse<AdminDashBoardInfoRes> getAdminDashBoardInfo() {
        AdminDashBoardInfoRes result = adminService.getAdminDashBoardInfo();
        return ResultResponse.<AdminDashBoardInfoRes>builder()
                .resultMessage("관리자 측 카테고리 조회 완료")
                .resultData(result)
                .build();
    }

    @GetMapping("statsSales")
    @Operation(summary = "관리자 통계) 근 6개월 총매출 조회")
    public ResultResponse<List<AdminSalesInfoRes>> getAdminSalesInfo() {
        List<AdminSalesInfoRes> result = adminService.getAdminSalesInfo();
        return ResultResponse.<List<AdminSalesInfoRes>>builder()
                .resultMessage("관리자 측 총매출 조회 완료")
                .resultData(result)
                .build();
    }

    @GetMapping("statsVisitor")
    @Operation(summary = "관리자 통계) 일주일 방문자 수 조회")
    public ResultResponse<List<AdminVisitorInfoRes>> getAdminVisitorInfo() {
        List<AdminVisitorInfoRes> result = adminService.getAdminVisitorInfo();
        return ResultResponse.<List<AdminVisitorInfoRes>>builder()
                .resultMessage("관리자 측 방문자 수 조회 완료")
                .resultData(result)
                .build();
    }

    @GetMapping("statsCategory")
    @Operation(summary = "관리자 통계) 카테고리 비율 조회")
    public ResultResponse<List<AdminCategoryInfoDto>> getAdminCategoryInfo() {
        List<AdminCategoryInfoDto> result = adminService.getAdminCategoryInfo3();
        return ResultResponse.<List<AdminCategoryInfoDto>>builder()
                .resultMessage("관리자 측 카테고리 조회 완료")
                .resultData(result)
                .build();
    }

    @GetMapping("statsMain")
    @Operation(summary = "관리자 통계) 주요 통계 조회")
    public ResultResponse<AdminMainStatsRes> getAdminMainStatsInfo() {
        AdminMainStatsRes result = adminService.getAdminMainStatsInfo();
        return ResultResponse.<AdminMainStatsRes>builder()
                .resultMessage("관리자 측 주요 통계 조회 완료")
                .resultData(result)
                .build();
    }

    @GetMapping("statsNewBusiness")
    @Operation(summary = "관리자 통계) 신규 등록 업체 조회")
    public ResultResponse<List<AdminNewBusinessInfoRes>> getAdminNewBusinessInfo() {
        List<AdminNewBusinessInfoRes> result = adminService.getAdminNewBusinessInfo();
        return ResultResponse.<List<AdminNewBusinessInfoRes>>builder()
                .resultMessage("관리자 측 신규 등록 업체 조회 완료")
                .resultData(result)
                .build();
    }

    @PatchMapping("adminAllow")
    @Operation(summary = "이메일을 입력하여 관리자가 관리자 권한 부여,화면은 따로 안만들거고 서버 자체적으로 특정 이메일 지정할때 쓸 api")
    public ResultResponse<Integer> patchAdminAllow(@RequestBody AdminAllowReq p){
        Integer result = adminService.patchAdminAllow(p);

        return ResultResponse.<Integer> builder()
                .resultMessage("관리자 권한 부여 완료")
                .resultData(result)
                .build();

    }




}
