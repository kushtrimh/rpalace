package org.kushtrimhajrizi.rpalace.oauth.client.registration;

import org.kushtrimhajrizi.rpalace.security.user.User;

import java.util.Optional;

/**
 * @author Kushtrim Hajrizi
 */
public interface ClientRegistrationTokenService {

    ClientRegistrationTokenDTO createClientRegistrationToken(User user);

    Optional<ClientRegistrationToken> getClientRegistrationToken(String token);

    void delete(ClientRegistrationToken clientRegistrationToken);
}
