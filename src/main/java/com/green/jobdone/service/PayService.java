package com.green.jobdone.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.green.jobdone.common.KaKaoPay;
import com.green.jobdone.common.model.Domain;
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

import java.util.HashMap;
import java.util.Map;


@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class PayService {
    private final KaKaoPay kaKaoPay;
    private final ServiceRepository serviceRepository;
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
        String approval_url = String.format("%sapi/payment/success?service_id=%d", server,serviceId);
        params.put("approval_url", approval_url); // 결제 성공 시 이동할 URL
        params.put("cancel_url", server+"api/payment/cancel"); // 결제 취소 시 이동할 URL
        params.put("fail_url", server+"api/payment/fail"); // 결제 실패 시 이동할 URL

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
    public KakaoPayCancelRes cancelKakaoPay(Long serviceId){
        KakaoPayCancelRes res = new KakaoPayCancelRes();
        return res;
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
        CompletedDto dto = new CompletedDto();
        dto.setServiceId(serviceId);

//        int res = serviceMapper.payOrDoneCompleted(dto);
//        if(res==0){
//            throw new RuntimeException();
//        }
        serviceRepository.paidCompleted(serviceId);

        // 카톡 메세지 보냄
        RestTemplate restTemplate = new RestTemplate();
        KakaoPayRes kakaoPayRes = restTemplate.postForObject(
                "https://open-api.kakaopay.com/online/v1/payment/approve",
                requestEntity,KakaoPayRes.class);
        log.info("kakaoPayRedayRes: {}", kakaoPayRes);

        String redirectUrl = String.format("%spaySuccess", domain.getServer());
        return new RedirectView(redirectUrl);
        // 여기 만나서 바로 이동하는식
    }
}
