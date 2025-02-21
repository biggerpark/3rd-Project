package com.green.jobdone.config.security.oauth;

import com.green.jobdone.config.jwt.JwtUser;
import com.green.jobdone.config.jwt.UserRole;
import lombok.Getter;

import java.util.List;

@Getter
public class OAuth2JwtUser extends JwtUser {
    private final String nickName;
    private final String pic;

    public OAuth2JwtUser(String nickName, String pic, long signedUserId, List<UserRole> roles) {
        super(signedUserId, roles);
        this.nickName = nickName;
        this.pic = pic;
    }
}
