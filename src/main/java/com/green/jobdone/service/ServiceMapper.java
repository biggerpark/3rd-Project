package com.green.jobdone.service;

import com.green.jobdone.service.model.*;
import com.green.jobdone.service.model.Dto.CompletedDto;
import com.green.jobdone.service.model.Dto.KakaoPayDto;
import com.green.jobdone.service.model.Dto.ServiceEtcDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ServiceMapper {

    int insService(ServicePostReq p);
    int insServiceDetail(ServicePostReq p);
    int insServiceOption(ServicePostReq p);
    List<ServiceGetRes> GetServiceFlow(ServiceGetReq p);
    ServiceGetOneRes GetServiceOne(ServiceGetOneReq p);
    int updService(ServicePutReq p);
    int updServiceDetail(ServicePutReq p);
    int updServiceOption(ServicePutReq p);
    Long providerUserId(Long serviceId);
    Long findUserId(Long businessId);
    List<ServiceEtcDto> GetEtc(Long serviceId);
    int getCompleted(long serviceId);
    int patchCompleted(ServicePatchReq p);
    int updServiceEtc(ServicePutReq p);
    KakaoPayDto serviceInfo(Long serviceId);
    int payOrDoneCompleted(CompletedDto dto);
    void saveTid(Long serviceId, String tid);
    List<Long> findEtcId(Long serviceId);
}
