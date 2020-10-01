package org.kushtrimhajrizi.rpalace.oauth.client;

import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;

/**
 * @author Kushtrim Hajrizi
 */
//@Entity
//@Table(name = "oauth2_authorized_client")
public class DefaultOAuth2AuthorizedClient extends OAuth2AuthorizedClient {

    public DefaultOAuth2AuthorizedClient(ClientRegistration clientRegistration, String principalName,
                                         OAuth2AccessToken accessToken) {
        super(clientRegistration, principalName, accessToken);
    }

    public DefaultOAuth2AuthorizedClient(ClientRegistration clientRegistration, String principalName,
                                         OAuth2AccessToken accessToken, OAuth2RefreshToken refreshToken) {
        super(clientRegistration, principalName, accessToken, refreshToken);
    }
}
