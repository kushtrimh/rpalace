package org.kushtrimhajrizi.rpalace.oauth.authserver.refreshtoken;

import org.kushtrimhajrizi.rpalace.security.user.User;

public interface RefreshTokenService {
    String createNew(User user);

    RefreshToken getActiveRefreshToken(User user);

    RefreshToken getActiveRefreshToken(String refreshToken);

    boolean isActiveRefreshToken(String refreshToken);
}
