package com.green.jobdone.config.security.oauth;

import com.green.jobdone.common.CookieUtils;
import com.green.jobdone.common.GlobalOauth2;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class Oauth2AuthenticationRequestBasedOnCookieRepository
        implements AuthorizationRequestRepository<OAuth2AuthorizationRequest> {

    private final CookieUtils cookieUtils;
    private final GlobalOauth2 globalOauth2;

    @Override
    public OAuth2AuthorizationRequest loadAuthorizationRequest(HttpServletRequest request) {
        return cookieUtils.getValue(request
                , globalOauth2.getAuthorizationRequestCookieName()
                , OAuth2AuthorizationRequest.class);
    }

    @Override
    public void saveAuthorizationRequest(OAuth2AuthorizationRequest authorizationRequest, HttpServletRequest request, HttpServletResponse response) {
        if(authorizationRequest == null) {
            this.removeAuthorizationCookies(response);
        }
        cookieUtils.setCookie(response
                , globalOauth2.getAuthorizationRequestCookieName()
                , authorizationRequest
                , globalOauth2.getCookieExpirySeconds()
                , "/");
        //FE 요청한 redirect_uri 쿠키에 저장한다.
        String redirectUriAfterLogin = request.getParameter(globalOauth2.getRedirectUriParamCookieName());
        cookieUtils.setCookie(response
                , globalOauth2.getRedirectUriParamCookieName()
                , redirectUriAfterLogin
                , globalOauth2.getCookieExpirySeconds()
                , "/");
    }

    @Override
    public OAuth2AuthorizationRequest removeAuthorizationRequest(HttpServletRequest request, HttpServletResponse response) {
        return this.loadAuthorizationRequest(request);
    }
    public void removeAuthorizationCookies(HttpServletResponse response) {
        cookieUtils.deleteCookie(response, globalOauth2.getAuthorizationRequestCookieName());
        cookieUtils.deleteCookie(response, globalOauth2.getRedirectUriParamCookieName());
    }
}