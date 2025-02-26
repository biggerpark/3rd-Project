package com.green.jobdone.admin;

import com.green.jobdone.admin.model.AdminUserInfoRes;
import com.green.jobdone.admin.model.BusinessApplicationGetRes;
import com.green.jobdone.admin.model.BusinessCategoryRes;
import com.green.jobdone.admin.model.BusinessRejectReq;
import com.green.jobdone.business.BusinessRepository;
import com.green.jobdone.entity.Business;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdminService {
    private final AdminRepository adminRepository;
    private final AdminMapper adminMapper;
    private final BusinessRepository businessRepository;


    public List<BusinessApplicationGetRes> getBusinessApplication(int page) {

        int offset = (page - 1) * 10;

        return adminMapper.getBusinessApplication(offset);

    }

    @Transactional
    public int postBusinessReject(BusinessRejectReq p){
//        Optional<Business> business = businessRepository.findById(p.getBusinessId());
        Business business = businessRepository.findById(p.getBusinessId()) // 프론트에서 받은 해당 pk를 통해 업체 정보를 entity 에 담음
                .orElseThrow(() -> new EntityNotFoundException("해당 업체를 찾을 수 없습니다."));


        business.setRejectContents(p.getRejectContents());
        business.setState(120); // 업체 상태를 수락취소로 바꿈

//        businessRepository.save(business); Transactional 애노테이션이 있으면 save 를 할 필요가 없다.


        return 1;
    }


    @Transactional
    public int postBusinessApprove(long businessId) {
        Business business = businessRepository.findById(businessId) // 프론트에서 받은 해당 pk를 통해 업체 정보를 entity 에 담음
                .orElseThrow(() -> new EntityNotFoundException("해당 업체를 찾을 수 없습니다."));



        business.setState(101); // 업체 상태를 수락완료로 바꿈


        return 1;
    }


    @Transactional
    public List<BusinessCategoryRes> getBusinessCategory(long categoryId) {


        return adminMapper.getBusinessCategory(categoryId);

    }


    @Transactional
    public List<AdminUserInfoRes> getAdminUserInfo(int page){
        int offset = (page - 1) * 10;

        List<AdminUserInfoRes> res = adminMapper.getAdminUserInfo(offset);

        for(AdminUserInfoRes item : res){
            String type=switch(item.getTypeName()){
                case 100 -> "일반유저";
                case 110 -> "업체 직원";
                case 120 -> "업체 매니저";
                case 130 -> "업체 사장";
                case 140 -> "프리랜서";
                default -> null;
            };

            item.setType(type);
        }

        return res;

    }










}
