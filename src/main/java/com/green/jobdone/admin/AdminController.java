package com.green.jobdone.admin;

import com.green.jobdone.admin.model.AdminUserInfoRes;
import com.green.jobdone.admin.model.BusinessApplicationGetRes;
import com.green.jobdone.admin.model.BusinessCategoryRes;
import com.green.jobdone.admin.model.BusinessRejectReq;
import com.green.jobdone.category.model.CategoryGetRes;
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









}
