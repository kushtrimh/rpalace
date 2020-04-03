package org.kushtrimhajrizi.rpalace.oauth.config;

import org.kushtrimhajrizi.rpalace.oauth.interceptor.OAuth2RestTemplateInterceptor;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.RequestEntity;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.endpoint.*;
import org.springframework.security.oauth2.client.http.OAuth2ErrorResponseErrorHandler;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationExchange;
import org.springframework.security.oauth2.core.http.converter.OAuth2AccessTokenResponseHttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;


@EnableWebSecurity
public class OAuth2SecurityConfig extends WebSecurityConfigurerAdapter {

    private final ClientRegistrationRepository clientRegistrationRepository;

    public OAuth2SecurityConfig(ClientRegistrationRepository clientRegistrationRepository) {
        this.clientRegistrationRepository = clientRegistrationRepository;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
            .antMatchers("/", "/login**", "/login/**", "/error**", "/oauth2/authorization/**", "/webjars/**", "/reddit**")
            .permitAll()
            .anyRequest()
            .authenticated()
            .and()
            .oauth2Login(oauth2Login -> oauth2Login
                .tokenEndpoint(tokenEndpointConfig ->
                        tokenEndpointConfig.accessTokenResponseClient(oAuth2AccessTokenResponseClient())))
            .oauth2Client(oauth2Client -> oauth2Client
                .authorizationCodeGrant(codeGrant ->
                        codeGrant.accessTokenResponseClient(oAuth2AccessTokenResponseClient())));

    }

    @Bean
    public ClientRegistration redditClientRegistration() {
        return clientRegistrationRepository.findByRegistrationId("reddit");
    }

    @Bean
    public RestTemplate restTemplate(OAuth2RestTemplateInterceptor oAuth2RestTemplateInterceptor) {
        return new RestTemplateBuilder().defaultHeader(HttpHeaders.USER_AGENT, "RPalace v.0.1")
                .interceptors(oAuth2RestTemplateInterceptor).build();
    }

    @Bean
    public OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> oAuth2AccessTokenResponseClient() {
        DefaultAuthorizationCodeTokenResponseClient defaultAuthorizationClient =
                new DefaultAuthorizationCodeTokenResponseClient();
        ClientRegistration clientRegistration = redditClientRegistration();

        String credentials = clientRegistration.getClientId() + ":" + clientRegistration.getClientSecret();
        String basicAuthorizationCode = Base64.getEncoder().encodeToString(
                credentials.getBytes(StandardCharsets.UTF_8));
        defaultAuthorizationClient.setRequestEntityConverter((authorizationCodeGrantRequest) -> {
            OAuth2AuthorizationExchange authorizationExchange = authorizationCodeGrantRequest.getAuthorizationExchange();
            MultiValueMap<String, String> formParameters = new LinkedMultiValueMap<>();
            formParameters.add("grant_type", clientRegistration.getAuthorizationGrantType().getValue());
            formParameters.add("code", authorizationExchange.getAuthorizationResponse().getCode());
            formParameters.add("redirect_uri", clientRegistration.getRedirectUriTemplate());
            return RequestEntity
                    .post(URI.create(clientRegistration.getProviderDetails().getTokenUri()))
                    .header(HttpHeaders.AUTHORIZATION, "Basic " + basicAuthorizationCode)
                    .header(HttpHeaders.USER_AGENT, "RPalace v.1.0")
                    .body(formParameters);
        });

        RestTemplate restTemplate = new RestTemplate(List.of(
                new FormHttpMessageConverter(),
                new OAuth2AccessTokenResponseHttpMessageConverter())
        );
        restTemplate.setErrorHandler(new OAuth2ErrorResponseErrorHandler());
        defaultAuthorizationClient.setRestOperations(restTemplate);

        return defaultAuthorizationClient;
    }
}