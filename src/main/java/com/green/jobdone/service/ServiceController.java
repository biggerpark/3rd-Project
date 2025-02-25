package com.green.jobdone.service;

import com.green.jobdone.common.model.ResultResponse;
import com.green.jobdone.service.model.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequestMapping("service")
@RequiredArgsConstructor
@RestController
@Slf4j
@Tag(name = "서비스")
public class ServiceController {
    private final ServiceService serviceService;
    @Operation(summary = "예약하기 세차같은경우 평수에 0기입")
    @PostMapping
    public ResultResponse<ServicePostRes> postService(@Valid @RequestBody ServicePostReq p ){
        log.info("p: {}", p);
        int res = serviceService.postService(p);
        ServicePostRes res1 = new ServicePostRes();
        res1.setServiceId(p.getServiceId());
        return ResultResponse.<ServicePostRes>builder()
                .resultData(res1)
                .resultMessage(res1 == null ? "예약 실패" : "예약 성공")
                .build();
    }
    @Operation(summary = "서비스 현황 조회")
    @GetMapping
    public ResultResponse<List<ServiceGetRes>> getService(@Valid @ParameterObject @ModelAttribute ServiceGetReq p){
        List<ServiceGetRes> res = serviceService.getService(p);
        return ResultResponse.<List<ServiceGetRes>>builder()
                .resultMessage("조회 완료")
                .resultData(res)
                .build();
    }
    @Operation(summary = "견적서, 상세보기")
    @GetMapping("detail")
    public ResultResponse<ServiceGetOneRes> getOneService(@Valid @ParameterObject @ModelAttribute ServiceGetOneReq p){
        log.info("p : {}",p);
        ServiceGetOneRes res = serviceService.getOneService(p);
        return ResultResponse.<ServiceGetOneRes>builder()
                .resultData(res)
                .resultMessage("조회 완료")
                .build();
    }
    @Operation(summary = "견적서 작성/수정, etcId가 있으면 기존 etc 수정 없으면 새 etc추가")
    @PutMapping
    public ResultResponse<Integer> putService(@Valid @RequestBody ServicePutReq p){
        int res = serviceService.updService(p);
        return ResultResponse.<Integer>builder()
                .resultData(res)
                .resultMessage("수정 완료")
                .build();
    }
    @Operation(summary = "서비스 상태 변경")
    @PatchMapping
    public ResultResponse<Integer> patchServiceCompleted(@RequestBody ServicePatchReq p){
        int res = serviceService.completedService(p);
        return ResultResponse.<Integer>builder()
                .resultData(res)
                .resultMessage("요청 완료")
                .build();
    }

}
