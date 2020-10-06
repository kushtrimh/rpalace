package org.kushtrimhajrizi.rpalace.oauth.client;

import org.kushtrimhajrizi.rpalace.security.user.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.RequestEntity;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProvider;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProviderBuilder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.endpoint.DefaultAuthorizationCodeTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.DefaultRefreshTokenTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AccessTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.client.endpoint.OAuth2RefreshTokenGrantRequest;
import org.springframework.security.oauth2.client.http.OAuth2ErrorResponseErrorHandler;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.AuthenticatedPrincipalOAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationExchange;
import org.springframework.security.oauth2.core.http.converter.OAuth2AccessTokenResponseHttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;

@Configuration
public class OAuth2ClientConfig {

    @Value("${rpalace.user-agent}")
    private String userAgent;

    private ClientRegistrationRepository clientRegistrationRepository;

    public OAuth2ClientConfig(ClientRegistrationRepository clientRegistrationRepository) {
        this.clientRegistrationRepository = clientRegistrationRepository;
    }

    @Bean
    public WebClient webClient(OAuth2AuthorizedClientManager authorizedClientManager) {
        ServletOAuth2AuthorizedClientExchangeFilterFunction oauth2ClientExchangeFilterFunction =
                new ServletOAuth2AuthorizedClientExchangeFilterFunction(authorizedClientManager);
        return WebClient.builder()
                .apply(oauth2ClientExchangeFilterFunction.oauth2Configuration())
                .build();
    }

    @Bean
    public ClientRegistration redditClientRegistration() {
        return clientRegistrationRepository.findByRegistrationId("reddit");
    }

    @Bean
    public OAuth2AuthorizedClientService oAuth2AuthorizedClientService(
            ClientRegistrationRepository clientRegistrationRepository,
            UserRepository userRepository,
            OAuth2AuthorizedClientEntityRepository oAuth2AuthorizedClientEntityRepository) {
        return new OAuth2AuthorizedClientServiceImpl(oAuth2AuthorizedClientEntityRepository,
                clientRegistrationRepository,
                userRepository);
    }


    @Bean
    public OAuth2AuthorizedClientRepository oAuth2AuthorizedClientRepository(
            OAuth2AuthorizedClientService oAuth2AuthorizedClientService) {
        return new AuthenticatedPrincipalOAuth2AuthorizedClientRepository(oAuth2AuthorizedClientService);
    }

    @Bean
    public OAuth2AuthorizedClientManager authorizedClientManager(
            ClientRegistrationRepository clientRegistrationRepository,
            OAuth2AuthorizedClientRepository oAuth2AuthorizedClientRepository,
            OAuth2AccessTokenResponseClient<OAuth2RefreshTokenGrantRequest> oAuth2RefreshTokenResponseClient) {
        OAuth2AuthorizedClientProvider authorizedClientProvider = OAuth2AuthorizedClientProviderBuilder.builder()
                .authorizationCode()
                .refreshToken(refreshTokenGrantBuilder -> refreshTokenGrantBuilder
                        .accessTokenResponseClient(oAuth2RefreshTokenResponseClient))
                .build();

        DefaultOAuth2AuthorizedClientManager authorizedClientManager =
                new DefaultOAuth2AuthorizedClientManager(clientRegistrationRepository, oAuth2AuthorizedClientRepository);
        authorizedClientManager.setAuthorizedClientProvider(authorizedClientProvider);
        return authorizedClientManager;
    }

    @Bean
    public OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> oAuth2AccessTokenResponseClient() {
        DefaultAuthorizationCodeTokenResponseClient defaultAuthorizationClient =
                new DefaultAuthorizationCodeTokenResponseClient();
        ClientRegistration clientRegistration = redditClientRegistration();

        String basicAuthorizationCode = getBasicAuthorizationCode(clientRegistration);
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
                new OAuth2AccessTokenResponseHttpMessageConverter()
        ));
        restTemplate.setErrorHandler(new OAuth2ErrorResponseErrorHandler());
        defaultAuthorizationClient.setRestOperations(restTemplate);

        return defaultAuthorizationClient;
    }

    @Bean
    public OAuth2AccessTokenResponseClient<OAuth2RefreshTokenGrantRequest> oAuth2AccessTokenResponseClientRefreshToken() {
        DefaultRefreshTokenTokenResponseClient defaultRefreshTokenTokenResponseClient =
                new DefaultRefreshTokenTokenResponseClient();

        ClientRegistration clientRegistration = redditClientRegistration();
        String basicAuthorizationCode = getBasicAuthorizationCode(clientRegistration);
        defaultRefreshTokenTokenResponseClient.setRequestEntityConverter((refreshTokenRequest) -> {
            String refreshToken = refreshTokenRequest.getRefreshToken().getTokenValue();
            MultiValueMap<String, String> formParameters = new LinkedMultiValueMap<>();
            formParameters.add("grant_type", AuthorizationGrantType.REFRESH_TOKEN.getValue());
            formParameters.add("refresh_token", refreshToken);
            return RequestEntity
                    .post(URI.create(clientRegistration.getProviderDetails().getTokenUri()))
                    .header(HttpHeaders.AUTHORIZATION, "Basic " + basicAuthorizationCode)
                    .header(HttpHeaders.USER_AGENT, userAgent)
                    .body(formParameters);
        });

        RestTemplate restTemplate = new RestTemplate(List.of(
            new FormHttpMessageConverter(),
            new OAuth2AccessTokenResponseHttpMessageConverter()
        ));
        restTemplate.setErrorHandler(new OAuth2ErrorResponseErrorHandler());
        defaultRefreshTokenTokenResponseClient.setRestOperations(restTemplate);

        return defaultRefreshTokenTokenResponseClient;
    }

    private String getBasicAuthorizationCode(ClientRegistration clientRegistration) {
        String credentials = clientRegistration.getClientId() + ":" + clientRegistration.getClientSecret();
        return Base64.getEncoder().encodeToString(
                credentials.getBytes(StandardCharsets.UTF_8));
    }
}
