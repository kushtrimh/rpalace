package org.kushtrimhajrizi.rpalace.oauth.authserver.refreshtoken;

import org.kushtrimhajrizi.rpalace.security.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, String> {

    Optional<RefreshToken> findByRefreshTokenHash(String refreshTokenHash);

    Optional<RefreshToken> findByActiveTrueAndUser(User user);

    @Modifying
    @Query("update RefreshToken rt set rt.active = 0 where rt.user = ?1")
    void disableActiveRefreshToken(User user);
}
