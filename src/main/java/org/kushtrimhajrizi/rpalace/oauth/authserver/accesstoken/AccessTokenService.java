package org.kushtrimhajrizi.rpalace.oauth.authserver.accesstoken;

import org.kushtrimhajrizi.rpalace.security.user.User;

public interface AccessTokenService {
    AccessTokenDTO createNew(User user);

    void invalidate(String userId);
}
