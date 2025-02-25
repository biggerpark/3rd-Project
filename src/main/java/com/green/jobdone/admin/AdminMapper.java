package com.green.jobdone.admin;

import com.green.jobdone.admin.model.BusinessApplicationGetRes;
import jakarta.persistence.ManyToOne;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AdminMapper {
    List<BusinessApplicationGetRes> getBusinessApplication(int page);
}
