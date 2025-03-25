package com.green.jobdone.service;

import com.green.jobdone.TestUtils;
import com.green.jobdone.common.JpaAuditingConfiguration;
import com.green.jobdone.entity.*;
import com.green.jobdone.product.OptionDetailRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test") //test yaml을 사용
@DataJpaTest
@Transactional
@Import(JpaAuditingConfiguration.class) // created_at, updated_at 현재시간을 넣기 위해선 auditing 써야 됨
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ServiceDBConnectTest {
    @Autowired ServiceRepository serviceRepository;
    static final Long userId_1 = 1L;
    static final Long serviceId_5 = 5L;
    static final Long productId_1 =1L;
    static final Long optionDetailId_1 =1L;
    static final Long optionDetailId_2 = 2L;
    Service service = new Service();
    ServiceDetail serviceDetail= new ServiceDetail();
    ServiceOption serviceOption= new ServiceOption();
    @Autowired
    private ServiceDetailRepository serviceDetailRepository;
    @Autowired
    private ServiceOptionRepository serviceOptionRepository;
    @Autowired
    private OptionDetailRepository optionDetailRepository;

    @BeforeEach
    void initData(){
        User user = new User();
        user.setUserId(userId_1);
        Product product = new Product();
        product.setProductId(productId_1);
        service.setAddress("테스트");
        service.setUser(user);
        service.setPrice(15000);
        service.setLat(23);
        service.setLng(127);
        service.setProduct(product);


    }
    @Test
    void insertService(){
        List<Service> serviceBefore = serviceRepository.findAll();
        List<ServiceDetail> serviceDetailBefore = serviceDetailRepository.findAll();
        List<ServiceOption> serviceOptionBefore = serviceOptionRepository.findAll();
//        Service beforeInsert = serviceRepository.findById(serviceId_5).orElse(null);

        serviceRepository.save(service);
        // 서비스 넣기

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        LocalDate startDate = LocalDate.parse("2025/03/12", dateFormatter);

        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        LocalTime mStartTime = LocalTime.parse("08:00:00", timeFormatter);
        serviceDetail.setStartDate(startDate);
        serviceDetail.setMStartTime(mStartTime);
        serviceDetail.setService(service);
        serviceDetailRepository.save(serviceDetail);
        //서비스 디테일 넣기

        serviceOption.setService(service);
        OptionDetail optionDetail = optionDetailRepository.findById(optionDetailId_1).orElse(null);
        serviceOption.setOptionDetail(optionDetail);
        serviceOption.setComment("테스트");
        serviceOptionRepository.save(serviceOption);
        // 1개 넣는건 테스트 하기 쉬움 2개는??

        Service afterInsert = serviceRepository.findById(service.getServiceId()).orElse(null);
        List<Service> afterInsertServices = serviceRepository.findAll();
        ServiceDetail afterInsertServiceDetail = serviceDetailRepository.findById(serviceDetail.getDetailId()).orElse(null);
        List<ServiceDetail> afterInsertServicesDetail = serviceDetailRepository.findAll();
        ServiceOption afterInsertServiceOption = serviceOptionRepository.findById(serviceOption.getServiceOptionId()).orElse(null);
        List<ServiceOption> serviceOptionAfter = serviceOptionRepository.findAll();


        assertAll(
                () -> TestUtils.assertCurrentTimestamp(afterInsert.getCreatedAt())
                ,() ->assertEquals(serviceBefore.size()+1, afterInsertServices.size())
                ,() ->assertEquals(serviceDetailBefore.size()+1, afterInsertServicesDetail.size())
                ,() ->assertEquals(serviceOptionBefore.size()+1, serviceOptionAfter.size())
//                ,() ->assertNull(beforeInsert)
                ,() ->assertNotNull(afterInsert)
                ,() ->assertNotNull(afterInsertServiceDetail)
                ,() ->assertNotNull(afterInsertServiceOption)
                ,() ->assertEquals(service.getServiceId(),afterInsert.getServiceId())

        );

    }
}
