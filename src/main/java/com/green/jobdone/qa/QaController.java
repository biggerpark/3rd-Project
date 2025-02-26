package com.green.jobdone.qa;


import com.green.jobdone.common.model.ResultResponse;
import com.green.jobdone.qa.model.QaReq;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("qa")
@Slf4j
@Tag(name = "문의사항")
public class QaController {
    private final QaService qaService;

    @PostMapping
    @Operation(summary = "qa 등록")
    public ResultResponse<String> postQa(@Valid @RequestBody QaReq p){
        log.info("p: {}",p);
        qaService.insQa(p);
        return ResultResponse.<String>builder()
                .resultMessage("등록 완료")
                .resultData("문의사항이 등록 되었습니다.")
                .build();
    }

}
