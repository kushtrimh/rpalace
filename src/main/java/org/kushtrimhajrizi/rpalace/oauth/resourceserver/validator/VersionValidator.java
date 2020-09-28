package org.kushtrimhajrizi.rpalace.oauth.resourceserver.validator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.kushtrimhajrizi.rpalace.exception.AccessTokenException;
import org.kushtrimhajrizi.rpalace.oauth.JWTClaimParameter;
import org.kushtrimhajrizi.rpalace.oauth.authserver.accesstoken.versioning.AccessTokenVersionService;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

/**
 * @author Kushtrim Hajrizi
 */
@Component
public class VersionValidator implements OAuth2TokenValidator<Jwt> {

    private static final Logger logger = LogManager.getLogger(VersionValidator.class);

    private OAuth2Error error = new OAuth2Error("version", "Invalid token", null);
    private AccessTokenVersionService accessTokenVersionService;

    public VersionValidator(AccessTokenVersionService accessTokenVersionService) {
        this.accessTokenVersionService = accessTokenVersionService;
    }

    @Override
    public OAuth2TokenValidatorResult validate(Jwt jwt) {
        String userId = jwt.getSubject();
        String jwtVersion = jwt.getClaimAsString(JWTClaimParameter.VERSION.getParameterName());
        String currentVersion = null;
        try {
            currentVersion = accessTokenVersionService.getAccessTokenVersion(userId);
        } catch (AccessTokenException e) {
           logger.error("Access token not valid", e);
           return OAuth2TokenValidatorResult.failure(error);
        }
        return currentVersion.equals(jwtVersion) ?
                OAuth2TokenValidatorResult.success() :
                OAuth2TokenValidatorResult.failure(error);
    }
}
