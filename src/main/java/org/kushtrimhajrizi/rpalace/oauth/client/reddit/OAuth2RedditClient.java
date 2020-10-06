package org.kushtrimhajrizi.rpalace.oauth.client.reddit;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * @author Kushtrim Hajrizi
 */
@Component
public class OAuth2RedditClient {

    private static final String CLIENT_ID = "reddit";

    private OAuth2AuthorizedClientManager oAuth2AuthorizedClientManager;
    private WebClient webClient;

    public OAuth2RedditClient(OAuth2AuthorizedClientManager oAuth2AuthorizedClientManager, WebClient webClient) {
        this.oAuth2AuthorizedClientManager = oAuth2AuthorizedClientManager;
        this.webClient = webClient;
    }

    private OAuth2AuthorizedClient getOAuth2AuthorizedClient() {
        OAuth2AuthorizeRequest oAuth2AuthorizeRequest = OAuth2AuthorizeRequest.withClientRegistrationId(CLIENT_ID)
                .principal(SecurityContextHolder.getContext().getAuthentication())
                .build();
        return oAuth2AuthorizedClientManager.authorize(oAuth2AuthorizeRequest);
    }
}
