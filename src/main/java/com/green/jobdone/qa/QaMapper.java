package com.green.jobdone.qa;

import com.green.jobdone.qa.model.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface QaMapper {
    List<QaRes> getQa(boolean isAdmin,long signedUserId);
    QaDetailRes getQaDetail(long qaId);
//    List<QaReportRes> getQaReport(int offset);
    QaAnswerRes getQaAnswer(long qaId);
    List<QaTypeDetailRes> getQaTypeDetail(long qaTypeId);
    List<QaBoardRes> getQaBoard();

}
