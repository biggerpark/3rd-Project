package com.green.jobdone.service;

import com.green.jobdone.common.exception.CustomException;
import com.green.jobdone.common.exception.ServiceErrorCode;
import com.green.jobdone.config.security.AuthenticationFacade;
import com.green.jobdone.service.model.*;
import com.green.jobdone.service.model.Dto.CompletedDto;
import com.green.jobdone.service.model.Dto.ServiceEtcDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class ServiceService {
    private final ServiceMapper serviceMapper;
    private final AuthenticationFacade authenticationFacade;


    @Transactional
    public int postService(ServicePostReq p){
        String st = String.format(p.getMStartTime()+":00");
        p.setMStartTime(st);

        Long userId = authenticationFacade.getSignedUserId();
        p.setUserId(userId);

        int res1 = serviceMapper.insService(p);
        int res2 = serviceMapper.insServiceDetail(p);
        int res = serviceMapper.insServiceOption(p);
        return res;
    }

    public List<ServiceGetRes> getService(ServiceGetReq p){

        Long userId = authenticationFacade.getSignedUserId();
        p.setUserId(userId);
        if(p.getStatus()>5 || p.getStatus()<0){
            throw new CustomException(ServiceErrorCode.INVALID_SERVICE_STATUS);
        }
        if(p.getBusinessId()!= null && !p.getUserId().equals(serviceMapper.findUserId(p.getBusinessId()))) {
            throw new CustomException(ServiceErrorCode.BUSINESS_OWNER_MISMATCH);
        }

//        if(p == null || p.getUserId()==null && p.getBusinessId()==null){
//            return new ArrayList<>();
//        } // 위 주석을 풀면(JWT 인증처리하면) 주석처리하기

        List<ServiceGetRes> res = serviceMapper.GetServiceFlow(p);
        return res;
    }

    @Transactional
    public ServiceGetOneRes getOneService(ServiceGetOneReq p){

        log.info("p:{}",p);
        Long businessId = p.getBusinessId();
        // 13으로 찍힘

        ServiceGetOneRes res = serviceMapper.GetServiceOne(p);
        Long userId = null;
        try{
            userId = authenticationFacade.getSignedUserId();
        } catch (Exception e){
            e.printStackTrace();
        }
        List<ServiceEtcDto> dto = serviceMapper.GetEtc(p.getServiceId());
        res.setEtc(dto);
        // 토큰의 userId

        if(p.getServiceId()==0){
            return null;
        }

        if(businessId==null && userId==null || res.getUserId()!=userId) {
            res.setUserName("");
            res.setUserPhone("");
            res.setAddress("");
           return res;
            //이부분 어케할지 userId 없이도 볼수 있도록? 주소가 보여버리는데??
        }
        log.info("businessId:{}",businessId);
        if(businessId != null && !userId.equals(serviceMapper.findUserId(businessId))) {
            // 토큰의 userId랑 businessId를 이용한 조회(userId) 다르면 리턴
            throw new CustomException(ServiceErrorCode.BUSINESS_OWNER_MISMATCH);
        }

        return res;
    }

    @Transactional
    public int updService(ServicePutReq p){
        Long serviceProviderUserId = serviceMapper.providerUserId(p.getServiceId());
        if(!serviceProviderUserId.equals(authenticationFacade.getSignedUserId())){
//             p.getProviderUserId 대신 authenticationFacade.getSignedUserId()
            throw new CustomException(ServiceErrorCode.BUSINESS_OWNER_MISMATCH);
        }
        String st = String.format(p.getMStartTime()+":00");
        p.setMStartTime(st);
        String et = String.format(p.getMEndTime()+":00");
        p.setMEndTime(et);
        List<ServiceEtcDto> etcDto = p.getEtc();
        int sum = 0;
        for(ServiceEtcDto dto : etcDto){
            sum += dto.getEtcPrice();
        }

        if(sum!=0){
            int realPrice = p.getTotalPrice();
            // totalPrice = price(수정하면 프론트 귀찮을까봐 냅둠)
            // 처음 get때 받는 price에 해당하는 부분
            realPrice += sum;
            p.setTotalPrice(realPrice);
            // realPrice = totalPrice
        }

        int res1 = serviceMapper.updService(p);
        int res2 = serviceMapper.updServiceDetail(p);
        int res4 = serviceMapper.updServiceEtc(p);
//        int res3 = serviceMapper.updServiceOption(p);
        return res4;
    }

    @Transactional
    public int completedService(ServicePatchReq p){
        p.setUserId(authenticationFacade.getSignedUserId());
        int com = serviceMapper.getCompleted(p.getServiceId());
        if(!transitionAllowed(com,p.getCompleted(),p.getBusinessId())) {
            throw new CustomException(ServiceErrorCode.INVALID_SERVICE_STATUS);
        }
        if(p.getBusinessId()==null){
            int res = serviceMapper.patchCompleted(p);
            // xml에서 userId가 해당되는 경우에만 가능하도록 해놨음
            return res;
        }

        Long findUserId = serviceMapper.findUserId(p.getBusinessId());


        if(!findUserId.equals(p.getUserId())){
            throw new CustomException(ServiceErrorCode.BUSINESS_OWNER_MISMATCH);
        }
        p.setUserId(0);

        if(p.getCompleted()==7){
            CompletedDto dto = new CompletedDto();
            dto.setServiceId(p.getServiceId());
            dto.setBusinessId(p.getBusinessId());
            return serviceMapper.payOrDoneCompleted(dto);
        }


            int res = serviceMapper.patchCompleted(p);
            return res;

    }

    private boolean transitionAllowed(int oldCompleted, int newCompleted, Long businessId){

        Map<Integer, List<Integer>> businessAllowed = Map.of(
                0, List.of(1, 5),
                1, List.of(2),
                2, List.of(5)
        );


        Map<Integer, List<Integer>> userAllowed = Map.of(
                0, List.of(1, 3, 5),
                1, List.of(2),
                2, List.of(1, 3, 5, 6),
                3, List.of(4),
                6, List.of(7),
                7, List.of(8, 9)
        );

        // 업체일 때 상태변환 허용
        if (businessId != null) {
            return businessAllowed.getOrDefault(oldCompleted, List.of()).contains(newCompleted);
        }

        // 유저일 때 상태변환 허용
        return userAllowed.getOrDefault(oldCompleted, List.of()).contains(newCompleted);
    }
}
