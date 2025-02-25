package com.green.jobdone.admin;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("admin")
@RequiredArgsConstructor
@Slf4j
public class AdminController {
    private final AdminService adminService;


//    @GetMapping("businessApplication")
//    @Operation(summary = "업체 신청 리스트 조회")
//

}
