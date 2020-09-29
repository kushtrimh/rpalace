package org.kushtrimhajrizi.rpalace.oauth.authserver.accesstoken.versioning;

import org.kushtrimhajrizi.rpalace.exception.AccessTokenException;
import org.kushtrimhajrizi.rpalace.security.user.User;

public interface AccessTokenVersionService {

    String getAccessTokenVersion(String userId) throws AccessTokenException;

    /**
     * Updates the access token version for the given user
     * @param userId
     * @return new generated access token version
     */
    String updateAccessTokenVersion(String userId);

    String updateAccessTokenVersion(User user);

    String generateAccessTokenVersion();
}
