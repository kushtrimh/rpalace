package org.kushtrimhajrizi.rpalace.service.impl;

import org.kushtrimhajrizi.rpalace.dto.UserDTO;
import org.kushtrimhajrizi.rpalace.entity.DefaultUser;
import org.kushtrimhajrizi.rpalace.entity.Permission;
import org.kushtrimhajrizi.rpalace.exception.UserAlreadyExistsException;
import org.kushtrimhajrizi.rpalace.exception.UserAlreadyHasToken;
import org.kushtrimhajrizi.rpalace.exception.UserDoesNotExist;
import org.kushtrimhajrizi.rpalace.repository.UserRepository;
import org.kushtrimhajrizi.rpalace.security.PermissionType;
import org.kushtrimhajrizi.rpalace.service.PermissionService;
import org.kushtrimhajrizi.rpalace.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PermissionService permissionService;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository,
                           PermissionService permissionService,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.permissionService = permissionService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void save(UserDTO userDTO) throws UserAlreadyExistsException {
        if (userDTO == null) {
            throw new IllegalArgumentException("User DTO is required");
        }
        DefaultUser user = userRepository.findByEmail(userDTO.getEmail());
        if (user != null) {
            throw new UserAlreadyExistsException("User already exist");
        }
        String encodedPassword = Optional.ofNullable(userDTO.getPassword())
                .map(passwordEncoder::encode).orElseThrow(() -> new IllegalArgumentException("Password is required"));
        DefaultUser newUser = DefaultUser.fromEmailAndPassword(userDTO.getEmail(), encodedPassword);
        userRepository.save(newUser);
    }

    @Override
    @Transactional
    public void setToken(int id, String token) throws UserDoesNotExist, UserAlreadyHasToken {
        Optional<DefaultUser> userOptional = userRepository.findById(id);
        if (userOptional.isEmpty()) {
            throw new UserDoesNotExist();
        }

        DefaultUser user = userOptional.get();
        if (user.getToken() != null) {
            throw new UserAlreadyHasToken();
        }
        user.setToken(token);
        Optional<Permission> permissionOptional = permissionService.findByPermissionType(PermissionType.TOKEN_USER);
        permissionOptional.ifPresent(permission -> user.getPermissions().add(permission));
    }
}
