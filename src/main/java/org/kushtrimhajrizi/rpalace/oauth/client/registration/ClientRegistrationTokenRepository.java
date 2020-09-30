package org.kushtrimhajrizi.rpalace.oauth.client.registration;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Kushtrim Hajrizi
 */
@Repository
public interface ClientRegistrationTokenRepository extends JpaRepository <ClientRegistrationToken, String> {
    Optional<ClientRegistrationToken> findByToken(String token);
}
