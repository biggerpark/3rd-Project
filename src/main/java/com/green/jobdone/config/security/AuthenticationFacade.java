package com.green.jobdone.config.security;

import com.green.jobdone.config.jwt.JwtUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationFacade {
    public JwtUser getSignedUser() {
        MyUserDetails myUserDetails = (MyUserDetails) SecurityContextHolder.getContext()
                                                                           .getAuthentication()
                                                                           .getPrincipal();// Authentication 의 Principal 요소에 myUserDetails 를 담았었기 때문에 여기서 가져와야함.
        return myUserDetails.getJwtUser();
    }

    public long getSignedUserId() {
        return getSignedUser().getSignedUserId();
    }

    public String getToken() {
        // 현재 인증된 사용자의 Authentication 객체를 가져옵니다.
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Authentication 객체에서 JWT 토큰을 가져옵니다.
        if (authentication != null && authentication.getCredentials() != null) {
            return authentication.getCredentials().toString();  // 토큰을 반환
        }

        return null;  // 인증되지 않은 경우 null 반환
    }
}
