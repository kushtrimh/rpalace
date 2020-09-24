package org.kushtrimhajrizi.rpalace.security.user;

import org.kushtrimhajrizi.rpalace.exception.UserAlreadyExistsException;

import java.util.Optional;

public interface UserService {

    void save(UserDTO userDTO) throws UserAlreadyExistsException;

    Optional<User> getByEmail(String email);
}
