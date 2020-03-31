package org.kushtrimhajrizi.rpalace.oauth;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class OAuth2TokenService {

    private final OAuth2AuthorizedClientService oAuth2AuthorizedClientService;

    public OAuth2TokenService(OAuth2AuthorizedClientService oAuth2AuthorizedClientService) {
        this.oAuth2AuthorizedClientService = oAuth2AuthorizedClientService;
    }

    public Optional<String> getToken() {
        OAuth2AuthenticationToken token = OAuth2AuthenticationToken.class.cast(
                SecurityContextHolder.getContext().getAuthentication());
        if (token == null) {
            return Optional.empty();
        }
        OAuth2AuthorizedClient client = oAuth2AuthorizedClientService.loadAuthorizedClient(
                token.getAuthorizedClientRegistrationId(),
                token.getName());
        if (client != null) {
            return Optional.ofNullable(client.getAccessToken().getTokenValue());
        }
        return Optional.empty();
    }
}
