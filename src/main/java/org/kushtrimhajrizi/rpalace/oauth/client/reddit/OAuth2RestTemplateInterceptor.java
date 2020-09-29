package org.kushtrimhajrizi.rpalace.oauth.client.reddit;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Deprecated
public class OAuth2RestTemplateInterceptor implements ClientHttpRequestInterceptor {

    @Value("${rpalace.user-agent}")
    private String userAgent;

    private final OAuth2TokenService oAuth2TokenService;

    public OAuth2RestTemplateInterceptor(OAuth2TokenService oAuth2TokenService) {
        this.oAuth2TokenService = oAuth2TokenService;
    }

    @Override
    public ClientHttpResponse intercept(HttpRequest httpRequest, byte[] bytes,
            ClientHttpRequestExecution clientHttpRequestExecution) throws IOException {
        oAuth2TokenService.getToken().ifPresent(token -> {
            httpRequest.getHeaders().add(HttpHeaders.AUTHORIZATION, "bearer " + token);
            httpRequest.getHeaders().add(HttpHeaders.USER_AGENT, userAgent);
        });
        return clientHttpRequestExecution.execute(httpRequest, bytes);
    }
}
