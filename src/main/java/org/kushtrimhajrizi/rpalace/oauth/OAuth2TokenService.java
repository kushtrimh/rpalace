package org.kushtrimhajrizi.rpalace.oauth;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Component;

import javax.swing.plaf.SeparatorUI;
import java.util.Optional;

@Component
public class OAuth2TokenService {

    private final OAuth2AuthorizedClientService oAuth2AuthorizedClientService;

    public OAuth2TokenService(OAuth2AuthorizedClientService oAuth2AuthorizedClientService) {
        this.oAuth2AuthorizedClientService = oAuth2AuthorizedClientService;
    }

    public Optional<String> getToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof OAuth2AuthenticationToken authToken) {
            OAuth2AuthorizedClient client = oAuth2AuthorizedClientService.loadAuthorizedClient(
                    authToken.getAuthorizedClientRegistrationId(),
                    authToken.getName());
            if (client != null) {
                return Optional.ofNullable(client.getAccessToken().getTokenValue());
            }
            return Optional.empty();
        }
        return Optional.empty();
    }
}
