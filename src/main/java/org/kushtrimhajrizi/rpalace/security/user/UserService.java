package org.kushtrimhajrizi.rpalace.security.user;

import org.kushtrimhajrizi.rpalace.exception.UserAlreadyExistsException;

import java.util.Optional;

public interface UserService {

    void save(UserDTO userDTO) throws UserAlreadyExistsException;

    Optional<User> findByEmail(String email);

/*
    void setToken(int id, String token) throws UserAlreadyHasToken, UserDoesNotExist;
*/
}
