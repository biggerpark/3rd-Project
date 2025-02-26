package com.green.jobdone.admin;

import com.green.jobdone.admin.model.BusinessApplicationGetRes;
import com.green.jobdone.admin.model.BusinessCategoryRes;
import jakarta.persistence.ManyToOne;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AdminMapper {
    List<BusinessApplicationGetRes> getBusinessApplication(int offset);
    List<BusinessCategoryRes> getBusinessCategory(long categoryId);
}
