package org.kushtrimhajrizi.rpalace.security.user;

import java.util.Optional;

public interface UserService {

    void save(UserDTO userDTO);

    Optional<User> getByEmail(String email);

    Optional<User> getById(String id);
}
