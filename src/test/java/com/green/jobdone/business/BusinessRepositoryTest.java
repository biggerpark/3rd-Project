package com.green.jobdone.business;

import com.green.jobdone.common.JpaAuditingConfiguration;
import com.green.jobdone.entity.Business;
import com.green.jobdone.entity.DetailType;
import com.green.jobdone.entity.User;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@DataJpaTest
@Transactional
@Import(JpaAuditingConfiguration.class) // 현재시간을 넣기위함
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class BusinessRepositoryTest {

        /*
    given - 준비단계
    when - 실행
    then - 단언(검증)
     */

    @Mock
    BusinessRepository businessRepository;
    static final Long userId_1 = 1L;
    static final Long detailTypeId_1 = 1L;

    Business business = new Business();

    @BeforeEach
    void initData() {
        User user = new User();
        user.setUserId(userId_1);
        DetailType detailType = new DetailType();
        detailType.setDetailTypeId(detailTypeId_1);
        business.setUser(user);
        business.setDetailType(detailType);
        business.setBusinessName("business_test");
        business.setBusiCreatedAt(LocalDate.now());
        business.setBusinessNum("1234567890"); //10자리 한정
        business.setAddress("대구시 여러분 담배꽁초");
        business.setTel("12345678900");
    }

    @Test
    void insBusiness() {
        businessRepository.save(business);

        Business insertBusiness = businessRepository.findById(business.getBusinessId()).orElse(null);

        assertNotNull(insertBusiness);
        assertEquals(business.getBusinessId(), insertBusiness.getBusinessId());
        System.out.println("businessId:" + business.getBusinessId());

    }


    @Test
    void getCoordinatesFromAddress() {
    }

    @Test
    void patchBusinessThumbnail() {
    }

    @Test
    void patchBusinessLogo() {
    }

    @Test
    void patchBusinessPaper() {
    }

    @Test
    void postBusinessContents() {
    }

    @Test
    void businessPicTemp() {
    }

    @Test
    void businessPicConfirm() {
    }

    @Test
    void udtBusiness() {
    }

    @Test
    void udtBusinessState() {
    }

    @Test
    void setBusinessThumbnail() {
    }

    @Test
    void delBusinessPic() {
    }

    @Test
    void getBusinessList() {
    }

    @Test
    void getBusinessListMap() {
    }

    @Test
    void getBusinessOne() {
    }

    @Test
    void getBusinessOnePics() {
    }

    @Test
    void getBusinessMonthly() {
    }

    @Test
    void getBusinessRevenueByAdmins() {
    }

    @Test
    void getBusinessService() {
    }

    @Test
    void getBusinessServiceByAdmin() {
    }
}