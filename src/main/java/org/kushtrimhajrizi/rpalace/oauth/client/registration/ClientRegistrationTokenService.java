package org.kushtrimhajrizi.rpalace.oauth.client.registration;

import org.kushtrimhajrizi.rpalace.security.user.User;

import java.util.Optional;

/**
 * @author Kushtrim Hajrizi
 */
public interface ClientRegistrationTokenService {

    ClientRegistrationTokenDTO createClientRegistrationToken(User user,
                                                             ClientRegistrationTokenDTO clientRegistrationTokenDTO);

    Optional<ClientRegistrationToken> getClientRegistrationToken(String token);

    Optional<ClientRegistrationToken> getClientRegistrationToken(User user);

    void delete(ClientRegistrationToken clientRegistrationToken);

    void delete(User user);

    void disable(ClientRegistrationToken clientRegistrationToken);
}
