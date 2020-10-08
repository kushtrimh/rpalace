package org.kushtrimhajrizi.rpalace.oauth.client;

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
public interface OAuth2AuthorizedClientEntityRepository extends JpaRepository<OAuth2AuthorizedClientEntity, String> {

    Optional<OAuth2AuthorizedClientEntity> findByClientIdAndUser(String clientId, User user);

    Optional<OAuth2AuthorizedClientEntity> findByClientIdAndUserAndActiveTrue(String clientId, User user);

    void deleteByClientIdAndUser(String clientId, User user);

    @Modifying
    @Query("update OAuth2AuthorizedClientEntity ent set ent.active = 0 where ent.clientId = ?1 and ent.user = ?2")
    void deactive(String clientId, User user);
}
