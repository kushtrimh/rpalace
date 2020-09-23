package org.kushtrimhajrizi.rpalace.oauth.authserver;

import org.kushtrimhajrizi.rpalace.exception.AccessTokenException;
import org.kushtrimhajrizi.rpalace.security.user.User;

public interface AuthServerService {
    AccessTokenDTO createAccessToken(User user) throws AccessTokenException;

    String createRefreshToken(User user);
}
