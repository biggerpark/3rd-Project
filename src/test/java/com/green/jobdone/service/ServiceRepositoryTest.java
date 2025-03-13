package com.green.jobdone.service;

import com.green.jobdone.common.JpaAuditingConfiguration;
import com.green.jobdone.entity.Product;
import com.green.jobdone.entity.Service;
import com.green.jobdone.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ActiveProfiles("test") //test yaml을 사용
@DataJpaTest
@Transactional
@Import(JpaAuditingConfiguration.class) // created_at, updated_at 현재시간을 넣기 위해선 auditing 써야 됨
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ServiceRepositoryTest {
    @Autowired
    ServiceRepository serviceRepository;
    static final Long userId_1 = 1L;
    static final Long productId_1 =1L;
    static final Long serviceId_1 =1L;

    Service service = new Service();

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
    void insertServiceTest(){
        serviceRepository.save(service);

        Service insService = serviceRepository.findById(service.getServiceId()).orElse(null);

        assertNotNull(insService);
        assertEquals("테스트", insService.getAddress());
    }
    @Test
    void updateServiceTest(){
        Service updService = serviceRepository.findById(serviceId_1).orElse(null);
        updService.setAddress("서비스업데이트테스트");
        serviceRepository.save(service);
        assertEquals("서비스업데이트테스트", updService.getAddress());
    }
    @Test
    void delServiceTest(){
        List<Service> beforeDelServices = serviceRepository.findAll();
        serviceRepository.deleteById(serviceId_1);
        List<Service> afterDelServices = serviceRepository.findAll();
        assertEquals(beforeDelServices.size()+1, afterDelServices.size());
    }
}
