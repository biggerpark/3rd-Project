package com.green.jobdone.user;

import com.green.jobdone.common.CookieUtils;
import com.green.jobdone.common.MyFileUtils;
import com.green.jobdone.common.PicUrlMaker;
import com.green.jobdone.common.exception.CustomException;
import com.green.jobdone.common.exception.UserErrorCode;
import com.green.jobdone.config.jwt.JwtConst;
import com.green.jobdone.config.jwt.JwtUser;
import com.green.jobdone.config.jwt.TokenProvider;
import com.green.jobdone.config.jwt.UserRole;
import com.green.jobdone.config.security.AuthenticationFacade;
import com.green.jobdone.config.security.SignInProviderType;
import com.green.jobdone.entity.User;
import com.green.jobdone.mail.MailMapper;
import com.green.jobdone.user.model.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final MailMapper mailMapper;
    private final UserMapper mapper;
    private final MyFileUtils myFileUtils;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final CookieUtils cookieUtils;
    private final AuthenticationFacade authenticationFacade;
    private final JwtConst jwtConst;
    private final UserRepository userRepository;
    private final UserMapper userMapper;


    public int postUserSignUp(UserSignUpReq p, MultipartFile pic) {
//        String existsEmail = mapper.checkEmailExists(p.getEmail());

        User user = new User();
        UserDto existsEmail = userRepository.checkPostUser(p.getEmail());
        if(existsEmail!=null) {
            if(existsEmail.getRole().getCode()==999){
                user = userRepository.findById(existsEmail.getUserId()).orElseGet(User::new);
            } else {
                throw new IllegalArgumentException("이미 등록된 이메일입니다.");
            }
        }
        String img = String.format("img%d.jpg", (int)(Math.random()*4)+1);
        String savedPicName = (pic != null ? myFileUtils.makeRandomFileName(pic) : img);


        String hashedPassword = passwordEncoder.encode(p.getUpw());
        log.info("hashedPassword: {}", hashedPassword);

        user.setProviderType(SignInProviderType.LOCAL);
        user.setEmail(p.getEmail());
        user.setUpw(hashedPassword);
        user.setName(p.getName());
        user.setPic(savedPicName);
//        user.setUuid( UUID.randomUUID().toString().replace("-", "")); // UUID 설정
        user.setPhone(p.getPhone());

        user.setRole(UserRole.USER);

        //int result = mapper.insUser(p);
        userRepository.save(user);
//        p.setUpw(hashedPassword);
//        p.setPic(savedPicName);
//
//        int result = mapper.postUserSignUp(p);

        if (pic == null) {
            mailMapper.delAuthInfo(p.getEmail());
            return 1;
        }

        // 저장 위치 만든다.
        // middlePath = user/${userId}
        // filePath = user/${userId}/${savedPicName}
        long userId = user.getUserId(); //userId를 insert 후에 얻을 수 있다.
//        String uuid=user.getUuid();
        String middlePath = String.format("user/%d", userId);
        myFileUtils.makeFolders(middlePath);
        log.info("middlePath: {}", middlePath);
        String filePath = String.format("%s/%s", middlePath, savedPicName);
        try {
            myFileUtils.transferTo(pic, filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }

        mailMapper.delAuthInfo(p.getEmail());

        return 1;

    }

    public UserSignInRes postUserSignIn(UserSignInReq p, HttpServletResponse response) {



        UserSignInResDto res = mapper.postUserSignIn(p.getEmail()); // email 에 해당하는 유저 res 가져오기

        if(res==null){
            throw new CustomException(UserErrorCode.INCORRECT_ID_PW);
        }


        if (res.getPic() != null) {
            res.setPic(PicUrlMaker.makePicUserUrl(res.getUserId(), res.getPic()));
        }

        User user = userRepository.findByEmailAndProviderType(p.getEmail(), SignInProviderType.LOCAL);
        if(user == null || !passwordEncoder.matches(p.getUpw(), user.getUpw())) {
            throw new CustomException(UserErrorCode.INCORRECT_ID_PW);
        }

        /*
        JWT 토큰 생성 2개? AccessToken(20분), RefreshToken(15일)
         */

        JwtUser jwtUser = new JwtUser(user.getUserId(), res.getRoles());


        String accessToken = tokenProvider.generateAccessToken(jwtUser);
        String refreshToken = tokenProvider.generateRefreshToken(jwtUser);

        log.info("accessToken: {}", accessToken);
        log.info("refreshToken: {}", refreshToken);

        //refreshToken은 쿠키에 담는다.

        cookieUtils.setCookie(response, "refreshToken", refreshToken, jwtConst.getRefreshTokenCookieExpiry(), "/api/user/access-token");

        return UserSignInRes
                .builder()
                .email(res.getEmail())
                .type(res.getType())
                .userId(res.getUserId())
                .pic(res.getPic())
                .accessToken(accessToken)
                .name(res.getName())
                .phone(res.getPhone())
                .message("로그인 성공")
                .businessId(res.getBusinessId())
                .build();
    }

    public int postUserEmailCheck(String email) {
        UserDto res = userRepository.checkPostUser(email);
        if (res==null|| res.getRole().getCode()==999) {
            return 1;
        }

        return 0;

    }


    public UserInfoGetRes getUserInfo() {


        long userId = authenticationFacade.getSignedUserId();

        //나중에 최종적으로 주석 풀기

        UserInfoGetRes res = mapper.getUserInfo(userId);

        String profile = res.getPic().substring(0,3);
        String profile2 = "img";
        String profile3 = "htt";

        if(profile.equals(profile2)) {
            res.setPic(String.format("/pic/user/defaultImg/%s", res.getPic()));
        } else if(profile.equals(profile3)){
            res.setPic(res.getPic());
        } else {
                res.setPic(PicUrlMaker.makePicUserUrl(userId, res.getPic()));
//            res.setPic(PicUrlMaker.makePicUserUuidUrl(res.getUuid(),res.getPic()));
        }




        return res;


    }

    public String postUserAccessToken(HttpServletRequest req) {

        Cookie cookie = cookieUtils.getCookie(req, "refreshToken");
        String checkDomain = cookie.getDomain();
//        if(checkDomain.contains("112.222."))


        if (cookie != null) {
            String refreshToken = cookie.getValue();
            if (refreshToken != null) {

                JwtUser jwtUser = tokenProvider.getJwtUserFromToken(refreshToken);

                String accessToken = tokenProvider.generateAccessToken(jwtUser);

                return accessToken;
            }
        }

        return null;
    }

    public int updateUserInfo(UserInfoPatchReq p, MultipartFile pic) {

        long userId = authenticationFacade.getSignedUserId();

//        p.setUserId(userId);

        User user=new User();
        user.setUserId(userId);
        user.setPhone(p.getPhone());
        user.setName(p.getName());




        if (pic == null) {
//            int result = mapper.updateUserInfo(p);

            int result=userRepository.updateUserInfo(user);

            return result;

        }


        String savedPicName = myFileUtils.makeRandomFileName(pic);

        user.setPic(savedPicName);


//        int result = mapper.updateUserInfo(p);

        int result=userRepository.updateUserInfo(user);

        String middlePath = String.format("user/%d", userId);
//        String middlePath = String.format("%s/%s",UUID.randomUUID().toString().replace("-", ""),savedPicName);


        myFileUtils.deleteFolder(middlePath,true);

        myFileUtils.makeFolders(middlePath);

        String filePath = String.format("%s/%s", middlePath, savedPicName);

        try {
            myFileUtils.transferTo(pic, filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }


        return result;
    }


    public int deleteUser(UserInfoDelReq p) {

        long userId=authenticationFacade.getSignedUserId();
        p.setUserId(userId);

        String pw = mapper.selectInfoPwUser(p.getUserId());

        if (pw == null || !passwordEncoder.matches(p.getUpw(), pw)) {
            return 0;
        }



//        int result = mapper.deleteUser(p);

        int result=userRepository.deleteUser(userId);


        String deletePath = String.format("user/%d", p.getUserId());
        myFileUtils.deleteFolder(deletePath, true);


        return result;

    }

    public UserPwPatchRes updatePassword(UserPwPatchReq p) {

        // 1. 사용자가 현재 비밀번호를 알고 있는 경우
        if (p.getCurrentPassword() != null) {

            long userId=authenticationFacade.getSignedUserId();

            p.setUserId(userId);

            String pw = mapper.selectInfoPwUser(p.getUserId());

            // 비밀번호 일치 여부 체크
            if (pw == null || !passwordEncoder.matches(p.getCurrentPassword(), pw)) {
                return UserPwPatchRes.builder()
                        .message("비밀번호가 일치하지 않습니다.")
                        .result(0)
                        .build();
            }

            // 비밀번호 해싱
            String hashedPw = passwordEncoder.encode(p.getNewPassword());
            p.setNewPassword(hashedPw);

            // 비밀번호 변경
            int result = mapper.updatePassword(p);

            return UserPwPatchRes.builder()
                    .message("비밀번호 변경 완료")
                    .result(result)
                    .build();
        }

        // 2. 사용자가 현재 비밀번호를 모를 경우, 이메일 인증을 통한 비밀번호 변경
        Integer authCheck = mailMapper.selAuthCheck(p.getEmail());

        // 인증된 이메일인지 체크
        if (authCheck != null && authCheck == 1) {
            String hashedPw = passwordEncoder.encode(p.getNewPassword());
            p.setNewPassword(hashedPw);

            // 이메일을 통한 비밀번호 변경
            int result = mapper.updatePasswordThEmail(p);

            mailMapper.delAuthInfo(p.getEmail());

            return UserPwPatchRes.builder()
                    .message("비밀번호 변경 완료")
                    .result(result)
                    .build();
        } else if (authCheck == null) {
            return UserPwPatchRes.builder()
                    .message("인증된 이메일이 아닙니다.")
                    .result(0)
                    .build();
        } else {
            return UserPwPatchRes.builder()
                    .message("인증된 이메일이 아닙니다.")
                    .result(0)
                    .build();
        }
    }

//    @Transactional
//    public int getUuidCheck(){
//        List<UserUuidDto> list=userMapper.getUuidCheck();
//
//        for(UserUuidDto str:list){
//            if(str.getUuid()==null){
//                Optional<User> optionalUser=userRepository.findById(str.getUserId());
//                User user=optionalUser.get();
//                user.setUuid(UUID.randomUUID().toString().replace("-", ""));
//                userRepository.save(user);
//            }
//        }
//        return 1;
//    }

}
