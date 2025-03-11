package com.green.jobdone.business;

import com.green.jobdone.common.FileStorageService;
import com.green.jobdone.entity.Business;
import com.green.jobdone.entity.User;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class BusinessServiceTest {

    @Mock
    private BusinessRepository businessRepository;

    @Mock
    private FileStorageService fileStorageService;

    @InjectMocks
    private BusinessService businessService;

    @Test
    void insBusiness() {
        //given
        User user = new User();
        Long userId = 1L;
        user.setUserId(userId);
        Long detailTypeId = 2L;

        Business business1 = Business.builder()
                .user(user)
                .businessName("Business 1")
                .state(100)
                .busiCreatedAt(LocalDate.now())
                .build();

    }

    @Test
    void patchBusinessLogo() {
    }
}