package com.green.jobdone.qa;


import com.green.jobdone.common.model.ResultResponse;
import com.green.jobdone.qa.model.QaAnswerReq;
import com.green.jobdone.qa.model.QaDetailRes;
import com.green.jobdone.qa.model.QaReq;
import com.green.jobdone.qa.model.QaRes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("qa")
@Slf4j
@Tag(name = "문의사항")
public class QaController {
    private final QaService qaService;

    @PostMapping
    @Operation(summary = "문의 등록")
    public ResultResponse<Integer> postQa(@Valid @RequestBody QaReq p){
        log.info("p: {}",p);
        qaService.insQa(p);
        return ResultResponse.<Integer>builder()
                .resultMessage("등록 완료")
                .resultData(1)
                .build();
    }


    @GetMapping
    @Operation(summary = "문의사항 조회,관리자가 조회")
    public ResultResponse<List<QaRes>> getQa(@RequestParam int page){
        List<QaRes> res = qaService.getQa(page);

        return ResultResponse.<List<QaRes>>builder()
                .resultMessage("문의사항 조회 완료")
                .resultData(res)
                .build();

    }

    @GetMapping("detail")
    @Operation(summary = "문의내역 상세 조회, 관리자가 조회")
    public ResultResponse<QaDetailRes> getQaDetail(@RequestParam long qaId){
        QaDetailRes result=qaService.getQaDetail(qaId);


        return ResultResponse.<QaDetailRes>builder()
                .resultMessage("문의 내역 상세 조회 완료")
                .resultData(result)
                .build();

    }

    @PostMapping("answer")
    @Operation(summary = "문의 답변 , 관리자가 답변")
    public ResultResponse<Integer> postQaAnswer(@RequestBody QaAnswerReq p){
        Integer res = qaService.postQaAnswer(p);

        return ResultResponse.<Integer>builder()
                .resultMessage("문의 답변 완료")
                .resultData(res)
                .build();

    }


}
