package com.green.jobdone.admin;

import com.green.jobdone.admin.model.BusinessApplicationGetRes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdminService {
    private final AdminRepository adminRepository;
    private final AdminMapper adminMapper;


    public List<BusinessApplicationGetRes> getBusinessApplication(int page) {

        return adminMapper.getBusinessApplication(page);


    }


}
