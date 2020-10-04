package org.kushtrimhajrizi.rpalace.oauth.client.registration;

import org.kushtrimhajrizi.rpalace.security.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Kushtrim Hajrizi
 */
@Repository
public interface ClientRegistrationTokenRepository extends JpaRepository <ClientRegistrationToken, String> {

    Optional<ClientRegistrationToken> findByToken(String token);

    Optional<ClientRegistrationToken> findByUser(User user);

    void deleteByUser(User user);

    @Modifying
    @Query("update ClientRegistrationToken crt set crt.active = 0 where crt.token = ?1")
    void disable(String token);
}
