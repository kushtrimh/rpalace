package org.kushtrimhajrizi.rpalace.oauth.client;

import org.kushtrimhajrizi.rpalace.exception.UserDoesNotExistException;
import org.kushtrimhajrizi.rpalace.security.authority.Authority;
import org.kushtrimhajrizi.rpalace.security.authority.DefinedAuthority;
import org.kushtrimhajrizi.rpalace.security.user.User;
import org.kushtrimhajrizi.rpalace.security.user.UserRepository;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

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

    public OAuth2AuthorizedClientServiceImpl() {
    }

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
        User user = userRepository.findById(principalName).orElseThrow(UserDoesNotExistException::new);
        OAuth2AuthorizedClientEntity entity = oAuth2AuthorizedClientEntityRepository.findByClientIdAndUserAndActiveTrue(
                clientRegistrationId, user).orElse(null);
        if (entity == null) {
            return null;
        }
        return (T) getOAuth2AuthorizedClient(entity);
    }

    @Override
    @Transactional
    public void saveAuthorizedClient(OAuth2AuthorizedClient oAuth2AuthorizedClient, Authentication authentication) {
        User user = null;
        if (authentication instanceof UsernamePasswordAuthenticationToken) {
            user = (User) authentication.getPrincipal();
        } else if (authentication instanceof JwtAuthenticationToken) {
            String userId = ((Jwt) authentication.getPrincipal()).getSubject();
            user = userRepository.findById(userId).orElseThrow(UserDoesNotExistException::new);
        }
        if (user == null) {
            throw new UserDoesNotExistException("Could not get user from authentication object " + authentication);
        }
        OAuth2AccessToken oAuth2AccessToken = oAuth2AuthorizedClient.getAccessToken();
        OAuth2RefreshToken oAuth2RefreshToken = oAuth2AuthorizedClient.getRefreshToken();
        String clientRegistrationId = oAuth2AuthorizedClient.getClientRegistration().getRegistrationId();
        updateUserAuthorities(user, clientRegistrationId);
        oAuth2AuthorizedClientEntityRepository.deactive(clientRegistrationId, user);
        var oAuth2AuthorizedClientEntity = new OAuth2AuthorizedClientEntity.Builder()
                .clientId(clientRegistrationId)
                .user(user)
                .accessTokenType(oAuth2AccessToken.getTokenType().getValue())
                .accessToken(oAuth2AccessToken.getTokenValue())
                .accessTokenIssuedAt(oAuth2AccessToken.getIssuedAt())
                .accessTokenExpiresAt(oAuth2AccessToken.getExpiresAt())
                .accessTokenScopes(String.join(SCOPES_DELIMITER, oAuth2AccessToken.getScopes()))
                .refreshToken(oAuth2RefreshToken.getTokenValue())
                .refreshTokenIssuedAt(oAuth2RefreshToken.getIssuedAt())
                .createdAt(Instant.now())
                .active(true)
                .build();

        oAuth2AuthorizedClientEntityRepository.save(oAuth2AuthorizedClientEntity);
    }

    @Override
    @Transactional
    public void removeAuthorizedClient(String clientRegistrationId, String principalName) {
        User user = userRepository.findById(principalName).orElseThrow(UserDoesNotExistException::new);
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

    private void updateUserAuthorities(User user, String clientRegistrationId) {
        if (clientRegistrationId.equals("reddit")) {
            user.addAuthority(new Authority(DefinedAuthority.REDDIT_CLIENT));
        }
        userRepository.save(user);
    }
}
