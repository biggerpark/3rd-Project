package com.green.jobdone.business;

import com.green.jobdone.business.model.*;
import com.green.jobdone.business.model.get.*;
import com.green.jobdone.business.phone.BusinessPhonePostReq;
import com.green.jobdone.business.pic.BusinessOnePicsGetReq;
import com.green.jobdone.business.pic.BusinessOnePicsGetRes;
import com.green.jobdone.business.pic.BusinessPicDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BusinessMapper {
    long insBusiness(BusinessPostSignUpReq p); //
    int udtBusiness(BusinessDetailPutReq p);
    int udtBusinessLogo(BusinessLogoPatchReq p);
    int udtBusinessPaper(BusinessPaperPatchReq p);

    //유효성검사
    int existBusinessNum(String businessNum);
    long existBusinessId(long businessId); // 로그인한 유저가 해당업체 관리자인지?

    //pic
    int insBusinessPic(BusinessPicDto p);
    int putBusinessPic(long businessPicId);
    List<BusinessOnePicsGetRes> getBusinessPicList(BusinessOnePicsGetReq p);
    int delBusinessPic(BusinessPicReq p);
    String getBusinessPicName(long businessPicId);
    int udtBusinessThumbnail(BusinessPicReq p);

    //state
    int putBusinessState(BusinessStatePutReq p);

    //get 하 내일하자 --> 완
    List<BusinessGetRes> selAllBusiness(BusinessGetReq p);
    BusinessGetOneRes selOneBusiness(long businessId);

    List<BusinessGetMonthlyRes> getBusinessMonthly(BusinessGetMonthlyReq p);














    //businessPhone
    int insBusinessPhone(BusinessPhonePostReq p);
    int existBusinessPhone(long businessId, String phone);

}
