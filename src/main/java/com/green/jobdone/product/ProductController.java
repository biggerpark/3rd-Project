package com.green.jobdone.product;

import com.green.jobdone.common.model.ResultResponse;
import com.green.jobdone.product.model.*;
import com.green.jobdone.product.model.dto.ProductOptionPostDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("product")
@Slf4j
@RequiredArgsConstructor
@Tag(name = "상품")
public class ProductController {
    private final ProductService service;


    @PostMapping
    @Operation(summary = "업체별 업체 상품 등록, 업체가 등록함,ex) 우리 업체는 청소 카테고리(categoryId)의 소분류(detailTypeId) 중 하나를 product 로 정하겠다 ")
    public ResultResponse<Integer> postProduct(@RequestBody ProductPostReq p) {


        int result = service.postProduct(p);


        return ResultResponse.<Integer>builder()
                .resultMessage(result==0?"이미 등록하셨습니다":"상품 등록 완료")
                .resultData(result)
                .build();

    }



    @PostMapping("option")
    @Operation(summary = "관리자 상품 소분류 옵션 등록, 관리자가 등록함 ex) 관리자가 청소 카테고리 소분류 중 하나인 집청소의 옵션으로 방갯수,화장실 갯수 등을 설정할 수 있음")
    public ResultResponse<Integer> postOption(@RequestBody ProductOptionPostReq p) {
        int result = service.postOption(p);


        return ResultResponse.<Integer>builder()
                .resultMessage(result == 0 ? "이미 등록된 상품 소분류  옵션입니다" : "상품 소분류 옵션 등록 완료")
                .resultData(result)
                .build();
    }

    @GetMapping("option")
    @Operation(summary = "관리자가 등록한 detailTypeId 별 모든 옵션 조회, 관리자가 조회")
    public ResultResponse<List<ProductGetOption>> getProductOption() {
        List<ProductGetOption> result = service.getProductOption();


        return ResultResponse.<List<ProductGetOption>>builder()
                .resultMessage("모든 옵션 조회 완료")
                .resultData(result)
                .build();
    }







    @PostMapping("productOption")
    @Operation(summary = "업체 상품별 소분류 옵션 등록, 업체가 등록함 ex) 업체의 productId 는 관리자가 등록한 optionId 중 원하는 다양한 옵션(optionId)을 정할 수 있다.")
    public ResultResponse<Integer> postProductOption(@RequestBody ProductOptionPostDto p) {
        int result = service.postProductOption(p);


        return ResultResponse.<Integer>builder()
                .resultMessage(result == 0 ? "이미 등록된 상품 소분류  옵션입니다" : "업체 상품별 소분류 옵션 등록 완료")
                .resultData(result)
                .build();
    }




    @PostMapping("optionDetail")
    @Operation(summary = "업체 상품 옵션 상세 정보 등록, 업체가 등록함 ex) 업체는 등록한 옵션(optionId)의 상세 옵션들을 설정할 수 있다. 방갯수 옵션이 있으면 1개,2개 등등 지정할 수 있다. ")
    public ResultResponse<Integer> postOptionDetail(@RequestBody ProductOptionDetailPostReq p) {


        int result = service.postOptionDetail(p);


        return ResultResponse.<Integer>builder()
                .resultMessage(result==0?"이미 등록된 상세 옵션입니다":"상품 상세 옵션 등록 완료")
                .resultData(result)
                .build();

    }



    @GetMapping
    @Operation(summary = "업체의 상품,상품상세옵션 조회 ex) 업체가 등록한 product 의 옵션들과 상세 옵션들을 모두 받아볼 수 있다.")
    public ResultResponse<ProductGetRes> getProductInfoByBusiness(@RequestParam long businessId) {


        ProductGetRes result = service.getProductInfoByBusiness(businessId);


        return ResultResponse.<ProductGetRes>builder()
                .resultMessage(result!=null?"업체의 상품,상품상세옵션 조회 완료":"등록된 상품,상품상세옵션이 없습니다")
                .resultData(result)
                .build();

    }

    @PatchMapping
    @Operation(summary = "업체 상품에 대한 가격 수정, 업체가 수정함")
    public ResultResponse<Integer> updProduct(@RequestBody ProductPatchReq p) {


        int result = service.updProduct(p);


        return ResultResponse.<Integer>builder()
                .resultMessage(result==0?"수정할 상품 가격이 없습니다":"상품 가격 수정 완료")
                .resultData(result)
                .build();

    }


    @PatchMapping("optionDetail")
    @Operation(summary = "업체 상품 옵션에 대한 상세 옵션 수정, 업체가 수정함")
    public ResultResponse<Integer> updOptionDetail(@RequestBody ProductOptionDetailPatchReq p) {


        int result = service.updOptionDetail(p);


        return ResultResponse.<Integer>builder()
                .resultMessage(result==0?"수정할 상세 옵션이 없습니다":"상품 상세 옵션 수정 완료")
                .resultData(result)
                .build();

    }

    @PostMapping("postAll")
    @Operation(summary = "상품가격, 옵션, 옵션디테일 한번에")
    public ResultResponse<Integer> postAll(@RequestBody ProductPostAllReq p) {
        log.info("p: {}",p);
        service.postAll(p);
        return ResultResponse.<Integer>builder()
                .resultData(1)
                .resultMessage("성공")
                .build();
    }




}
