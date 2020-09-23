package org.kushtrimhajrizi.rpalace.oauth.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.RequestEntity;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.oauth2.client.AuthorizedClientServiceOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.JdbcOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProvider;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProviderBuilder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
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

import javax.sql.DataSource;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;

@Configuration
public class OAuth2ClientConfig {

    @Value("${rpalace.user-agent}")
    private String userAgent;

    private final ClientRegistrationRepository clientRegistrationRepository;

    public OAuth2ClientConfig(ClientRegistrationRepository clientRegistrationRepository) {
        this.clientRegistrationRepository = clientRegistrationRepository;
    }

    @Bean
    public ClientRegistration redditClientRegistration() {
        return clientRegistrationRepository.findByRegistrationId("reddit");
    }

    @Bean
    public OAuth2AuthorizedClientService oAuth2AuthorizedClientService(DataSource dataSource, ClientRegistrationRepository clientRegistrationRepository) {
        return new JdbcOAuth2AuthorizedClientService(new JdbcTemplate(dataSource), clientRegistrationRepository);
    }

    @Bean
    public OAuth2AuthorizedClientManager authorizedClientManager(
            ClientRegistrationRepository clientRegistrationRepository,
            OAuth2AuthorizedClientService oAuth2AuthorizedClientService) {
        OAuth2AuthorizedClientProvider authorizedClientProvider = OAuth2AuthorizedClientProviderBuilder.builder()
                .authorizationCode()
                .refreshToken()
                .build();

        AuthorizedClientServiceOAuth2AuthorizedClientManager authorizedClientManager =
                new AuthorizedClientServiceOAuth2AuthorizedClientManager(
                        clientRegistrationRepository, oAuth2AuthorizedClientService);
        authorizedClientManager.setAuthorizedClientProvider(authorizedClientProvider);
        return authorizedClientManager;
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
