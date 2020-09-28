package org.kushtrimhajrizi.rpalace.oauth.authserver.refreshtoken;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.kushtrimhajrizi.rpalace.exception.RefreshTokenException;
import org.kushtrimhajrizi.rpalace.security.user.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
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
    public String createNew(User user) throws RefreshTokenException {
        refreshTokenRepository.disableActiveRefreshToken(user);
        var uuid = UUID.randomUUID();
        var token = uuid.toString();
        String hashedToken = getHashedRefreshToken(token);
        RefreshToken refreshToken = RefreshToken.newActiveRefreshToken(
                hashedToken, user);
        refreshTokenRepository.save(refreshToken);
        return token;
    }

    @Override
    @Transactional
    public RefreshToken getActiveRefreshToken(User user) throws RefreshTokenException {
        return refreshTokenRepository.findByActiveTrueAndUser(user)
                .orElseThrow(() -> new RefreshTokenException("Could not find refresh token"));
    }

    @Override
    @Transactional
    public RefreshToken getActiveRefreshToken(String refreshToken) throws RefreshTokenException {
        String hashedSubmittedRefreshToken = getHashedRefreshToken(refreshToken);
        return refreshTokenRepository.findByRefreshTokenHash(hashedSubmittedRefreshToken)
                .orElseThrow(RefreshTokenException::new);
    }

    @Override
    @Transactional
    public boolean isActiveRefreshToken(String submittedRefreshToken)
            throws RefreshTokenException {
        RefreshToken refreshToken = getActiveRefreshToken(submittedRefreshToken);
        return refreshToken.getActive();
    }

    private String getHashedRefreshToken(String token) throws RefreshTokenException {
        try {
            var digest = MessageDigest.getInstance("SHA-256");
            byte[] hashedTokenBytes = digest.digest(token.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hashedTokenBytes);
        } catch (NoSuchAlgorithmException e) {
            logger.error(e);
            throw new RefreshTokenException("Could not create refresh token", e);
        }
    }
}
