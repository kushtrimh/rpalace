package org.kushtrimhajrizi.rpalace.oauth.client.reddit;

import org.kushtrimhajrizi.rpalace.api.reddit.response.UserInfoResponse;
import org.kushtrimhajrizi.rpalace.oauth.client.OAuth2Client;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import static org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction.oauth2AuthorizedClient;

/**
 * @author Kushtrim Hajrizi
 */
@Component
public class OAuth2RedditClient implements OAuth2Client {

    private static final String CLIENT_ID = "reddit";

    private OAuth2AuthorizedClientManager oAuth2AuthorizedClientManager;
    private WebClient webClient;

    public OAuth2RedditClient(OAuth2AuthorizedClientManager oAuth2AuthorizedClientManager, WebClient webClient) {
        this.oAuth2AuthorizedClientManager = oAuth2AuthorizedClientManager;
        this.webClient = webClient;
    }

    public UserInfoResponse getUserInfo() {
        return webClient.get()
                .uri("/api/v1/me")
                .attributes(oauth2AuthorizedClient(getOAuth2AuthorizedClient()))
                .retrieve()
                .bodyToMono(UserInfoResponse.class)
                .block();
    }

    private OAuth2AuthorizedClient getOAuth2AuthorizedClient() {
        OAuth2AuthorizeRequest oAuth2AuthorizeRequest = OAuth2AuthorizeRequest.withClientRegistrationId(CLIENT_ID)
                .principal(SecurityContextHolder.getContext().getAuthentication())
                .build();
        return oAuth2AuthorizedClientManager.authorize(oAuth2AuthorizeRequest);
    }
}
