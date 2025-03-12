package com.green.jobdone.business;

import com.green.jobdone.business.model.BusinessPostSignUpReq;
import com.green.jobdone.category.DetailTypeRepository;
import com.green.jobdone.common.FileStorageService;
import com.green.jobdone.common.MyFileUtils;
import com.green.jobdone.config.security.AuthenticationFacade;
import com.green.jobdone.entity.Business;
import com.green.jobdone.entity.DetailType;
import com.green.jobdone.entity.User;
import com.green.jobdone.user.UserRepository;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static  org.mockito.Mockito.when;
import static org.skyscreamer.jsonassert.JSONAssert.assertEquals;

import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Optional;

@MockitoSettings(strictness = Strictness.LENIENT)
class BusinessServiceInsertTest {

    @Mock
    private BusinessRepository businessRepository;

    @Mock
    private FileStorageService fileStorageService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private DetailTypeRepository detailTypeRepository;

    @Mock
    private MyFileUtils myFileUtils;

    @Mock
    private AuthenticationFacade authenticationFacade;

    @InjectMocks
    private BusinessService businessService;

    private BusinessPostSignUpReq givenParam;
    private MockMultipartFile paper;
    private MockMultipartFile logo;
    private User user;
    private DetailType detailType;


@Test
    void setUp() {
        user = new User();
        user.setUserId(1L);

        detailType = new DetailType();
        detailType.setDetailTypeId(2L);

        givenParam = BusinessPostSignUpReq.builder()
                .signedUserId(1L)
                .detailTypeId(2L)
                .businessName("")
                .businessNum("012434565789")
                .address("address")
                .tel("01022225555")
                .busiCreatedAt("2022/05/05")
                .build();

        paper = new MockMultipartFile("paper", "paper.jpg", "image/jpeg", "paper content".getBytes());
        logo = new MockMultipartFile("logo", "logo.jpg", "image/jpeg", "logo content".getBytes());

        // Mocking behavior
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(detailTypeRepository.findById(2L)).thenReturn(Optional.of(detailType));
        when(businessRepository.findExistBusinessNum(givenParam.getBusinessNum())).thenReturn(0);
        when(businessRepository.save(any(Business.class))).thenAnswer(invocation -> {
            Business business = invocation.getArgument(0);
            business.setBusinessId(1L); // ID 자동 증가 시뮬레이션
            return business;
        });
    }

    @Test
    void insBusiness() {
        // when & then: Expect ConstraintViolationException to be thrown
        assertThrows(ConstraintViolationException.class, () -> {
            businessService.insBusiness(paper, logo, givenParam);
        });
    }
    @Test
    void insBusiness_shouldNotThrowException_whenValidRequest() {
        // given
        BusinessPostSignUpReq validParam = BusinessPostSignUpReq.builder()
                .businessName("Valid Business Name")
                .businessNum("0123456789") // 유효한 값
                .address("Valid Address")
                .tel("01022225555")
                .busiCreatedAt("2022/05/05")
                .build();

        // when, then
        assertDoesNotThrow(() -> {
            businessService.insBusiness(paper, logo, validParam);
        });
    }

}