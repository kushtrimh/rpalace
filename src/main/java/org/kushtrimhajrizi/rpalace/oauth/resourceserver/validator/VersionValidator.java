package org.kushtrimhajrizi.rpalace.oauth.resourceserver.validator;

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

    private OAuth2Error error = new OAuth2Error("version", "Invalid token", null);
    private AccessTokenVersionService accessTokenVersionService;

    public VersionValidator(AccessTokenVersionService accessTokenVersionService) {
        this.accessTokenVersionService = accessTokenVersionService;
    }

    @Override
    public OAuth2TokenValidatorResult validate(Jwt jwt) {
        String userId = jwt.getSubject();
        String jwtVersion = jwt.getClaimAsString(JWTClaimParameter.VERSION.getParameterName());
        String currentVersion = accessTokenVersionService.getAccessTokenVersion(userId);
        return currentVersion.equals(jwtVersion) ?
                OAuth2TokenValidatorResult.success() :
                OAuth2TokenValidatorResult.failure(error);
    }
}
