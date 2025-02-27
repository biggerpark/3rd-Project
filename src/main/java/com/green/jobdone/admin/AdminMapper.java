package com.green.jobdone.admin;

import com.green.jobdone.admin.model.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AdminMapper {
    List<BusinessApplicationGetRes> getBusinessApplication(int offset);
    List<BusinessCategoryRes> getBusinessCategory(long categoryId);
    List<AdminUserInfoRes> getAdminUserInfo(int offset);
    AdminSalesInfoDto getSalesInfo(String month, int lastDay);
    AdminCategoryInfoDto getCategoryInfo();
    AdminPaidInfoDto getPaidCount(String beforeMonth, String targetMonth, int lastDay, String today);
    AdminRatingInfoRes getRatingAverage();
    int getUserIncrease(String targetMonth, String today);
    List<AdminNewBusinessInfoRes> getNewBusiness();
    AdminDashBoardNewServiceDto getNewServiceCount(String today, String yesterday);
}
