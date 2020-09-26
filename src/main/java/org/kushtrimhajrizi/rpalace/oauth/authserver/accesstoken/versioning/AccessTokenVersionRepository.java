package org.kushtrimhajrizi.rpalace.oauth.authserver.accesstoken.versioning;

import org.kushtrimhajrizi.rpalace.security.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccessTokenVersionRepository extends JpaRepository<AccessTokenVersion, String> {

    @Query(value = "select * from access_token_version WHERE user_id = ?1", nativeQuery = true)
    Optional<AccessTokenVersion> findByUserId(String userId);

    Optional<AccessTokenVersion> findByUser(User user);

    @Modifying
    @Query(value = "update access_token_version set version = ?1 where user_id = ?2", nativeQuery = true)
    void updateAccessTokenVersion(String newAccessTokenVersion, String userId);
}
