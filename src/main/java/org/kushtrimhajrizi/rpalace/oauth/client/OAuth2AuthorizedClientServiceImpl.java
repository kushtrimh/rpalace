package org.kushtrimhajrizi.rpalace.oauth.client;

import org.kushtrimhajrizi.rpalace.exception.UserDoesNotExistException;
import org.kushtrimhajrizi.rpalace.security.user.User;
import org.kushtrimhajrizi.rpalace.security.user.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.Set;

/**
 * @author Kushtrim Hajrizi
 */
public class OAuth2AuthorizedClientServiceImpl implements OAuth2AuthorizedClientService {

    private static final String SCOPES_DELIMITER = ",";

    private OAuth2AuthorizedClientEntityRepository oAuth2AuthorizedClientEntityRepository;
    private ClientRegistrationRepository clientRegistrationRepository;
    private UserRepository userRepository;

    public OAuth2AuthorizedClientServiceImpl(
            OAuth2AuthorizedClientEntityRepository oAuth2AuthorizedClientEntityRepository,
            ClientRegistrationRepository clientRegistrationRepository,
            UserRepository userRepository) {
        this.oAuth2AuthorizedClientEntityRepository = oAuth2AuthorizedClientEntityRepository;
        this.clientRegistrationRepository = clientRegistrationRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public <T extends OAuth2AuthorizedClient> T loadAuthorizedClient(String clientRegistrationId, String principalName) {
        User user = userRepository.findByEmail(principalName).orElseThrow(UserDoesNotExistException::new);
        OAuth2AuthorizedClientEntity entity = oAuth2AuthorizedClientEntityRepository.findByClientIdAndUser(
                clientRegistrationId, user).orElse(null);
        if (entity == null) {
            return null;
        }
        return (T) getOAuth2AuthorizedClient(entity);
    }

    @Override
    @Transactional
    public void saveAuthorizedClient(OAuth2AuthorizedClient oAuth2AuthorizedClient, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        OAuth2AccessToken oAuth2AccessToken = oAuth2AuthorizedClient.getAccessToken();
        OAuth2RefreshToken oAuth2RefreshToken = oAuth2AuthorizedClient.getRefreshToken();

        var oAuth2AuthorizedClientEntity = new OAuth2AuthorizedClientEntity.Builder()
                .clientId(oAuth2AuthorizedClient.getClientRegistration().getClientId())
                .user(user)
                .accessTokenType(oAuth2AccessToken.getTokenType().getValue())
                .accessToken(oAuth2AccessToken.getTokenValue())
                .accessTokenIssuedAt(oAuth2AccessToken.getIssuedAt())
                .accessTokenExpiresAt(oAuth2AccessToken.getExpiresAt())
                .accessTokenScopes(String.join(SCOPES_DELIMITER, oAuth2AccessToken.getScopes()))
                .refreshToken(oAuth2RefreshToken.getTokenValue())
                .refreshTokenIssuedAt(oAuth2RefreshToken.getIssuedAt())
                .createdAt(Instant.now())
                .build();

        oAuth2AuthorizedClientEntityRepository.save(oAuth2AuthorizedClientEntity);
    }

    @Override
    @Transactional
    public void removeAuthorizedClient(String clientRegistrationId, String principalName) {
        User user = userRepository.findByEmail(principalName).orElseThrow(UserDoesNotExistException::new);
        oAuth2AuthorizedClientEntityRepository.deleteByClientIdAndUser(clientRegistrationId, user);
    }

    private OAuth2AuthorizedClient getOAuth2AuthorizedClient(OAuth2AuthorizedClientEntity entity) {
        var tokenType = OAuth2AccessToken.TokenType.BEARER;
        var accessToken = new OAuth2AccessToken(tokenType,
                entity.getAccessToken(),
                entity.getAccessTokenIssuedAt(),
                entity.getAccessTokenExpiresAt(),
                Set.of(entity.getAccessTokenScopes().split(SCOPES_DELIMITER)));
        var refreshToken = new OAuth2RefreshToken(entity.getRefreshToken(),
                entity.getRefreshTokenIssuedAt());

        return new OAuth2AuthorizedClient(
                clientRegistrationRepository.findByRegistrationId(entity.getClientId()),
                entity.getUser().getEmail(),
                accessToken,
                refreshToken);
    }
}
