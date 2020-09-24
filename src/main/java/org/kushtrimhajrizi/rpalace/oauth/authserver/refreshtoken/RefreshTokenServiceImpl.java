package org.kushtrimhajrizi.rpalace.oauth.authserver.refreshtoken;

import org.kushtrimhajrizi.rpalace.exception.RefreshTokenNotFoundException;
import org.kushtrimhajrizi.rpalace.security.user.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.UUID;

@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private RefreshTokenRepository refreshTokenRepository;
    private PasswordEncoder passwordEncoder;

    public RefreshTokenServiceImpl(RefreshTokenRepository refreshTokenRepository,
                                   PasswordEncoder passwordEncoder) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public String createNew(User user) {
        refreshTokenRepository.disableActiveRefreshToken(user);

        UUID uuid = UUID.randomUUID();
        String token = uuid.toString();
        RefreshToken refreshToken = RefreshToken.newActiveRefreshToken(
                passwordEncoder.encode(token), user);
        refreshTokenRepository.save(refreshToken);
        return token;
    }

    @Override
    @Transactional
    public RefreshToken getActiveRefreshToken(User user) throws RefreshTokenNotFoundException {
        return refreshTokenRepository.findByActiveTrueAndUser(user)
                .orElseThrow(() -> new RefreshTokenNotFoundException("Could not find refresh token"));
    }
}
