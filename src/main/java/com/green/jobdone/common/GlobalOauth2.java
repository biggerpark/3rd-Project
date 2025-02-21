package com.green.jobdone.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@Getter
@ConfigurationProperties(prefix = "global.oauth2")
@RequiredArgsConstructor
public class GlobalOauth2 {
    private final String baseUri;
    private final String authorizationRequestCookieName;
    private final String redirectUriParamCookieName;
    private final int cookieExpirySeconds;
    private final List<String> authorizedRedirectUris;
}
