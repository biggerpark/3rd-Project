package com.green.jobdone.business;

import com.green.jobdone.business.model.BusinessContentsPostReq;
import com.green.jobdone.business.model.BusinessPostSignUpReq;
import com.green.jobdone.business.model.get.BusinessGetRes;
import com.green.jobdone.entity.Business;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

import java.math.BigDecimal;
import java.net.BindException;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

public class BusinessServiceTestForPost extends BusinessServiceParentTest{

    @Test
    public void test() {
        given(authenticationFacade.getSignedUserId())
                .willReturn(SIGNED_USER_ID);
        // 가상의 logo 파일 생성
        MockMultipartFile mockLogo = new MockMultipartFile(
                "logo",                      // 필드명
                "test-logo.png",             // 파일명
                "image/png",                 // 파일 타입
                "로고 파일의 내용".getBytes() // 파일 데이터
        );

        // 가상의 paper 파일 생성
        MockMultipartFile mockPaper = new MockMultipartFile(
                "paper",
                "business-paper.pdf",
                "application/pdf",
                "서류 파일 내용".getBytes()
        );

        BusinessPostSignUpReq req = BusinessPostSignUpReq.builder()
                .signedUserId(SIGNED_USER_ID)
                .detailTypeId(2L)
                .businessName("test")
                .businessNum("1234567890")
                .busiCreatedAt(String.valueOf(LocalDate.now()))
                .tel("0104567894")
                .lat(new BigDecimal("65.22"))
                .lng(new BigDecimal("165.22"))
                .build();

        Business business = new Business();
        business.setBusinessId(1L);

        when(businessRepository.save(any(Business.class))).thenReturn(business);
        long businessId = businessService.insBusiness(mockPaper, mockLogo, req);

        assertThat(businessId).isGreaterThan(0L);
    }
}
