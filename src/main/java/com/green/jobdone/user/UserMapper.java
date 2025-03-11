package com.green.jobdone.user;

import com.green.jobdone.user.model.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Optional;

@Mapper
public interface UserMapper {
    int postUserSignUp(UserSignUpReq p);
    String checkEmailExists(String email);
    UserSignInResDto postUserSignIn(String email);
    UserSignUpEmailCheckRes postUserEmailCheck(String email);
    UserInfoGetRes getUserInfo(long userId);
    int updateUserInfo(UserInfoPatchReq p);
    String selectInfoPwUser(long userId);
    int deleteUser(UserInfoDelReq p);
    int updatePassword(UserPwPatchReq p);
    int updatePasswordThEmail(UserPwPatchReq p);
//    List<UserUuidDto> getUuidCheck();
}
