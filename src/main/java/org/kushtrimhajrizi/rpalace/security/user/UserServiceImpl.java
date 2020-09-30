package org.kushtrimhajrizi.rpalace.security.user;

import org.kushtrimhajrizi.rpalace.exception.UserAlreadyExistsException;
import org.kushtrimhajrizi.rpalace.oauth.authserver.accesstoken.versioning.AccessTokenVersion;
import org.kushtrimhajrizi.rpalace.oauth.authserver.accesstoken.versioning.AccessTokenVersionService;
import org.kushtrimhajrizi.rpalace.security.authority.Authority;
import org.kushtrimhajrizi.rpalace.security.authority.DefinedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private AccessTokenVersionService accessTokenVersionService;
    private PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository,
                           AccessTokenVersionService accessTokenVersionService,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.accessTokenVersionService = accessTokenVersionService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void save(UserDTO userDTO) throws UserAlreadyExistsException {
        if (userDTO == null) {
            throw new IllegalArgumentException("User DTO is required");
        }
        if (userRepository.findByEmail(userDTO.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException("User already exists");
        }
        String encodedPassword = Optional.ofNullable(userDTO.getPassword())
                .map(passwordEncoder::encode).orElseThrow(() -> new IllegalArgumentException("Password is required"));
        User newUser = User.fromEmailAndPassword(userDTO.getEmail(), encodedPassword);
        newUser.addAuthority(new Authority(DefinedAuthority.USER));
        newUser.addAccessTokenVersion(new AccessTokenVersion(accessTokenVersionService.generateAccessTokenVersion()));
        userRepository.save(newUser);
    }

    @Override
    public Optional<User> getByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public Optional<User> getById(String id) {
        return userRepository.findById(id);
    }
}
