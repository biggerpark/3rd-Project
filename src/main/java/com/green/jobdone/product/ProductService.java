package com.green.jobdone.product;

import com.green.jobdone.config.security.AuthenticationFacade;
import com.green.jobdone.product.model.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductService {
    private final ProductMapper mapper;
    private final AuthenticationFacade authenticationFacade;

    public int postProduct(ProductPostReq p) {

//        if (p.getUserId() != authenticationFacade.getSignedUserId()) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "잘못된 요청 입니다.");
//        } //나중에 최종적으로 주석 풀기


        Long businessId = mapper.checkBusinessProduct(p.getBusinessId());

        if (businessId == null || businessId == 0L) {
            int result = mapper.postProduct(p);
            return result;
        } else {

            return 0;


        }
    }


    //관리자가함.
    public int postOption(ProductOptionPostReq p) {


        List<String> list = mapper.checkOption(p.getDetailTypeId());

        if (list == null || list.size() == 0) {
            int result = mapper.postOption(p);

            return result;
        }

        for (String s : list) {
            if (s.equals(p.getOptionName())) {
                return 0;
            }
        }

        int result = mapper.postOption(p);

        return result;

    }

    public List<ProductGetOption> getProductOption() {

        return mapper.getProductOption();
    }


    public int postProductOption(ProductOptionPostDto p) {

//        if (p.getUserId() != authenticationFacade.getSignedUserId()) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "잘못된 요청 입니다.");
//        } //나중에 최종적으로 주석 풀기

        List<Long> list = mapper.checkProductOption(p.getProductId());

        if (list == null || list.size() == 0) {
            int result = mapper.postProductOption(p);

            return result;
        }

        for (Long s : list) {
            if (s.equals(p.getOptionId())) {
                return 0;
            }
        }


        int result = mapper.postProductOption(p);

        return result;

    }


    public int postOptionDetail(ProductOptionDetailPostReq p) {

//        if (p.getUserId() != authenticationFacade.getSignedUserId()) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "잘못된 요청 입니다.");
//        } //나중에 최종적으로 주석 풀기

        List<String> list = mapper.checkProductOptionDetail(p.getProductOptionId());

        if (list == null || list.size() == 0) {
            int result = mapper.postOptionDetail(p);

            return result;
        }

        for (String s : list) {
            if (s.equals(p.getOptionDetailName())) {
                return 0;
            }
        }

        int result = mapper.postOptionDetail(p);

        return result;


    }


    public ProductGetRes getProductInfoByBusiness(long businessId) {

        ProductGetRes result = mapper.getProductInfoByBusiness(businessId);

        return result;


    }

    public int updOptionDetail(ProductOptionDetailPatchReq p) {

//        if (p.getUserId() != authenticationFacade.getSignedUserId()) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "잘못된 요청 입니다.");
//        } //나중에 최종적으로 주석 풀기

        int result = mapper.updOptionDetail(p);

        return result;
    }

    public int updProduct(ProductPatchReq p) {

//        if (p.getUserId() != authenticationFacade.getSignedUserId()) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "잘못된 요청 입니다.");
//        } //나중에 최종적으로 주석 풀기

        int result = mapper.updProduct(p);

        return result;
    }


}
