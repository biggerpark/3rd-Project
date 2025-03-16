package com.green.jobdone.business;

import com.green.jobdone.business.model.BusinessPostSignUpReq;
import com.green.jobdone.category.DetailTypeRepository;
import com.green.jobdone.common.MyFileUtils;
import com.green.jobdone.config.security.AuthenticationFacade;

import com.green.jobdone.entity.Business;
import com.green.jobdone.entity.DetailType;
import com.green.jobdone.entity.User;
import com.green.jobdone.user.UserRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BusinessServiceTest {


    @InjectMocks
    private BusinessService businessService;
    @Mock
    private BusinessRepository businessRepository;
    @Mock
    private MyFileUtils myFileUtils;
    @Mock
    private AuthenticationFacade authenticationFacade;
    @Mock
    private UserRepository userRepository;
    @Mock
    private DetailTypeRepository detailTypeRepository;

    @Mock
    private RestTemplate restTemplate;

    @Test
    void insBusiness_success() {
        MockMultipartFile paper = new MockMultipartFile(
                "paper", "paper.pdf", "application/pdf", "사업자등록증".getBytes()
        );
        MockMultipartFile logo = new MockMultipartFile(
                "logo", "logo.jpg", "image/jpeg", "로고이미지".getBytes()
        );

        //유저 인증
        Long SIGNED_USER_ID = 1L;
        given(authenticationFacade.getSignedUserId()).willReturn(SIGNED_USER_ID);

        //요청 객체 생성
        BusinessPostSignUpReq givenParam = new BusinessPostSignUpReq();
        givenParam.setSignedUserId(SIGNED_USER_ID);
        givenParam.setBusinessNum("1234567890");
        givenParam.setBusinessName("test");
        givenParam.setAddress("대구 서구 평리로 327 ");
        givenParam.setDetailTypeId(1L);
        givenParam.setBusiCreatedAt(String.format("%s",LocalDate.now()));
        givenParam.setTel("01012345678");

        User mockUser = new User();
        mockUser.setUserId(SIGNED_USER_ID);
        DetailType mockDetailType = new DetailType();
        mockDetailType.setDetailTypeId(givenParam.getDetailTypeId());

        // 저장될 Business 객체 Mock 설정
        Business savedBusiness = new Business();
        savedBusiness.setBusinessName(givenParam.getBusinessName());
        savedBusiness.setAddress(givenParam.getAddress());
        savedBusiness.setBusinessNum(givenParam.getBusinessNum());
        savedBusiness.setTel(givenParam.getTel());

        given(userRepository.findById(SIGNED_USER_ID)).willReturn(Optional.of(mockUser));
        given(detailTypeRepository.findById(givenParam.getDetailTypeId())).willReturn(Optional.of(mockDetailType));
        given(businessRepository.save(any(Business.class))).willReturn(savedBusiness);

        // kakao api mock
        String apiResponse = "{ \"documents\": [ { \"x\": 128.559148 \"y\": 35.864541 } ] }";
        ResponseEntity<String> mockResponse = ResponseEntity.ok(apiResponse);
        given(restTemplate.exchange(anyString(), eq(org.springframework.http.HttpMethod.GET), any(), eq(String.class)))
                .willReturn(mockResponse);


        //메서드 실행
        long businessId = businessService.insBusiness(paper,logo,givenParam);
        assertNotNull(businessId);
        assertEquals(savedBusiness.getBusinessId(), businessId);
    }


    @Test
    void getCoordinatesFromAddress() {
        // 테스트용 가짜 API 키
        String apikey = "fijsdoidfjo111";

        String address = "대구 서구 평리로 327 1층";
        String url = "https://dapi.kakao.com/v2/local/search/address.json?query=" + address;

        // Authorization 헤더에 KakaoAK {apikey} 설정
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "KakaoAK " + apikey);  // 가짜 API 키 사용

        // HttpEntity 객체 생성 (헤더 포함)
        HttpEntity<String> entity = new HttpEntity<>(headers);

        // Mock API 응답 설정
        String apiResponse = "{ \"documents\": [ { \"x\": 127.123456, \"y\": 35.123456 } ] }";
        ResponseEntity<String> mockResponse = ResponseEntity.ok(apiResponse);

        // restTemplate.exchange() Mock 설정
        given(restTemplate.exchange(eq(url), eq(HttpMethod.GET), eq(entity), eq(String.class)))
                .willReturn(mockResponse);

        // 비즈니스 서비스에서 API 호출
        double[] coordinates = businessService.getCoordinatesFromAddress(address);

        // 결과 검증
        assertEquals(127.123456, coordinates[0], 0.000001);
        assertEquals(35.123456, coordinates[1], 0.000001);
    }

}