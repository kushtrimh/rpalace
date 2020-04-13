package org.kushtrimhajrizi.rpalace.security.config;

import org.kushtrimhajrizi.rpalace.security.PermissionType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.RequestEntity;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.endpoint.DefaultAuthorizationCodeTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AccessTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
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
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${rpalace.user-agent}")
    private String userAgent;

    private final ClientRegistrationRepository clientRegistrationRepository;
    private final DefaultUserDetailsService defaultUserDetailsService;

    public SecurityConfig(ClientRegistrationRepository clientRegistrationRepository,
                          DefaultUserDetailsService defaultUserDetailsService) {
        this.clientRegistrationRepository = clientRegistrationRepository;
        this.defaultUserDetailsService = defaultUserDetailsService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests(authorize ->
                    authorize
                    .antMatchers("/login**", "/error**", "/oauth2/**", "/webjars/**", "/register**")
                    .permitAll()
                    .antMatchers("/oauth", "/oauth/**")
                    .authenticated()
                    .anyRequest()
                    .hasAuthority(PermissionType.TOKEN_USER.toString()))
            .formLogin(login -> login.successForwardUrl("/oauth"))
            .oauth2Client(oauth2Client -> oauth2Client
                .authorizationCodeGrant(codeGrant ->
                        codeGrant.accessTokenResponseClient(oAuth2AccessTokenResponseClient())));
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(defaultUserDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public ClientRegistration redditClientRegistration() {
        return clientRegistrationRepository.findByRegistrationId("reddit");
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
                    .header(HttpHeaders.USER_AGENT, userAgent)
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