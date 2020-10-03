package org.kushtrimhajrizi.rpalace.oauth.client;

import org.kushtrimhajrizi.rpalace.security.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Kushtrim Hajrizi
 */
@Repository
public interface OAuth2AuthorizedClientEntityRepository extends JpaRepository<OAuth2AuthorizedClientEntity, String> {

    Optional<OAuth2AuthorizedClientEntity> findByClientIdAndUser(String clientId, User user);

    void deleteByClientIdAndUser(String clientId, User user);
}
