package org.kushtrimhajrizi.rpalace.oauth.authserver.refreshtoken;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.kushtrimhajrizi.rpalace.exception.HashException;
import org.kushtrimhajrizi.rpalace.exception.RefreshTokenException;
import org.kushtrimhajrizi.rpalace.security.user.User;
import org.kushtrimhajrizi.rpalace.utils.Hasher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.UUID;

@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private static final Logger logger = LogManager.getLogger(RefreshTokenServiceImpl.class);

    private RefreshTokenRepository refreshTokenRepository;

    public RefreshTokenServiceImpl(RefreshTokenRepository refreshTokenRepository,
                                   PasswordEncoder passwordEncoder) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Override
    @Transactional
    public String createNew(User user) {
        refreshTokenRepository.disableActiveRefreshToken(user);
        var uuid = UUID.randomUUID();
        var token = uuid.toString();
        String hashedToken;
        try {
            hashedToken = Hasher.hashSha256(token);
        } catch (HashException e) {
            throw new RefreshTokenException("Could not create refresh token", e);
        }
        RefreshToken refreshToken = RefreshToken.newActiveRefreshToken(
                hashedToken, user);
        refreshTokenRepository.save(refreshToken);
        return token;
    }

    @Override
    @Transactional
    public RefreshToken getActiveRefreshToken(User user)  {
        return refreshTokenRepository.findByActiveTrueAndUser(user)
                .orElseThrow(() -> new RefreshTokenException("Could not find refresh token"));
    }

    @Override
    @Transactional
    public RefreshToken getActiveRefreshToken(String refreshToken) {
        String hashedSubmittedRefreshToken;
        try {
            hashedSubmittedRefreshToken = Hasher.hashSha256(refreshToken);
        } catch (HashException e) {
            throw new RefreshTokenException("Could not create refresh token", e);
        }
        return refreshTokenRepository.findByRefreshTokenHash(hashedSubmittedRefreshToken)
                .orElseThrow(RefreshTokenException::new);
    }

    @Override
    @Transactional
    public boolean isActiveRefreshToken(String submittedRefreshToken) {
        RefreshToken refreshToken = getActiveRefreshToken(submittedRefreshToken);
        return refreshToken.getActive();
    }
}
