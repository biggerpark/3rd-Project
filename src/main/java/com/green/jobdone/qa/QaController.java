package com.green.jobdone.qa;


import com.green.jobdone.common.model.ResultResponse;
import com.green.jobdone.qa.model.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("qa")
@Slf4j
@Tag(name = "문의사항")
public class QaController {
    private final QaService qaService;

    @PostMapping
    @Operation(summary = "문의 및 신고 등록")
    public ResultResponse<Integer> postQa(@Valid @RequestPart QaReq p, @Valid @RequestPart(required = false) List<MultipartFile> pics){
        log.info("p: {}",p);
        qaService.insQa(p,pics);
        return ResultResponse.<Integer>builder()
                .resultMessage("문의/신고 등록 완료")
                .resultData(1)
                .build();
    }


    @GetMapping
    @Operation(summary = "문의사항 조회,관리자 및 유저 조회, 같은 api 사용, 토큰에 따라 보내는 데이터 틀려짐, 관리자는 모든 유저 문의 보여지고, 유저는 해당 유저가 문의한 글만 보여짐")
    public ResultResponse<List<QaRes>> getQa(){
        List<QaRes> res = qaService.getQa();

        return ResultResponse.<List<QaRes>>builder()
                .resultMessage("문의사항 조회 완료")
                .resultData(res)
                .build();

    }

        @GetMapping("detail")
        @Operation(summary = "문의내역 상세 조회, 관리자 및 유저가 조회, 관리자가 조회하면 qa 상태가 '검토중' 으로 바뀜, 유저/업체는 마이페이지에서 조회할때 사용할 api, 즉 여기서는 문의 게시글 조회수가 증가 안됨")
        public ResultResponse<QaDetailRes> getQaDetail(@RequestParam long qaId){
            QaDetailRes result=qaService.getQaDetail(qaId);


            return ResultResponse.<QaDetailRes>builder()
                .resultMessage("문의 내역 상세 조회 완료")
                .resultData(result)
                .build();

    }

    @PostMapping("answer")
    @Operation(summary = "문의 답변 , 관리자가 답변, qa 상태가 '답변완료' 으로 바뀜")
    public ResultResponse<Integer> postQaAnswer(@RequestBody QaAnswerReq p){
        Integer res = qaService.postQaAnswer(p);

        return ResultResponse.<Integer>builder()
                .resultMessage("문의 답변 완료")
                .resultData(res)
                .build();

    }

    @GetMapping("answer")
    @Operation(summary = "관리자가 등록한 답변 확인, 유저가 확인")
    public ResultResponse<QaAnswerRes> getQaAnswer(@RequestParam long qaId){

        QaAnswerRes result=qaService.getQaAnswer(qaId);

        return ResultResponse.<QaAnswerRes>builder()
                .resultMessage("등록된 답변 확인 완료")
                .resultData(result)
                .build();
    }


    @GetMapping("qaTypeId")
    @Operation(summary = "문의/신고 등록시 나오는 문의 상세 정보들 확인")
    public ResultResponse<List<QaTypeDetailRes>> getQaTypeDetail(@RequestParam long qaTypeId){
        List<QaTypeDetailRes> result = qaService.getQaTypeDetail(qaTypeId);

        return ResultResponse.<List<QaTypeDetailRes>>builder()
                .resultMessage("문의 상세 정보들 확인 완료")
                .resultData(result)
                .build();
    }


    @GetMapping("qaBoardDetail")
    @Operation(summary = "게시판에서 해당 문의를 눌렀을때 게시글 상세 조회 및 조회수 증가")
    public ResultResponse<QaDetailRes> getQaBoardDetail(@RequestParam long qaId){
        QaDetailRes result = qaService.getQaBoardDetail(qaId);

        return ResultResponse.<QaDetailRes>builder()
                .resultMessage("게시판 게시글 상세 조회 및 조회수 증가 성공")
                .resultData(result)
                .build();

    }

    @GetMapping("qaBoard")
    @Operation(summary = "게시판에서 모든 문의글 조회 완료")
    public ResultResponse<List<QaBoardRes>> getQaBoard(){

        List<QaBoardRes> result = qaService.getQaBoard();

        return ResultResponse.< List<QaBoardRes>>builder()
                .resultMessage("게시판 모든 문의글 조회 완료")
                .resultData(result)
                .build();
    }

    @DeleteMapping
    @Operation(summary = "관리자/ 문의 및 신고내역 확인에서 해당 문의를 삭제할 수 있음")
    public ResultResponse<Integer> deleteQa(@RequestParam long qaId){
        Integer res = qaService.deleteQa(qaId);

        return ResultResponse.<Integer>builder()
                .resultMessage("문의 삭제 완료")
                .resultData(res)
                .build();
    }



//    @GetMapping("report")
//    @Operation(summary = "신고내역조회, 관리자 및 유저도 조회,토큰받아서 role 로  구분할거임")
//    public ResultResponse<List<QaReportRes>> getQaReport(@RequestParam int page){
//        List<QaReportRes> result=qaService.getQaReport(page);
//
//
//        return ResultResponse.<List<QaReportRes>>builder()
//                .resultMessage("신고내역조회 조회 완료")
//                .resultData(result)
//                .build();
//
//    }



}
