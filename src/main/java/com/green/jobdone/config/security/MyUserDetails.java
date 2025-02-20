package com.green.jobdone.config.security;

import com.green.jobdone.config.jwt.JwtUser;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Setter
@Getter
public class MyUserDetails implements UserDetails, OAuth2User {

    private JwtUser jwtUser;

    @Override
    public Map<String, Object> getAttributes() {
        return Map.of();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return jwtUser.getRoles().stream()// List<String>을 Stream<List<String>>으로 변환
//                .map(SimpleGrantedAuthority::new) // 각 문자열을 SimpleGrantedAuthority로 매핑

                // UserRole을 문자열로 변환하여 SimpleGrantedAuthority 생성
                .map(item->new SimpleGrantedAuthority("ROLE_"+item.name()))
//                .map(item->new SimpleGrantedAuthority(item)) 위의 map 코드와 같은 표현.
                .toList();
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return "GUEST";
    }

    @Override
    public String getName() {
        return "";
    }
}
