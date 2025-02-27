package com.green.jobdone.category;

import com.green.jobdone.category.detail.model.DetailTypeGetReq;
import com.green.jobdone.category.detail.model.DetailTypeGetRes;
import com.green.jobdone.category.detail.model.DetailTypePostReq;
import com.green.jobdone.category.model.CategoryGetRes;
import com.green.jobdone.category.model.CategoryPostReq;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CategoryMapper {
    int insCategory(CategoryPostReq p);
    int existCategory(String categoryName);

    List<CategoryGetRes> getCategory();

    // 디테일 타입

    int insDetailType(DetailTypePostReq p);
    int existDetailType(long categoryId,String detailTypeName);

    List<DetailTypeGetRes> getDetailType(DetailTypeGetReq p);

}
