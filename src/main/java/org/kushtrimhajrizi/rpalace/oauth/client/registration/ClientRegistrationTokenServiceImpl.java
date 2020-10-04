package org.kushtrimhajrizi.rpalace.oauth.client.registration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.kushtrimhajrizi.rpalace.exception.ClientRegistrationTokenException;
import org.kushtrimhajrizi.rpalace.exception.HashException;
import org.kushtrimhajrizi.rpalace.security.user.User;
import org.kushtrimhajrizi.rpalace.utils.Hasher;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * @author Kushtrim Hajrizi
 */
@Service
public class ClientRegistrationTokenServiceImpl implements ClientRegistrationTokenService {

    private static final Logger logger = LogManager.getLogger(ClientRegistrationTokenServiceImpl.class);

    private ClientRegistrationTokenRepository clientRegistrationTokenRepository;

    public ClientRegistrationTokenServiceImpl(ClientRegistrationTokenRepository clientRegistrationTokenRepository) {
        this.clientRegistrationTokenRepository = clientRegistrationTokenRepository;
    }

    @Override
    @Transactional
    public ClientRegistrationTokenDTO createClientRegistrationToken(User user,
                                                                    ClientRegistrationTokenDTO clientRegistrationTokenDTO) {
        UUID uuid = UUID.randomUUID();
        String clientRegistrationToken = uuid.toString();
        String hashedClientRegistrationToken;
        try {
            hashedClientRegistrationToken = Hasher.hashSha256(clientRegistrationToken);
        } catch (HashException e) {
            throw new ClientRegistrationTokenException("Could not create client registration", e);
        }
        // Delete all the existing client registration tokens for this user
        clientRegistrationTokenRepository.deleteByUser(user);

        String returnUrl = clientRegistrationTokenDTO.getReturnUrl();
        ClientRegistrationToken registration = new ClientRegistrationToken();
        registration.setUser(user);
        Instant createdAt = Instant.now();
        registration.setCreatedAt(createdAt);
        registration.setToken(hashedClientRegistrationToken);
        registration.setActive(true);
        registration.setReturnUrl(returnUrl);

        clientRegistrationTokenRepository.save(registration);
        return new ClientRegistrationTokenDTO(
                clientRegistrationToken, createdAt, returnUrl);
    }

    @Override
    @Transactional
    public Optional<ClientRegistrationToken> getClientRegistrationToken(String token) {
        String hashedClientRegistrationToken;
        try {
            hashedClientRegistrationToken = Hasher.hashSha256(token);
        } catch (HashException e) {
            throw new ClientRegistrationTokenException("Could not create client registration", e);
        }
        return clientRegistrationTokenRepository.findByToken(hashedClientRegistrationToken);
    }

    @Override
    @Transactional
    public Optional<ClientRegistrationToken> getClientRegistrationToken(User user) {
        return clientRegistrationTokenRepository.findByUser(user);
    }

    @Override
    @Transactional
    public void delete(ClientRegistrationToken clientRegistrationToken) {
        clientRegistrationTokenRepository.delete(clientRegistrationToken);
    }


    @Override
    @Transactional
    public void delete(User user) {
        clientRegistrationTokenRepository.deleteByUser(user);
    }

    @Override
    @Transactional
    public void disable(ClientRegistrationToken clientRegistrationToken) {
        logger.debug("Client registration token disabled: {}", clientRegistrationToken);
        clientRegistrationTokenRepository.disable(clientRegistrationToken.getToken());
    }
}
