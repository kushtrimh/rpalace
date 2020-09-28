package org.kushtrimhajrizi.rpalace.oauth.authserver.accesstoken.versioning;

import org.kushtrimhajrizi.rpalace.exception.AccessTokenException;
import org.kushtrimhajrizi.rpalace.security.user.User;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.UUID;

@Service
public class AccessTokenVersionServiceImpl implements AccessTokenVersionService {

    private AccessTokenVersionRepository accessTokenVersionRepository;

    public AccessTokenVersionServiceImpl(AccessTokenVersionRepository accessTokenVersionRepository) {
        this.accessTokenVersionRepository = accessTokenVersionRepository;
    }

    @Override
    @Transactional
    public String getAccessTokenVersion(String userId) throws AccessTokenException {
        AccessTokenVersion version = accessTokenVersionRepository.findByUserId(userId)
                .orElseThrow(() -> new AccessTokenException("User does not have access token version"));
        return version.getVersion();
    }

    @Override
    @Transactional
    public String updateAccessTokenVersion(String userId) {
        String newVersion = generateAccessTokenVersion();
        accessTokenVersionRepository.updateAccessTokenVersion(newVersion, userId);
        return newVersion;
    }

    @Override
    @Transactional
    public String updateAccessTokenVersion(User user) {
        return updateAccessTokenVersion(user.getId());
    }

    @Override
    public String generateAccessTokenVersion() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }
}
