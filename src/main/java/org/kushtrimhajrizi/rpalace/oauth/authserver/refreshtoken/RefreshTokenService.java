package org.kushtrimhajrizi.rpalace.oauth.authserver.refreshtoken;

import org.kushtrimhajrizi.rpalace.exception.RefreshTokenNotFoundException;
import org.kushtrimhajrizi.rpalace.security.user.User;

public interface RefreshTokenService {
    String createNew(User user);

    RefreshToken getActiveRefreshToken(User user) throws RefreshTokenNotFoundException;
}
