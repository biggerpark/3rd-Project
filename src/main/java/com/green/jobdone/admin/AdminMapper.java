package com.green.jobdone.admin;

import com.green.jobdone.admin.model.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AdminMapper {
    List<BusinessApplicationGetRes> getBusinessApplication(int offset);
    List<BusinessCategoryRes> getBusinessCategory(long categoryId);
    List<AdminUserInfoRes> getAdminUserInfo(int offset);
    List<AdminSalesInfoDto> getSalesInfo(String month, int lastDay);
    List<AdminCategoryInfoDto> getCategoryInfo();
    AdminPaidInfoDto getPaidCount(String beforeMonth, String targetMonth, int lastDay, String today, int dayOfMonth);
    List<AdminRatingInfoDto> getRatingAverage();
    int getUserIncrease(String targetMonth, String today);
    List<AdminNewBusinessInfoRes> getNewBusiness();
    AdminDashBoardNewServiceDto getNewServiceCount(String today, String yesterday);
    AdminNewUserInfoDto getNewUserCount(String today, String yesterday);
    AdminNewBusinessInfoDto getNewBusinessCount(String today, String yesterday);
    AdminUnprocessedInquiriesInfoDto getUnprocessedInquiries(String today);
    AdminDto getAdminInfo(String Aid);
}
