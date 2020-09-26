package org.kushtrimhajrizi.rpalace.oauth.authserver.refreshtoken;

import org.kushtrimhajrizi.rpalace.exception.RefreshTokenException;
import org.kushtrimhajrizi.rpalace.exception.RefreshTokenNotFoundException;
import org.kushtrimhajrizi.rpalace.security.user.User;

public interface RefreshTokenService {
    String createNew(User user) throws RefreshTokenException;

    RefreshToken getActiveRefreshToken(User user) throws RefreshTokenNotFoundException;

    RefreshToken getActiveRefreshToken(String refreshToken) throws RefreshTokenException;

    boolean isActiveRefreshToken(String refreshToken) throws RefreshTokenException;
}
