package org.kushtrimhajrizi.rpalace.repository;

import org.kushtrimhajrizi.rpalace.entity.DefaultUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<DefaultUser, Integer> {

    DefaultUser findByEmail(String email);
}
