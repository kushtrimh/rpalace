package org.kushtrimhajrizi.rpalace.service;

import org.kushtrimhajrizi.rpalace.dto.UserDTO;
import org.kushtrimhajrizi.rpalace.exception.UserAlreadyExistsException;
import org.kushtrimhajrizi.rpalace.exception.UserAlreadyHasToken;
import org.kushtrimhajrizi.rpalace.exception.UserDoesNotExist;

public interface UserService {

    void save(UserDTO userDTO) throws UserAlreadyExistsException;

    void setToken(int id, String token) throws UserAlreadyHasToken, UserDoesNotExist;
}
