package org.kushtrimhajrizi.rpalace.oauth.interceptor;

import org.kushtrimhajrizi.rpalace.oauth.OAuth2TokenService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class OAuth2Interceptor implements HandlerInterceptor {

    private final OAuth2TokenService oAuth2TokenService;

    public OAuth2Interceptor(OAuth2TokenService oAuth2TokenService) {
        this.oAuth2TokenService = oAuth2TokenService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request,
        HttpServletResponse response, Object handler) throws Exception {
        if (request instanceof HttpRequest httpRequest) {
            oAuth2TokenService.getToken().ifPresent(token ->
                    httpRequest.getHeaders().add(HttpHeaders.AUTHORIZATION, "Bearer " + token));
        }
        return true;
    }
}
