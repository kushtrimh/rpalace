package org.kushtrimhajrizi.rpalace.oauth.client.registration;

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

    private ClientRegistrationTokenRepository clientRegistrationTokenRepository;

    public ClientRegistrationTokenServiceImpl(ClientRegistrationTokenRepository clientRegistrationTokenRepository) {
        this.clientRegistrationTokenRepository = clientRegistrationTokenRepository;
    }

    @Override
    @Transactional
    public ClientRegistrationTokenDTO createClientRegistrationToken(User user) {
        UUID uuid = UUID.randomUUID();
        String clientRegistrationToken = uuid.toString();
        String hashedClientRegistrationToken;
        try {
            hashedClientRegistrationToken = Hasher.hashSha256(clientRegistrationToken);
        } catch (HashException e) {
            throw new ClientRegistrationTokenException("Could not create client registration", e);
        }
        ClientRegistrationToken registration = new ClientRegistrationToken();
        registration.setUser(user);
        Instant createdAt = Instant.now();
        registration.setCreatedAt(createdAt);
        registration.setToken(hashedClientRegistrationToken);
        clientRegistrationTokenRepository.save(registration);
        return new ClientRegistrationTokenDTO(clientRegistrationToken, createdAt);
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
    public void delete(ClientRegistrationToken clientRegistrationToken) {
        clientRegistrationTokenRepository.delete(clientRegistrationToken);
    }
}
