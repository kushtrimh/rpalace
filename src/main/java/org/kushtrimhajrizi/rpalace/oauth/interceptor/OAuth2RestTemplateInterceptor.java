package org.kushtrimhajrizi.rpalace.oauth.interceptor;


import org.kushtrimhajrizi.rpalace.oauth.OAuth2TokenService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class OAuth2RestTemplateInterceptor implements ClientHttpRequestInterceptor {

    private final OAuth2TokenService oAuth2TokenService;

    public OAuth2RestTemplateInterceptor(OAuth2TokenService oAuth2TokenService) {
        this.oAuth2TokenService = oAuth2TokenService;
    }

    @Override
    public ClientHttpResponse intercept(HttpRequest httpRequest, byte[] bytes,
            ClientHttpRequestExecution clientHttpRequestExecution) throws IOException {
        oAuth2TokenService.getToken().ifPresent(token ->
            httpRequest.getHeaders().add(HttpHeaders.AUTHORIZATION, "Bearer " + token));
        return clientHttpRequestExecution.execute(httpRequest, bytes);
    }
}
