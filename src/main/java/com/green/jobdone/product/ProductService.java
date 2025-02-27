package com.green.jobdone.product;

import com.green.jobdone.business.BusinessRepository;
import com.green.jobdone.common.exception.CommonErrorCode;
import com.green.jobdone.common.exception.CustomException;
import com.green.jobdone.common.exception.ServiceErrorCode;
import com.green.jobdone.config.security.AuthenticationFacade;
import com.green.jobdone.entity.Option;
import com.green.jobdone.entity.OptionDetail;
import com.green.jobdone.entity.Product;
import com.green.jobdone.product.model.*;
import com.green.jobdone.product.model.dto.OptionDto;
import com.green.jobdone.product.model.dto.ProductOptionDetailDto;
import com.green.jobdone.product.model.dto.ProductOptionPostDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductService {
    private final ProductMapper mapper;
    private final AuthenticationFacade authenticationFacade;
    private final ProductRepository productRepository;
    private final OptionRepository optionRepository;
    private final OptionDetailRepository optionDetailRepository;
    private final BusinessRepository businessRepository;

    public int postProduct(ProductPostReq p) {

        long userId=authenticationFacade.getSignedUserId();

        Long checkUserId=mapper.checkUserBusiness(p.getBusinessId());

        if(userId!=checkUserId){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "해당 업체에 대한 권한이 없습니다");
        }


        Long businessId = mapper.checkBusinessProduct(p.getBusinessId());

        if (businessId == null || businessId == 0L) {
            int result = mapper.postProduct(p);
            return result;
        } else {

            return 0;


        }
    }


    //관리자가함. >> 업체가 하기로 변경
    public int postOption(ProductOptionPostReq p) {


        List<String> list = mapper.checkOption(p.getProductId());

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

        long userId=authenticationFacade.getSignedUserId();

        Long checkUserId=mapper.checkUserProductOption(p.getProductId());

        if(userId!=checkUserId){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "해당 업체에 대한 권한이 없습니다");
        }



        List<Long> list = mapper.checkProductOption(p.getProductId());

        if (list == null || list.size() == 0) {
            int result = mapper.postProductOption(p);

            return result;
        }

        for (Long s : list) {
            if (s.equals(p.getName())) {
                return 0;
            }
        }


        int result = mapper.postProductOption(p);

        return result;

    }


    public int postOptionDetail(ProductOptionDetailPostReq p) {

        long userId=authenticationFacade.getSignedUserId();

        Long checkUserId=mapper.checkUserOptionDetail(p.getOptionId());

        if(userId!=checkUserId){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "해당 업체에 대한 권한이 없습니다");
        }




        List<String> list = mapper.checkProductOptionDetail(p.getOptionId());

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

        long userId=authenticationFacade.getSignedUserId();

        Long checkUserId=mapper.checkUserUpdOptionDetail(p.getOptionDetailId());

        if(userId!=checkUserId){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "해당 업체에 대한 권한이 없습니다");
        }


        int result = mapper.updOptionDetail(p);

        return result;
    }

    public int updProduct(ProductPatchReq p) {

        long userId=authenticationFacade.getSignedUserId();

        Long checkUserId=mapper.checkUserUpdProduct(p.getProductId());


        if(userId!=checkUserId){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "해당 업체에 대한 권한이 없습니다");
        }


        int result = mapper.updProduct(p);

        return result;
    }

    @Transactional
    public void postAll(ProductPostAllReq p){
        if (p.getProductId() == null) {
            throw new CustomException(CommonErrorCode.NOT_EXIST_BUSINESS);
        }
        if(!businessRepository.findUserIdByProductId(p.getProductId()).equals(authenticationFacade.getSignedUserId())){
            throw new CustomException(ServiceErrorCode.BUSINESS_OWNER_MISMATCH);
        };
        Product product = productRepository.findById(p.getProductId()).orElse(null);
        if(product!=null && (p.getPrice() != null) && !p.getPrice().equals(0)){
            product.setPrice(p.getPrice());
            productRepository.save(product);
        }
//        List<Option> optionList = new ArrayList<>(p.getOptions().size());
        if(p.getOptions()!=null && !p.getOptions().isEmpty()) {
            for (OptionDto ol : p.getOptions()) {
                Option option = new Option();
                if (ol.getOptionId() != null && ol.getOptionId() != 0) {
                    option = optionRepository.findById(ol.getOptionId()).orElseGet(Option::new);
                }
                option.setProduct(product);
                option.setName(ol.getOptionName());
//                optionList.add(option);
                List<OptionDetail> optionDetailList = new ArrayList<>(ol.getOptionDetails().size());

                if (ol.getOptionDetails() != null && !ol.getOptionDetails().isEmpty()) {
                    for (ProductOptionDetailDto od : ol.getOptionDetails()) {
                        OptionDetail optionDetail = new OptionDetail();

                        if (od.getOptionDetailId() != null) {
                            optionDetail = optionDetailRepository.findById(od.getOptionDetailId())
                                    .orElseGet(OptionDetail::new);
                        }
                        optionDetail.setOption(option);
                        optionDetail.setName(od.getOptionDetailName());
                        optionDetail.setPrice(od.getOptionDetailPrice());
                        optionDetailList.add(optionDetail);
                    }
                }
                optionRepository.save(option);
                optionDetailRepository.saveAll(optionDetailList);
            }
        }
//        optionRepository.saveAll(optionList);
    }
    @Transactional
    public void delOption(ProductOptionDelReq p){
        if(authenticationFacade.getSignedUserId()!=businessRepository.findUserIdByBusinessId(p.getBusinessId())){
            throw new CustomException(ServiceErrorCode.BUSINESS_OWNER_MISMATCH);
        }
        Option option = optionRepository.findById(p.getOptionId()).orElseThrow(() -> new CustomException(ServiceErrorCode.OPTION_NOT_FOUND));
        optionRepository.delete(option);
    }

    @Transactional
    public void delOptionDetail(ProductOptionDetailDelReq p){
        if(authenticationFacade.getSignedUserId()!=businessRepository.findUserIdByBusinessId(p.getBusinessId())){
            throw new CustomException(ServiceErrorCode.BUSINESS_OWNER_MISMATCH);
        }
        OptionDetail optionDetail = optionDetailRepository.findById(p.getOptionDetailId()).orElseThrow(() -> new CustomException((ServiceErrorCode.OPTION_DETAIL_NOT_FOUND)));
        optionDetailRepository.delete(optionDetail);
    }
}
