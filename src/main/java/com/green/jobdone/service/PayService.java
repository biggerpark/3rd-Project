package com.green.jobdone.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.green.jobdone.common.KaKaoPay;
import com.green.jobdone.common.exception.CustomException;
import com.green.jobdone.common.exception.ServiceErrorCode;
import com.green.jobdone.common.model.Domain;
import com.green.jobdone.config.security.AuthenticationFacade;
import com.green.jobdone.service.model.Dto.CancelDto;
import com.green.jobdone.service.model.Dto.CompletedDto;
import com.green.jobdone.service.model.Dto.KakaoPayDto;
import com.green.jobdone.service.model.KakaoPayCancelRes;
import com.green.jobdone.service.model.KakaoPayRedayRes;
import com.green.jobdone.service.model.KakaoPayRes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.view.RedirectView;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;


@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class PayService {
    private final KaKaoPay kaKaoPay;
    private final ServiceRepository serviceRepository;
    private final AuthenticationFacade authenticationFacade;
    private RestTemplate restTemplate = new RestTemplate();
    private final ServiceMapper serviceMapper;
    private KakaoPayRedayRes kakaoPayRedayRes;
    private final Domain domain;


    private HttpHeaders getHeaders(){
        HttpHeaders headers = new HttpHeaders();
        // 스프링프레임워크꺼 써야됨
        String auth = "SECRET_KEY " + kaKaoPay.getSecretKey();
        headers.set("Authorization", auth);
        headers.set("Content-Type", "application/json");
        return headers;
    }

    public KakaoPayRedayRes useKakaoPay(Long serviceId){

        log.info("serviceId : {}", serviceId);
        KakaoPayDto kakaoPayDto = serviceMapper.serviceInfo(serviceId);
        int com = serviceRepository.completedByServiceId(serviceId);
        if(com!=2){
            throw new CustomException(ServiceErrorCode.FAIL_UPDATE_SERVICE);
        }
        //요청할걸 담는 부분
        Map<String , Object> params = new HashMap<>();
        params.put("cid",kaKaoPay.getCid()); //
        params.put("partner_order_id", serviceId); // 가맹점 주문번호(serviceId)
        params.put("partner_user_id", kakaoPayDto.getUserId()); // 가맹점 회원 ID(주문 userId)
        params.put("item_name", kakaoPayDto.getProductName()); // 상품명
        params.put("quantity", 1); // 상품 수량
        params.put("total_amount", kakaoPayDto.getPrice()); // 총 금액
        params.put("vat_amount", kakaoPayDto.getPrice()/10); // 부가세
        params.put("tax_free_amount", 0); // 비과세 금액
        String server = domain.getServer();
//        String server = "localhost:8080";
        String approval_url = String.format("https://%s/api/payment/success?service_id=%d", server,serviceId);
        params.put("approval_url", approval_url); // 결제 성공 시 이동할 URL
        params.put("cancel_url", "https://"+server+"/api/payment/cancel"); // 결제 취소 시 이동할 URL
        params.put("fail_url", "https://"+server+"/api/payment/fail"); // 결제 실패 시 이동할 URL
        com.green.jobdone.entity.Service service = serviceRepository.findById(serviceId).orElse(null);
        service.setPayPrice(kakaoPayDto.getPrice());
        serviceRepository.save(service);
        log.info("params : {}", params);

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonParams = null;
        try {
            jsonParams = objectMapper.writeValueAsString(params);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        log.info("getHeader: {}",getHeaders());
        log.info("jsonParams: {}", jsonParams);


        //요청 부분
        HttpEntity<String> requestEntity = new HttpEntity<>(jsonParams, getHeaders());
        ResponseEntity<KakaoPayRedayRes> response = restTemplate.exchange(
                "https://open-api.kakaopay.com/online/v1/payment/ready",  // 카카오 API 요청 URL
                HttpMethod.POST,
                requestEntity,
                KakaoPayRedayRes.class
        );

        return response.getBody();
    }
    @Transactional
    public KakaoPayCancelRes cancelKakaoPay(Long serviceId){
        Long userId = authenticationFacade.getSignedUserId();
        CancelDto dto = serviceRepository.findCancelDtoByServiceId(serviceId);
//        if(dto.getCompleted()!=10){
        LocalDate threeDaysAgo = dto.getStartDate().minusDays(3);
        // 오늘부터 3일 전 1월 4일이면 1월 1일
        if(!userId.equals(dto.getUserId())){
            throw new CustomException(ServiceErrorCode.USER_MISMATCH);
        } //관리자라 여기 필요x >> 이제 관리자 아님 필요함
        log.info("오늘 : {}, 아래 불린 {}",LocalDate.now(),!threeDaysAgo.isBefore(LocalDate.now()));
        if(dto.getCompleted()==6 && !threeDaysAgo.isBefore(LocalDate.now())){
            serviceRepository.updCompleted(serviceId,10);
        } else {
            throw new CustomException(ServiceErrorCode.TIME_OVER);
        }
//        serviceRepository.updCompleted(serviceId,11);


        Map<String ,Object> req = new HashMap<>();
        req.put("cid",kaKaoPay.getCid());
        req.put("tid",dto.getTid());
        req.put("cancel_amount",dto.getPrice());
        req.put("cancel_tax_free_amount",0);
        req.put("cancel_vat_amount",dto.getPrice()/10);

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonParams = null;
        try {
            jsonParams = objectMapper.writeValueAsString(req);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        log.info("jsonParams: {}", jsonParams);
        log.info("Headers: {} ",getHeaders());
        HttpEntity<String> requestEntity = new HttpEntity<>(jsonParams, getHeaders());
        ResponseEntity<KakaoPayCancelRes> res = restTemplate.exchange(
                "https://open-api.kakaopay.com/online/v1/payment/cancel",  // 카카오 API 요청 URL
                HttpMethod.POST,
                requestEntity,
                KakaoPayCancelRes.class
        );

        return res.getBody();
    }

    public void saveTid(Long serviceId, String tid){
//        serviceMapper.saveTid(serviceId,tid);
        serviceRepository.saveTid(serviceId,tid);
    }

    @Transactional
    public RedirectView payRes(String pgToken, Long serviceId){
        log.info("serviceId: {}",serviceId);

        // 요청 전송
//        String tid = (String)SessionUtil.getAttribute("tid");
//        Long serviceId = (Long)SessionUtil.getAttribute("serviceId");

//        String tid = (String)session.getAttribute("tid");
//        Long serviceId = (Long) session.getAttribute("serviceId");
//        String tid = "1";
//        Long serviceId = null;
        KakaoPayDto kakaoPayDto = serviceMapper.serviceInfo(serviceId);
        int com = serviceRepository.completedByServiceId(serviceId);
        if(com!=2){
            throw new CustomException(ServiceErrorCode.FAIL_UPDATE_SERVICE);
        }
        log.info("kakaoPayDto: {}", kakaoPayDto);

        Map<String,String> params = new HashMap<>();
        params.put("cid",kaKaoPay.getCid());
        params.put("tid",kakaoPayDto.getTid());
        params.put("partner_order_id", String.valueOf(serviceId));
        params.put("partner_user_id", String.valueOf(kakaoPayDto.getUserId()));
        params.put("pg_token",pgToken);

        // 파라미터, 헤더
        HttpEntity<Map<String ,String>> requestEntity = new HttpEntity<>(params, getHeaders());
        log.info("requestEntity: {}", requestEntity);
//        CompletedDto dto = new CompletedDto();
//        dto.setServiceId(serviceId);

//        int res = serviceMapper.payOrDoneCompleted(dto);
//        if(res==0){
//            throw new RuntimeException();
//        }
        serviceRepository.updateServiceStatus(serviceId,6);

        // 카톡 메세지 보냄
        RestTemplate restTemplate = new RestTemplate();
        KakaoPayRes kakaoPayRes = restTemplate.postForObject(
                "https://open-api.kakaopay.com/online/v1/payment/approve",
                requestEntity,KakaoPayRes.class);
        log.info("kakaoPayRedayRes: {}", kakaoPayRes);

        String redirectUrl = String.format("https://%s/paySuccess", domain.getServer());
        log.info("redirectUrl: {}",redirectUrl);
        return new RedirectView(redirectUrl);
        // 여기 만나서 바로 이동하는식
    }
}
