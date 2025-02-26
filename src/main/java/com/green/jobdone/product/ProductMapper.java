package com.green.jobdone.product;

import com.green.jobdone.product.model.*;
import com.green.jobdone.product.model.dto.ProductOptionPostDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ProductMapper {
    int postProduct(ProductPostReq p);
    Long checkBusinessProduct(long businessId);
    int updProduct(ProductPatchReq p);
    Long checkUserBusiness(long businessId);
    Long checkUserUpdProduct(long productId);


    int postOption(ProductOptionPostReq p);
    List<String> checkOption(long detailTypeId);
    List<ProductGetOption> getProductOption();

    int postProductOption(ProductOptionPostDto p);
    List<Long> checkProductOption(Long productId);
    Long checkUserProductOption(long productId);




    int postOptionDetail(ProductOptionDetailPostReq p);
    List<String> checkProductOptionDetail(long productOptionId);
    int updOptionDetail(ProductOptionDetailPatchReq p);
    Long checkUserOptionDetail(long productOptionId);
    Long checkUserUpdOptionDetail(long optionDetailId);

    ProductGetRes getProductInfoByBusiness(long businessId);

}
