package com.green.jobdone.admin;

import com.green.jobdone.admin.model.*;
import com.green.jobdone.business.BusinessRepository;
import com.green.jobdone.category.CategoryRepository;
import com.green.jobdone.category.DetailTypeRepository;
import com.green.jobdone.common.PicUrlMaker;
import com.green.jobdone.entity.Business;
import com.green.jobdone.service.ServiceRepository;
import com.green.jobdone.visitor.VisitorCountRepository;
import com.green.jobdone.visitor.VisitorHistoryRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdminService {
    private final AdminRepository adminRepository;
    private final AdminMapper adminMapper;
    private final BusinessRepository businessRepository;
    private final ServiceRepository serviceRepository;
    private final VisitorHistoryRepository visitorHistoryRepository;
    private final VisitorCountRepository visitorCountRepository;
    private final CategoryRepository categoryRepository;
    private final DetailTypeRepository detailTypeRepository;


    public List<BusinessApplicationGetRes> getBusinessApplication(int page) {

        int offset = (page - 1) * 10;

       List<BusinessApplicationGetRes> res=adminMapper.getBusinessApplication(offset);


       for(BusinessApplicationGetRes res1:res){
           String paperUrl=PicUrlMaker.makePicUrlPaper(res1.getBusinessId(),res1.getPaper());
           res1.setPaper(paperUrl);
       }

       return res;
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


    @Transactional // 업체승인 하면 user type 사장으로 바뀌고, business state 도 바뀜
    public int postBusinessApprove(long businessId) {
        Business business = businessRepository.findById(businessId) // 프론트에서 받은 해당 pk를 통해 업체 정보를 entity 에 담음
                .orElseThrow(() -> new EntityNotFoundException("해당 업체를 찾을 수 없습니다."));


        LocalDate today = LocalDate.now();




        business.setApproveAt(today);
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

    public List<AdminSalesInfoRes> getAdminSalesInfo() {
        YearMonth targetMonth = YearMonth.from(LocalDate.now());
        List<AdminSalesInfoRes> result = new ArrayList<>();
        for (int i = 5; i >= 0; i--) {
            AdminSalesInfoRes res = new AdminSalesInfoRes();
            YearMonth month = targetMonth.minusMonths(i);
            //lastDays.add(month.lengthOfMonth());
            res.setMonth(month.toString());
            List<AdminSalesInfoDto> adminSalesInfoDtos = adminMapper.getSalesInfo(month.toString(), month.lengthOfMonth());
            res.setSalesInfoDtos(adminSalesInfoDtos);
            int totalPrice = 0;
            for (AdminSalesInfoDto item : adminSalesInfoDtos) {
                totalPrice += item.getTotalPrice();
            }
            res.setTotalPrice(totalPrice);
            result.add(res);
        }
        return result;
    }

    public List<AdminVisitorInfoRes> getAdminVisitorInfo() {
        LocalDate today = LocalDate.now();
        List<AdminVisitorInfoRes> result = new ArrayList<>();
        for (int i = 6; i >= 1; i--) {
            AdminVisitorInfoRes res = new AdminVisitorInfoRes();
            LocalDate date = today.minusDays(i);
            DayOfWeek dayOfWeek = date.getDayOfWeek();
            String koreanDay = dayOfWeek.getDisplayName(TextStyle.FULL, Locale.KOREAN);
            res.setDate(date.toString());
            res.setDateOfWeek(koreanDay);
            res.setVisitorCount(visitorHistoryRepository.getVisitorCountByDate(date));
            result.add(res);
        }
        AdminVisitorInfoRes res = new AdminVisitorInfoRes();
        res.setDate(today.toString());
        res.setDateOfWeek(today.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.KOREAN));
        res.setVisitorCount(visitorCountRepository.findById(1L).orElse(null).getCount());
        result.add(res);
        return result;
    }

//    public AdminCategoryInfoDto getAdminCategoryInfo() {
//        AdminCategoryInfoDto res = adminMapper.getCategoryInfo();
//
//        // 카테고리별 비율 계산 (소수점 2자리 유지)
//        res.setCleaningPercent(roundToTwoDecimal((res.getCleaningCount() / (double) res.getTotalCount()) * 100));
//        res.setMovingPercent(roundToTwoDecimal((res.getMovingCount() / (double) res.getTotalCount()) * 100));
//        res.setCarWashPercent(roundToTwoDecimal((res.getCarWashCount() / (double) res.getTotalCount()) * 100));
//
//        // 청소 서비스 상세 비율
//        res.setCleaningPercent1(roundToTwoDecimal((res.getCleaningCount1() / (double) res.getCleaningCount()) * 100));
//        res.setCleaningPercent2(roundToTwoDecimal((res.getCleaningCount2() / (double) res.getCleaningCount()) * 100));
//        res.setCleaningPercent3(roundToTwoDecimal((res.getCleaningCount3() / (double) res.getCleaningCount()) * 100));
//
//        // 이사 서비스 상세 비율
//        res.setMovingPercent4(roundToTwoDecimal((res.getMovingCount4() / (double) res.getMovingCount()) * 100));
//        res.setMovingPercent5(roundToTwoDecimal((res.getMovingCount5() / (double) res.getMovingCount()) * 100));
//
//        // 세차 서비스 상세 비율
//        res.setCarWashPercent6(roundToTwoDecimal((res.getCarWashCount6() / (double) res.getCarWashCount()) * 100));
//        res.setCarWashPercent7(roundToTwoDecimal((res.getCarWashCount7() / (double) res.getCarWashCount()) * 100));
//
//        return res;
//    }

//    public List<AdminCategoryInfoRes> getAdminCategoryInfo2() {
//        List<AdminCategoryInfoDto> categorys = adminMapper.getCategoryInfo();
//        List<AdminCategoryInfoRes> res = new ArrayList<>();
//        int totalCount = 0;
//        for(AdminCategoryInfoDto item : categorys) {
//            totalCount += item.get();
//        }
//        for(AdminCategoryInfoDto item : categorys) {
//            AdminCategoryInfoRes categoryInfoDto = new AdminCategoryInfoRes();
//            categoryInfoDto.setCategoryId(item.getCategoryId());
//            categoryInfoDto.setCategoryName(item.getCategoryName());
//            categoryInfoDto.setCategoryCount(item.getCategoryCount());
//            categoryInfoDto.setCategoryPercent(item.getCategoryCount()/(double)totalCount);
//            List<DetailType> detailTypes = detailTypeRepository.findByCategoryId(item.getCategoryId());
//            List<AdminDetailTypeInfoDto> dto = new ArrayList<>();
//            for (DetailType item2 : detailTypes) {
//                AdminDetailTypeInfoDto detailTypeInfoDto = new AdminDetailTypeInfoDto();
//                detailTypeInfoDto.setDetailTypeId(item2.getDetailTypeId());
//                detailTypeInfoDto.setDetailTypeName(item2.getDetailTypeName());
//                dto.add(detailTypeInfoDto);
//            }
//            categoryInfoDto.setDto(dto);
//            res.add(categoryInfoDto);
//        }
//        return res;
//    }

    public List<AdminCategoryInfoDto> getAdminCategoryInfo3() {
        List<AdminCategoryInfoDto> res = adminMapper.getCategoryInfo();
        log.info("res.size : {}", res.size());

        int totalcount = 0;
        for (AdminCategoryInfoDto item : res) {
            for(AdminDetailTypeInfoDto item2 : item.getDetailTypeCounts()){
                totalcount += item2.getCount();
            }
        }
        log.info("totalcount : {}", totalcount);

        for (AdminCategoryInfoDto item : res) {
            int categorycount = 0;
            for(AdminDetailTypeInfoDto item2 : item.getDetailTypeCounts()){
                categorycount += item2.getCount();
            }
            for(AdminDetailTypeInfoDto item2 : item.getDetailTypeCounts()){
                item2.setDetailTypePercent(roundToTwoDecimal((item2.getCount()/(double)categorycount) * 100));
            }
            item.setCategoryCount(categorycount);
            item.setCategoryPercent(roundToTwoDecimal((categorycount/(double)totalcount) * 100));
        }
        return res;
    }

    // 🔹 소수점 2자리까지 반올림하는 메서드
    private double roundToTwoDecimal(double value) {
        return Math.round(value * 100) / 100.0;
    }

    public AdminMainStatsRes getAdminMainStatsInfo() {
        LocalDate today = LocalDate.now();
        YearMonth targetMonth = YearMonth.from(LocalDate.now());
        YearMonth beforeMonth = targetMonth.minusMonths(1);
        AdminMainStatsRes res = new AdminMainStatsRes();
        int lastDay = beforeMonth.lengthOfMonth();
        AdminPaidInfoDto paidInfoDto = adminMapper.getPaidCount(beforeMonth.toString(), targetMonth.toString(), lastDay, today.toString(), today.getDayOfMonth());
        double pastPaid = paidInfoDto.getPastPaidCount();
        double nowPaid = paidInfoDto.getNowPaidCount();
        res.setGrowthRate(roundToTwoDecimal(((nowPaid - pastPaid) / (double)pastPaid) * 100 ) );
        List<AdminRatingInfoDto> adminRatingInfoDtos = adminMapper.getRatingAverage();
        List<AdminRatingInfoRes> adminRatingInfoRes = new ArrayList<>();
        res.setTotalAvg(roundToTwoDecimal(adminRatingInfoDtos.get(0).getTotalAvgScore()));
        for (AdminRatingInfoDto item : adminRatingInfoDtos) {
            AdminRatingInfoRes result = new AdminRatingInfoRes();
            result.setCategoryName(item.getCategoryName());
            result.setAvgScore(roundToTwoDecimal(item.getAvgScore()));
            adminRatingInfoRes.add(result);
        }
        res.setRatingInfoRes(adminRatingInfoRes);
        res.setCompeletedServiceCount(serviceRepository.getTotalCompletedInfo());
        res.setNewCustomerCount(adminMapper.getUserIncrease(targetMonth.toString(), today.toString()));
        return res;
    }

    public List<AdminNewBusinessInfoRes> getAdminNewBusinessInfo() {
        return adminMapper.getNewBusiness();
    }

    public AdminDashBoardInfoRes getAdminDashBoardInfo(){
        AdminDashBoardInfoRes res = new AdminDashBoardInfoRes();
        LocalDate today = LocalDate.now();
        AdminDashBoardNewServiceDto newServiceDto = adminMapper.getNewServiceCount(today.toString(), today.minusDays(1).toString());
        int todayServiceCount = newServiceDto.getTodayServiceCount();
        int yesterdayServiceCount = newServiceDto.getYesterdayServiceCount();
        res.setNewServiceCount(todayServiceCount);
        res.setNewServicePercent(roundToTwoDecimal(((todayServiceCount - yesterdayServiceCount) / (double)yesterdayServiceCount) * 100 ));
        AdminNewUserInfoDto newUserInfoDto = adminMapper.getNewUserCount(today.toString(), today.minusDays(1).toString());
        int todayUserCount = newUserInfoDto.getTodayNewUserCount();
        int yesterdayUserCount = newUserInfoDto.getYesterdayNewUserCount();
        res.setNewUserCount(todayUserCount);
        res.setNewUserPercent(roundToTwoDecimal((todayUserCount - yesterdayUserCount) / (double)yesterdayUserCount) * 100);
        AdminNewBusinessInfoDto newBusinessInfoDto = adminMapper.getNewBusinessCount(today.toString(), today.minusDays(1).toString());
        int todayBusinessCount = newBusinessInfoDto.getTodayNewBusinessCount();
        int yesterdayBusinessCount = newBusinessInfoDto.getYesterdayNewBusinessCount();
        res.setNewBusinessCount(todayBusinessCount);
        res.setNewBusinessCountThenYesterday(todayBusinessCount - yesterdayBusinessCount);
        res.setUnprocessedInquiries(15);
        return res;
    }















}
