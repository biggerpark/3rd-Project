package com.green.jobdone.config.security.oauth.userinfo;

import java.util.Map;

public class GoogleOAuth2UserInfo extends Oauth2UserInfo {

    public GoogleOAuth2UserInfo(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getName() {
        return attributes.get("name").toString();
    }

    @Override
    public String getEmail() {
        return attributes.get("email").toString();
    }

    @Override
    public String getProfileImageUrl() {
        return attributes.get("picture").toString();
    }
}
