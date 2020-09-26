package org.kushtrimhajrizi.rpalace.oauth.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class OAuth2TokenService {

    @Value("${rpalace.registration-id}")
    private String registrationId;

    private final OAuth2AuthorizedClientService oAuth2AuthorizedClientService;

    public OAuth2TokenService(OAuth2AuthorizedClientService oAuth2AuthorizedClientService) {
        this.oAuth2AuthorizedClientService = oAuth2AuthorizedClientService;
    }

    public Optional<String> getToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        OAuth2AuthorizedClient authorizedClient = oAuth2AuthorizedClientService
                .loadAuthorizedClient(registrationId, authentication.getName());
        return Optional.ofNullable(authorizedClient).map(OAuth2AuthorizedClient::getAccessToken)
                .map(OAuth2AccessToken::getTokenValue);
    }
}
