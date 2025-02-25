package com.green.jobdone.admin;

import com.green.jobdone.admin.model.BusinessApplicationGetRes;
import com.green.jobdone.common.model.ResultResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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


}
