package com.green.jobdone.admin;

import com.green.jobdone.admin.model.*;
import com.green.jobdone.business.BusinessRepository;
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


    @Transactional
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
            AdminSalesInfoDto adminSalesInfoDtos = adminMapper.getSalesInfo(month.toString(), month.lengthOfMonth());
            res.setTotalSales((adminSalesInfoDtos.getTotalSales()*5)/100);
            res.setCleaningSales((adminSalesInfoDtos.getCleaningSales()*5)/100);
            res.setMovingSales((adminSalesInfoDtos.getMovingSales()*5)/100);
            res.setCarWashSales((adminSalesInfoDtos.getCarWashSales()*5)/100);
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

    public AdminCategoryInfoDto getAdminCategoryInfo() {
        AdminCategoryInfoDto res = adminMapper.getCategoryInfo();

        // 카테고리별 비율 계산 (소수점 2자리 유지)
        res.setCleaningPercent(roundToTwoDecimal((res.getCleaningCount() / (double) res.getTotalCount()) * 100));
        res.setMovingPercent(roundToTwoDecimal((res.getMovingCount() / (double) res.getTotalCount()) * 100));
        res.setCarWashPercent(roundToTwoDecimal((res.getCarWashCount() / (double) res.getTotalCount()) * 100));

        // 청소 서비스 상세 비율
        res.setCleaningPercent1(roundToTwoDecimal((res.getCleaningCount1() / (double) res.getCleaningCount()) * 100));
        res.setCleaningPercent2(roundToTwoDecimal((res.getCleaningCount2() / (double) res.getCleaningCount()) * 100));
        res.setCleaningPercent3(roundToTwoDecimal((res.getCleaningCount3() / (double) res.getCleaningCount()) * 100));

        // 이사 서비스 상세 비율
        res.setMovingPercent4(roundToTwoDecimal((res.getMovingCount4() / (double) res.getMovingCount()) * 100));
        res.setMovingPercent5(roundToTwoDecimal((res.getMovingCount5() / (double) res.getMovingCount()) * 100));

        // 세차 서비스 상세 비율
        res.setCarWashPercent6(roundToTwoDecimal((res.getCarWashCount6() / (double) res.getCarWashCount()) * 100));
        res.setCarWashPercent7(roundToTwoDecimal((res.getCarWashCount7() / (double) res.getCarWashCount()) * 100));

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

        AdminPaidInfoDto paidInfoDto = adminMapper.getPaidCount(beforeMonth.toString(), targetMonth.toString(), lastDay, today.toString());
        int pastPaid = paidInfoDto.getPastPaidCount();
        int nowPaid = paidInfoDto.getNowPaidCount();
        res.setGrowthRate(roundToTwoDecimal(((nowPaid - pastPaid) / (double)pastPaid) * 100 ) );
        AdminRatingInfoRes adminRatingInfoRes = adminMapper.getRatingAverage();
        res.setAverageRating(roundToTwoDecimal(adminRatingInfoRes.getTotalAvgScore()));
        res.setCompeletedServiceCount(serviceRepository.getTotalCompletedInfo());
        res.setCarWashAverageRating(roundToTwoDecimal(adminRatingInfoRes.getAvgScoreCarWash()));
        res.setMovingAverageRating(roundToTwoDecimal(adminRatingInfoRes.getAvgScoreMoving()));
        res.setCleaningAverageRating(roundToTwoDecimal(adminRatingInfoRes.getAvgScoreCleaning()));
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
        res.setNewBusinessCount(12);
        res.setNewUserCount(21);
        res.setNewUserPercent(12.5);
        res.setUnprocessedInquiries(15);
        return res;
    }














}
