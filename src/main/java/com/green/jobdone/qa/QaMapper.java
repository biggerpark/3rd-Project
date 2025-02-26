package com.green.jobdone.qa;

import com.green.jobdone.qa.model.QaRes;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface QaMapper {
    List<QaRes> getQa(int offset);
}
