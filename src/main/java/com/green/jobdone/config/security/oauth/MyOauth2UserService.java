package com.green.jobdone.config.security.oauth;

import com.green.jobdone.config.jwt.UserRole;
import com.green.jobdone.config.security.SignInProviderType;
import com.green.jobdone.config.security.oauth.userinfo.Oauth2UserInfo;
import com.green.jobdone.config.security.oauth.userinfo.Oauth2UserInfoFactory;
import com.green.jobdone.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import com.green.jobdone.config.security.MyUserDetails;
import com.green.jobdone.entity.User;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MyOauth2UserService extends DefaultOAuth2UserService {
    private final UserRepository userRepository;
    private final Oauth2UserInfoFactory oauth2UserInfoFactory;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest req) {
        try {
            return process(req);
        } catch (AuthenticationException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new InternalAuthenticationServiceException(ex.getMessage(), ex.getCause());
        }
    }

    private OAuth2User process(OAuth2UserRequest req) {
        OAuth2User oAuth2User = super.loadUser(req);
        /*
        req.getClientRegistration().getRegistrationId(); 소셜로그인 신청한 플랫폼 문자열값이 넘어온다.
        플랫폼 문자열값은 spring.security.oauth2.client.registration 아래에 있는 속성값들이다. (google, kakao, naver)
         */
        SignInProviderType signInProviderType = SignInProviderType.valueOf(req.getClientRegistration()
                .getRegistrationId()
                .toUpperCase());
        //사용하기 편하도록 규격화된 객체로 변환
        Oauth2UserInfo oauth2UserInfo = oauth2UserInfoFactory.getOauth2UserInfo(signInProviderType, oAuth2User.getAttributes());

        //기존에 회원가입이 되어있는지 체크
        User user = userRepository.findByEmailAndProviderType(oauth2UserInfo.getEmail(), signInProviderType);
        if(user == null) { // 최초 로그인 상황 > 회원가입 처리
            user = new User();
            user.setProviderType(signInProviderType);
            user.setEmail(oauth2UserInfo.getEmail());
            user.setUpw("");
            user.setName(oauth2UserInfo.getName());
            user.setPhone("");
            user.setPic(oauth2UserInfo.getProfileImageUrl());
            userRepository.save(user);
        }
//        JwtUser jwtUser = new JwtUser();
//        jwtUser.setSignedUserId(user.getUserId());
//        jwtUser.setRoles(new ArrayList<>(1));
//        jwtUser.getRoles().add("ROLE_USER");
        List<UserRole> userRoles = new ArrayList<>();
        OAuth2JwtUser oauth2jwtUser = new OAuth2JwtUser(user.getName()
                , user.getPic()
                , user.getUserId()
                , Arrays.asList(UserRole.USER));

        MyUserDetails myUserDetails = new MyUserDetails();
//        myUserDetails.setJwtUser(jwtUser);
        myUserDetails.setJwtUser(oauth2jwtUser);

        return myUserDetails;
    }
}
